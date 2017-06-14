package cn.xidianedu.pickall.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.xidianedu.pickall.R;

/**
 * Created by ShiningForever on 2016/8/3.
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mMainFragmentList;
    private Context mContext;
    private String[] titles = {"采摘", "地图", "信息", "更多"};
    private int[] icons = {R.drawable.tab_ic_home_selector, R.drawable.tab_ic_map_selector,
            R.drawable.tab_ic_info_selector, R.drawable.tab_ic_more_selector};


    public MainFragmentPagerAdapter(FragmentManager fm, Context context, List<Fragment> fragmentList) {
        super(fm);
        mContext = context;
        mMainFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mMainFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mMainFragmentList.size();
    }

    // 底部导航栏布局
    public View getTabView(int position) {

        View view = View.inflate(mContext, R.layout.tab_main, null);
        ImageView iv = (ImageView) view.findViewById(R.id.tab_imageView);
        TextView tv = (TextView) view.findViewById(R.id.tab_textView);
        iv.setImageResource(icons[position]);
        tv.setText(titles[position]);
        return view;
    }
}
