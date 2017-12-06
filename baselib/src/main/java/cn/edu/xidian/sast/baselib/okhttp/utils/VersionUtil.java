package cn.edu.xidian.sast.baselib.okhttp.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;

public class VersionUtil {
    private static String CHANNEL = "default";
    private static String versionName;
    private static int versionCode;

    static void init(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "1.0.0";
            versionCode = 100;
        }
    }

    public static String getVersionName() {
        return versionName;
    }
    public static int getVersionCode() {
        return versionCode;
    }
}
