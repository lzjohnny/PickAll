package cn.xidianedu.pickall.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;

import java.util.ArrayList;
import java.util.List;

import cn.xidianedu.pickall.R;
import cn.xidianedu.pickall.adapter.GuideFragmentPagerAdapter;
import cn.xidianedu.pickall.fragment.GuideFragment1;
import cn.xidianedu.pickall.fragment.GuideFragment2;
import cn.xidianedu.pickall.fragment.GuideFragment3;
import cn.xidianedu.pickall.fragment.GuideFragment4;
import cn.xidianedu.pickall.indicatorext.ScaleCircleNavigator;

/**
 * Created by ShiningForever on 2016/7/31.
 * 背景可滑动的启动页
 */

public class GuideActivity extends BaseActivity implements View.OnClickListener{
    private final static int GUIDE_PAGE_NUMBER = 4;
    private ViewPager viewPager;
    private Button btnStart;
    private Button btnLogin;

//    private MagicIndicator magicIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        viewPager = (ViewPager) findViewById(R.id.guide_view_pager);
        btnStart = (Button) findViewById(R.id.btn_guide_start);
        btnLogin = (Button) findViewById(R.id.btn_guide_login);

        btnStart.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        initData();
        initIndicator();
    }

    private void initData() {
        final List<Fragment> fragmentList = new ArrayList<>();

        GuideFragment1 fragment1 = new GuideFragment1();
        GuideFragment2 fragment2 = new GuideFragment2();
        GuideFragment3 fragment3 = new GuideFragment3();
        GuideFragment4 fragment4 = new GuideFragment4();
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        fragmentList.add(fragment3);
        fragmentList.add(fragment4);

        GuideFragmentPagerAdapter adapter = new GuideFragmentPagerAdapter(getFragmentManager(),
                fragmentList);
        viewPager.setAdapter(adapter);
    }

    // 设置小圆点指示器，使用开源库实现
    private void initIndicator() {
        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.indicator_guide);
        ScaleCircleNavigator scaleCircleNavigator = new ScaleCircleNavigator(this);
        scaleCircleNavigator.setCircleCount(4);
        scaleCircleNavigator.setNormalCircleColor(Color.LTGRAY);
        scaleCircleNavigator.setSelectedCircleColor(Color.DKGRAY);
        scaleCircleNavigator.setCircleClickListener(new ScaleCircleNavigator.OnCircleClickListener() {
            @Override
            public void onClick(int index) {
                viewPager.setCurrentItem(index);
            }
        });
        magicIndicator.setNavigator(scaleCircleNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_guide_start:
                start(v);
                break;
            case R.id.btn_guide_login:
                login(v);
                break;
        }
    }

    private void start(View v) {
        // 非首次登录
        SharedPreferences.Editor editor = getSharedPreferences("app_config", MODE_PRIVATE).edit();
        editor.putBoolean("first_launch", false);
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void login(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
