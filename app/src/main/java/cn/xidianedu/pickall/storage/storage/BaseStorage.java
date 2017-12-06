package cn.xidianedu.pickall.storage.storage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import cn.xidianedu.pickall.PickAllApplication;

/**
 * Created by ShiningForever on 2017/12/3.
 */

public abstract class BaseStorage {
    private static Context mApplicationContext = PickAllApplication.getInstance().getApplicationContext();
    private String mSharePCacheName = "default_storage";

    // 以下是数据库操作封装
    // ...

    // 每个Storage类对应一个同名的SharePreferenceXML
    public BaseStorage(String sharePCacheName) {
        mSharePCacheName = sharePCacheName;
    }

    // 以下是SharedPreferences操作封装
    public boolean removeShareCache(String key) {
        SharedPreferences mySharedPreferences = mApplicationContext.getSharedPreferences(mSharePCacheName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.remove(key);
        return editor.commit();
    }

    public boolean saveStringCache(String key, String content) {
        SharedPreferences mySharedPreferences = mApplicationContext.getSharedPreferences(mSharePCacheName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(key, content);
        return editor.commit();
    }

    public String loadStringCache(String key) {
        SharedPreferences sharedPreferences = mApplicationContext.getSharedPreferences(mSharePCacheName, Activity.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public String loadStringCache(String key, String defaultValue) {
        SharedPreferences sharedPreferences = mApplicationContext.getSharedPreferences(mSharePCacheName, Activity.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }

    public boolean saveIntCache(String key, int content) {
        SharedPreferences mySharedPreferences = mApplicationContext.getSharedPreferences(mSharePCacheName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putInt(key, content);
        return editor.commit();
    }

    public int loadIntCache(String key) {
        SharedPreferences sharedPreferences = mApplicationContext.getSharedPreferences(mSharePCacheName, Activity.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 0);
    }

    public int loadIntCache(String key, int defaultValue) {
        SharedPreferences sharedPreferences = mApplicationContext.getSharedPreferences(mSharePCacheName, Activity.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultValue);
    }

    public boolean saveBooleanCache(String key, boolean content) {
        SharedPreferences mySharedPreferences = mApplicationContext.getSharedPreferences(mSharePCacheName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(key, content);
        return editor.commit();
    }

    public boolean loadBooleanCache(String key) {
        SharedPreferences sharedPreferences = mApplicationContext.getSharedPreferences(mSharePCacheName, Activity.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public boolean loadBooleanCache(String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = mApplicationContext.getSharedPreferences(mSharePCacheName, Activity.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public boolean saveFloatCache(String key, float content) {
        SharedPreferences mySharedPreferences = mApplicationContext.getSharedPreferences(mSharePCacheName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putFloat(key, content);
        return editor.commit();
    }

    public float loadFloatCache(String key) {
        SharedPreferences sharedPreferences = mApplicationContext.getSharedPreferences(mSharePCacheName, Activity.MODE_PRIVATE);
        return sharedPreferences.getFloat(key, 0);
    }

    public float loadFloatCache(String key, float def) {
        SharedPreferences sharedPreferences = mApplicationContext.getSharedPreferences(mSharePCacheName, Activity.MODE_PRIVATE);
        return sharedPreferences.getFloat(key, def);
    }

    public boolean saveLongCache(String key, long content) {
        SharedPreferences mySharedPreferences = mApplicationContext.getSharedPreferences(mSharePCacheName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putLong(key, content);
        return editor.commit();
    }

    public long loadLongCache(String key) {
        SharedPreferences sharedPreferences = mApplicationContext.getSharedPreferences(mSharePCacheName, Activity.MODE_PRIVATE);
        return sharedPreferences.getLong(key, 0);
    }

    public long loadLongCache(String key, long def) {
        SharedPreferences sharedPreferences = mApplicationContext.getSharedPreferences(mSharePCacheName, Activity.MODE_PRIVATE);
        return sharedPreferences.getLong(key, def);
    }
}
