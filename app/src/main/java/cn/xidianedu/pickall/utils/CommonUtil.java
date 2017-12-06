package cn.xidianedu.pickall.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import cn.xidianedu.pickall.PickAllApplication;

public class CommonUtil {
    private static ConnectivityManager connectivityManager;
    private static String CHANNEL = "default";
    private static String versionName;
    private static int versionCode;

    public static void init(Context context) {
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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

    public static boolean isNetworkAvailable() {
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.isConnectedOrConnecting();
    }

    public static String getMobileTypeNetwork() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return "OTHER";
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return "WIFI";
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_WIMAX) {
            return "WIMAX";
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return networkInfo.getSubtypeName();
        } else {
            return "OTHER";
        }
    }

    public static int dip2px(float dpValue) {
        final float scale = PickAllApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(float pxValue) {
        final float scale = PickAllApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String sqliteEscape(String keyWord) {
        keyWord = keyWord.replace("/", "//");
        keyWord = keyWord.replace("'", "''");
        keyWord = keyWord.replace("[", "/[");
        keyWord = keyWord.replace("]", "/]");
        keyWord = keyWord.replace("%", "/%");
        keyWord = keyWord.replace("&", "/&");
        keyWord = keyWord.replace("_", "/_");
        keyWord = keyWord.replace("(", "/(");
        keyWord = keyWord.replace(")", "/)");
        return keyWord;
    }
}
