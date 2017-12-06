package cn.xidianedu.pickall;

import android.app.Application;
import com.baidu.mapapi.SDKInitializer;

import cn.xidianedu.pickall.storage.GreenDaoHelper;
import cn.xidianedu.pickall.utils.CommonUtil;


/**
 * Created by ShiningForever on 2017/2/1.
 */

public class PickAllApplication extends Application {
    private static PickAllApplication mApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        initApplication();
    }

    private void initApplication() {
        CommonUtil.init(this);
        SDKInitializer.initialize(this);
        GreenDaoHelper.initDatabase(this);
    }

    public static PickAllApplication getInstance() {
        return mApplication;
    }
}

//    信任所有https域名
//    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
//        @Override
//        public boolean verify(String hostname, SSLSession session) {
//            return true;
//        }
//    });
