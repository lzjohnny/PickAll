package cn.xidianedu.pickall.notify;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Pair;
import android.util.SparseArray;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.xidianedu.pickall.utils.InitLog;

/**
 * Created by fanxi on 11/11/14.
 * <p>
 * Event dispatcher, keep and manager all EventHandler
 * the dispatch order of event is from the newest to the oldest
 * EventCenter will run on depend thread , so it almost does not
 * cost any main thread run time
 * </p>
 */

public class EventCenter extends HandlerThread {
    private static final String TAG = "EventCenter";

    /**
     * more Event receive name
     */
    private static final String METHOD_NAME = "onEvent";

    /**
     * Register command will find every event that the specified
     * handler will receive. The implement function is {@link #registerHandler(EventHandler)}
     */
    private final static int COMMAND_REGISTER = 0;

    /**
     * Unregister command will remove the specified handler from {@link #mHandlers}
     * then this handler will never receive any event until register it again;
     */
    private final static int COMMAND_UNREGISTER = 1;

    /**
     * Event send command , the main function which dispatch event to the Receiver
     */
    private static final int COMMAND_SEND_EVENT = 2;


    private final static EventCenter sInstance = new EventCenter();

    /**
     * Any new message will be cached , before EventCenter's thread is ready
     * and the Dispatcher Handler is created!
     */
    private List<Message> mMessageCache = new ArrayList<Message>();

    /**
     * Internal {@link Message} Dispatcher and Handler;
     */
    private EventDispatcher mDispatcher = null;

    /**
     * if the specified EventHandler is registered in one lifecycle
     * (From app be created in system , until destroy by user or system),
     * the mapping information about events and methods of class those belong to
     * this kind of EventHandler will be cached, so when next time the register
     * process will be more quick and effective
     */
    private final static Map<Class<? extends EventHandler>, Pair<int[], SparseArray<Method>>>
            HANDLER_TO_EVENT = new HashMap<Class<? extends EventHandler>, Pair<int[], SparseArray<Method>>>();

    /**
     * the mapping relation between event and handler
     */
    public SparseArray<List<EventHandler>> mHandlers = new SparseArray<List<EventHandler>>();

    private EventCenter() {
        super(TAG);
        start();
    }

    @Override
    protected void onLooperPrepared() {
        mDispatcher = new EventDispatcher();

        for (Message message : mMessageCache) {
            mDispatcher.sendMessage(message);
        }

        mMessageCache.clear();
        mMessageCache = null;
    }

    public static EventCenter getInstance() {
        return sInstance;
    }


    /**
     * Register event handler to EventCenter
     *
     * @param handler the EventHandle should be register into EventCenter
     */
    final void register(EventHandler handler) {
        InitLog.d(TAG, "[handler:%s] [mDispatcher:%b]", handler.getClass().getName(),
                mDispatcher == null);

        if (mDispatcher == null) {
            Message message = Message.obtain();
            message.what = COMMAND_REGISTER;
            message.obj = handler;
            mMessageCache.add(message);
        } else {
            Message message = mDispatcher.obtainMessage(COMMAND_REGISTER);
            message.obj = handler;
            mDispatcher.sendMessage(message);
        }
    }

    /**
     * Send event notification to every handler which need the kind of event
     *
     * @param event the Event which need be sent
     */
    public void send(Event event) {
        if (mDispatcher == null) {
            Message message = Message.obtain();
            message.what = COMMAND_SEND_EVENT;
            message.arg1 = event.getEventId();
            message.arg2 = Integer.MAX_VALUE;
            message.obj = event;
            mMessageCache.add(message);
        } else {
            Message message = mDispatcher.obtainMessage(COMMAND_SEND_EVENT);
            message.arg1 = event.getEventId();
            message.arg2 = Integer.MAX_VALUE;
            message.obj = event;

            mDispatcher.sendMessage(message);
        }
    }

    final void sendInternal(Message msg) {
        if (mDispatcher == null) {
            return;
        }
        Message message = mDispatcher.obtainMessage(COMMAND_SEND_EVENT);
        message.arg1 = msg.arg1;
        message.arg2 = msg.arg2;
        message.obj = msg.obj;
        mDispatcher.sendMessage(message);
    }


