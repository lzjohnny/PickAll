package cn.xidianedu.pickall.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudRgcResult;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.LocalSearchInfo;

import java.util.ArrayList;
import java.util.List;

import cn.xidianedu.pickall.R;
import cn.xidianedu.pickall.adapter.MainFragmentPagerAdapter;
import cn.xidianedu.pickall.fragment.MainFragment1;
import cn.xidianedu.pickall.fragment.MainFragment2;
import cn.xidianedu.pickall.fragment.MainFragment3;
import cn.xidianedu.pickall.fragment.MainFragment4;
import cn.xidianedu.pickall.view.NoScrollViewPager;

/**
 * 最主要的activity，包含了“采摘”、“地图”、“我的”三个Fragment页面
 */

public class MainActivity extends BaseActivity implements CloudListener{

    private long mExitTime;
    MainFragment2 fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        setSupportActionBar(toolbar);

        List<Fragment> fragmentList = new ArrayList<>();
        MainFragment1 fragment1 = new MainFragment1();
        fragment2 = new MainFragment2();
//        MainFragment3 fragment3 = new MainFragment3();
        MainFragment4 fragment4 = new MainFragment4();
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
//        fragmentList.add(fragment3);
        fragmentList.add(fragment4);

        NoScrollViewPager viewPager = (NoScrollViewPager) findViewById(R.id.main_view_pager);
        MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(getFragmentManager(), this, fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
//        mTabLayout.addTab(mTabLayout.newTab().setText("TabOne"));
//        mTabLayout.addTab(mTabLayout.newTab().setText("TabTwo"));
//        mTabLayout.addTab(mTabLayout.newTab().setText("TabThree"));
        mTabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(adapter.getTabView(i));
            }
        }

        //地图相关初始化
        CloudManager.getInstance().init(MainActivity.this);
        requestPickParkLocation();
    }

    private void requestPickParkLocation() {
        LocalSearchInfo info = new LocalSearchInfo();
        info.ak = "1fjoP27Ie6xmqCSelUxRj4cAW4WD2GWY";
        info.geoTableId = 169103;
        info.tags = "";
//        info.q = "天安门";
        info.region = "西安市";
        CloudManager.getInstance().localSearch(info);
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 1500) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CloudManager.getInstance().destroy();
    }

    @Override
    public void onGetSearchResult(CloudSearchResult cloudSearchResult, int i) {
        Log.d("MainActivity", "onGetSearchResult");
        fragment2.onGetSearchResult(cloudSearchResult, i);
    }

    @Override
    public void onGetDetailSearchResult(DetailSearchResult detailSearchResult, int i) {
        Log.d("MainActivity", "onGetDetailSearchResult");
    }

    @Override
    public void onGetCloudRgcResult(CloudRgcResult cloudRgcResult, int i) {
        Log.d("MainActivity", "onGetCloudRgcResult");
    }
}