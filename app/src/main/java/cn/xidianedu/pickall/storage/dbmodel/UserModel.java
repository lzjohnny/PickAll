package cn.xidianedu.pickall.storage.dbmodel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ShiningForever on 2017/12/3.
 */

@Entity
public class UserModel {
    @Id(autoincrement = true)
    private Long id;

    @NotNull
    @Index(unique = true)
    private int uid;

    @NotNull
    @Unique
    private String username;

    @Unique
    private String token;

    @Property(nameInDb = "base_info")
    private String baseInfo;

    @Property(nameInDb = "detail_info")
    private String detailInfo;

    @Generated(hash = 1445693454)
    public UserModel(Long id, int uid, @NotNull String username, String token, String baseInfo,
            String detailInfo) {
        this.id = id;
        this.uid = uid;
        this.username = username;
        this.token = token;
        this.baseInfo = baseInfo;
        this.detailInfo = detailInfo;
    }

    @Generated(hash = 782181818)
    public UserModel() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUid() {
        return this.uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBaseInfo() {
        return this.baseInfo;
    }

    public void setBaseInfo(String baseInfo) {
        this.baseInfo = baseInfo;
    }

    public String getDetailInfo() {
        return this.detailInfo;
    }

    public void setDetailInfo(String detailInfo) {
        this.detailInfo = detailInfo;
    }

}

// 用户表字段设计：id uid username token base_info(JSON: nickname & intro & avatar) detail_info(JSON: )

//实体注解：
// 如果你有超过一个的数据库结构，可以通过这个字段来区分
// 该实体属于哪个结构
//schema = "myschema",

//  实体是否激活的标志，激活的实体有更新，删除和刷新的方法
//active = true,

// 确定数据库中表的名称
// 表名称默认是实体类的名称
//nameInDb = "AWESOME_USERS",

// Define indexes spanning multiple columns here.
//indexes = {
//        @Index(value = "name DESC", unique = true)
//},

// DAO是否应该创建数据库表的标志(默认为true)
// 如果你有多对一的表，将这个字段设置为false
// 或者你已经在GreenDAO之外创建了表，也将其置为false
//createInDb = false
