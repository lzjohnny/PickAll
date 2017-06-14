package cn.xidianedu.pickall.account;

import org.litepal.LitePal;

/**
 * Created by ShiningForever on 2017/5/1.
 * 账户信息、登录状态管理类，暂时没用上，以后重构也许有用
 */

public class AccountManager {
//    static Context mContext = MyApplication.getContext();

    private static final AccountManager MANAGER = new AccountManager();
    public static AccountManager getInstance() {
        return MANAGER;
    }

    private AccountManager() {
        LitePal.getDatabase();
    }

    public static boolean isLogin() {
//        SharedPreferences pref = mContext.getSharedPreferences("login_info", Context.MODE_PRIVATE);
//        pref.getBoolean("is_login", false);
        return true;
    }

}
