package cn.xidianedu.pickall.notify;

import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

import cn.xidianedu.pickall.utils.InitLog;

/**
 * Created by fanxi on 11/11/14.
 * event dispatcher and method mapping
 */
public abstract class EventHandler extends Handler {
    private static final String TAG = "EventHandler";

    /**
     * Handler id allocate
     * */
    public static final AtomicInteger HANDLER_ID_ALLOCATE = new AtomicInteger(0);

    private EventCenter mEventCenter = EventCenter.getInstance();

    private SparseArray<Method> mEventFunctions = null;

    private int mHandlerId = -1;

    public int getId() {
        return mHandlerId;
    }

    @Override
    public void handleMessage(Message msg) {
        int eventId = msg.arg1;

        Method method = mEventFunctions.get(eventId);

        Event event = (Event) msg.obj;
        if (method != null) {
            try {
                Object result = method.invoke(this, event);
                if (result instanceof Boolean) {
                    Boolean consumed = (Boolean) result;
                    if (consumed) {
                        // if the event is consumed , the event will stop transport
                        return;
                    }
                }
                msg.arg2 = mHandlerId;
                mEventCenter.sendInternal(msg);
            } catch (IllegalAccessException e) {
                InitLog.d(TAG, "Method access error!", e);
            } catch (InvocationTargetException e) {
                InitLog.d(TAG, "Method invoke error!", e);
            }
        }
    }

    public final void register() {
        if (mHandlerId > 0) {
            throw new RuntimeException("EventHandler do invoke unregister after register");
        }

        mHandlerId = HANDLER_ID_ALLOCATE.getAndIncrement();
        mEventCenter.register(this);
    }

    //延时删除，后台进程删除，减少UI主线程占用
    public final void unregister() {
        mHandlerId = -1;
        mEventCenter.unregister(this);
    }

    final void sendFunctions(SparseArray<Method> functions) {
        mEventFunctions = functions;
    }
}