    /**
     * The implements about how to register a handler to EventCenter
     *
     * @param handler the handler need be registered
     */
    private void registerHandler(EventHandler handler) {
        if (handler.getId() < 0) return;

        Class<? extends EventHandler> clazz = handler.getClass();
        Pair<int[], SparseArray<Method>> item = HANDLER_TO_EVENT.get(clazz);
        if (item == null) {
            Method[] methods = clazz.getDeclaredMethods();
            SparseArray<Method> functions = new SparseArray<Method>();

            InitLog.d(TAG, "[handler:%s] [methods:%d]", clazz.getName(), methods.length);

            for (Method method : methods) {
                String name = method.getName();
                InitLog.d(TAG, "[method:%s] [objectClazz:%s]", name, clazz.getName());

                //过滤参数个数
                Class args[] = method.getParameterTypes();
                if (args.length != 1) {
                    continue;
                }

                //过滤参数类型
                Class type = args[0];
                InitLog.d(TAG, "[type:%s] [EventClazz:%s]", type.getName(), Event.class.getName());
                if (!isSubObjectOf(type,Event.class)) {
                    continue;
                }


                //注册事件处理方法
                int eventId = Event.getEventId(type);
                functions.put(eventId, method);
            }

            int size = functions.size();
            int[] events = new int[size];

            for (int i = 0; i < size; i++) {
                int eventId = functions.keyAt(i);
                events[i] = eventId;
            }

            item = new Pair<int[], SparseArray<Method>>(events, functions);
            HANDLER_TO_EVENT.put(clazz, item);
        }

        for (int event : item.first) {
            List<EventHandler> handlers = sInstance.mHandlers.get(event);
            if (handlers == null) {
                handlers = new ArrayList<EventHandler>();
                sInstance.mHandlers.put(event, handlers);
            }
            handlers.add(handler);
        }

        handler.sendFunctions(item.second);
    }

    /**
     * remove handler from event center , so handler will never receive any event
     *
     * @param handler the specified handler
     */
    public void unregister(EventHandler handler) {
        if (mDispatcher == null) {
            Message message = Message.obtain();
            message.obj = handler;
            message.what = COMMAND_UNREGISTER;
            mMessageCache.add(message);
        } else {
            Message message = mDispatcher.obtainMessage(COMMAND_UNREGISTER);
            message.obj = handler;
            mDispatcher.sendMessage(message);
        }
    }

    /**
     * 判断某个类型是否是另一个类型的子类
     * */
    public static boolean isSubObjectOf(Class clazz, Class objectClazz) {
        do {
            if (clazz == objectClazz) return true;
            InitLog.d(TAG, "[clazz:%s] [objectClazz:%s]", clazz.getName(), objectClazz.getName());

            Class[] interfaces = clazz.getInterfaces();
            for (Class item : interfaces) {
                if (item == objectClazz) {
                    return true;
                }
            }

            clazz = clazz.getSuperclass();
        } while (clazz != Object.class);
        return false;
    }

    /**
     * The internal message Dispatcher
     */
    private static class EventDispatcher extends Handler {

        @Override
        public void handleMessage(Message msg) {
            int commend = msg.what;
            InitLog.d(TAG, "[handler:%s] [commend:%d]", msg.obj.getClass().getName(),
                    commend);

            switch (commend) {
                case COMMAND_UNREGISTER: {
                    EventHandler handler = (EventHandler) msg.obj;
                    Pair<int[], SparseArray<Method>> item = HANDLER_TO_EVENT.get(handler.getClass());
                    if (item != null && item.first != null) {
                        for (int event : item.first) {
                            List<EventHandler> handlers = sInstance.mHandlers.get(event);
                            handlers.remove(handler);
                        }
                    }
                    break;
                }

                case COMMAND_REGISTER: {
                    EventHandler handler = (EventHandler) msg.obj;
                    sInstance.registerHandler(handler);
                    break;
                }

                case COMMAND_SEND_EVENT: {

                    int eventId = msg.arg1;
                    int lastHandlerId = msg.arg2;
                    List<EventHandler> handlers = sInstance.mHandlers.get(eventId);

                    if (handlers != null) {
                        int size = handlers.size();
                        for (int i = size - 1; i >= 0; i--) {
                            EventHandler handler = handlers.get(i);
                            int handlerId = handler.getId();
                            if (handlerId >= lastHandlerId || handlerId < 0) {
                                continue;
                            }

                            Message newMessage = handler.obtainMessage();
                            newMessage.what = COMMAND_SEND_EVENT;
                            newMessage.arg1 = eventId;
                            newMessage.arg2 = handlerId;
                            newMessage.obj = msg.obj;
                            handler.sendMessage(newMessage);
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

}
