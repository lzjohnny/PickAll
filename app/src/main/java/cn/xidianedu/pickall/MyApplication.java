package cn.xidianedu.pickall;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

import org.litepal.LitePal;

/**
 * Created by ShiningForever on 2017/2/1.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(getApplicationContext());

//        x.Ext.init(this);
//        x.Ext.setDebug(BuildConfig.DEBUG);

        SDKInitializer.initialize(this);

        // 信任所有https域名
        /*HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });*/
    }
}
