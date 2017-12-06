package cn.edu.xidian.sast.baselib.okhttp.utils;

/**
 * Created by ShiningForever on 2017/12/7.
 */

public class TypeUtil {

    // 判断clazzA是否是clazzB的子类
    public static boolean isSubObjectOf(Class clazzA, Class clazzB) {
        do {
            if (clazzA == clazzB) return true;

            Class[] interfaces = clazzA.getInterfaces();
            for (Class item : interfaces) {
                if (item == clazzB) {
                    return true;
                }
            }

            clazzA = clazzA.getSuperclass();
        } while (clazzA != Object.class);
        return false;
    }
}
