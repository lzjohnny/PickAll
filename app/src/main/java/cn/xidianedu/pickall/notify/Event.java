package cn.xidianedu.pickall.notify;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by fanxi on 9/26/14.
 * Event only thread safe , do not support transparent between process
 */
public abstract class Event {
    /**
     * allocate event id on runtime
     *
     * TODO implement file allocate , may be will support process event transparent
     * */
    private static final AtomicInteger ID_ALLOCATE = new AtomicInteger();
    public final static Map<Class<? extends Event>, Integer> eventIds = new HashMap<Class<? extends Event>, Integer>();

    private int mEventId = -1;

    /**
     * Returns the event id  of the specified event type.
     * if event ID  does not be allocated , the the id will be allocated
     *
     * @param clazz event class type
     *
     * @return int  eventID
     * */
    public static int getEventId(Class<? extends Event> clazz) {
        Integer id = eventIds.get(clazz);

        if (id == null)  {
            id = ID_ALLOCATE.addAndGet(1);
            eventIds.put(clazz, id);
        }

        return id;
    }

    /**
     * Return current event id , this way more quickly then {@link #getEventId(Class)}
     *
     * @return int eventID
     * */
    public final int getEventId() {
        if (mEventId < 0) {
            mEventId = getEventId(this.getClass());
        }
        return mEventId;
    }
}
