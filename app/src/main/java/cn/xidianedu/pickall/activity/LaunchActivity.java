package cn.xidianedu.pickall.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ShiningForever on 2016/7/31.
 * 判断是否为首次登录，可实现首次登录和非首次登录跳转到不同页面（现在统一跳到GuideActivity）
 * 是否首次登录的信息用SharedPreferences保存
 */
public class LaunchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Bmob.initialize(this, "7d1f229b07364a354e1ec121af159e39"); // Bomb后端云SDK初始化

        SharedPreferences pref = getSharedPreferences("app_config", MODE_PRIVATE);
        boolean firstLaunch = pref.getBoolean("first_launch", true);
        if (firstLaunch) {
            Intent intent = new Intent(this, GuideActivity.class);
            startActivity(intent);
        } else {
//            Intent intent = new Intent(this, MainActivity.class);
            Intent intent = new Intent(this, GuideActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
