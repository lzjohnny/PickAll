package cn.xidianedu.pickall.utils;

import android.text.TextUtils;
import android.util.Log;

public class InitLog {

    public static void d(String tag, String format, Object... args) {
        Log.d(tag, formatArguments(format, args));
    }

    public static void d(String format, Object... args) {
        String tag = getTag();
        Log.d(tag, formatArguments(format, args));
    }

    private static String getTag() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        if (trace.length >= 4) {
            String className = trace[4].getClassName();
            int index = className.lastIndexOf(".") + 1;
            return className.substring(index);
        }
        return "";
    }

    private static String formatArguments(String format, Object... args) {
        if (args == null || args.length == 0) return format;

        if (!TextUtils.isEmpty(format)) {
            try {
                return String.format(format, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        StringBuilder builder = new StringBuilder(format);
        for (Object arg : args) {
            builder.append("[").append(arg).append("] ");
        }
        return builder.toString();
    }

}
