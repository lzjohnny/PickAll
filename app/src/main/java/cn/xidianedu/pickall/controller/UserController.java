package cn.xidianedu.pickall.controller;

import cn.xidianedu.pickall.storage.dbmodel.UserModel;
import cn.xidianedu.pickall.storage.storage.UserStorage;

/**
 * Created by ShiningForever on 2017/12/3.
 */

public class UserController extends BaseController {
    private static UserController mInstance = new UserController();
    private UserStorage userStorage;

    public static UserController getInstance() {
        return mInstance;
    }

    private UserController() {
        super();
        userStorage = new UserStorage();
    }

    // 更新本地数据库并上传至服务器
    public void updateUserInfo() {

    }

    // 先从本地加载数据，同时调服务器接口，获取成功后通知数据刷新
    public UserModel loadUserInfo() {
        return null;
    }
}
