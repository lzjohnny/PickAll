package cn.xidianedu.pickall.storage.storage;

import com.greendao.gen.UserModelDao;
import cn.xidianedu.pickall.storage.GreenDaoHelper;
import cn.xidianedu.pickall.storage.dbmodel.UserModel;

/**
 * Created by ShiningForever on 2017/12/3.
 */

public class UserStorage extends BaseStorage {
    private UserModelDao mUserDao = GreenDaoHelper.getDaoSession().getUserModelDao();

    public UserStorage() {
        super(UserStorage.class.getSimpleName());
    }

    // 增加或修改用户
    public void cacheUserInfo(UserModel user) {
        UserModel oldUser = getUserByUid(user.getUid());
        if (oldUser != null) {
            user.setId(oldUser.getId());
        }
        mUserDao.save(user);
    }

    // 清除全部数据
    public void clearAll() {
        mUserDao.deleteAll();
    }

    // 删除指定UID用户
    public void clearUserByUid(int uid) {
        Long id = getUserByUid(uid).getId();
        mUserDao.deleteByKey(id);
    }

    // 查询
    public UserModel getUserByUid(int uid) {
        return mUserDao.queryBuilder().where(UserModelDao.Properties.Uid.eq(uid)).unique();
    }

    // 注意save可能有坑：GreenDao在insert和update默认通过id判断数据是否存在，但程序逻辑上需要使用uid作为用户标志
    // 所以要先按uid判断表中是否有数据
    // 或者直接使用uid作为主键
    // 或者重写AbstractDao...
}
