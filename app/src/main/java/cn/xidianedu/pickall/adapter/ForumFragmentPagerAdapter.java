package cn.xidianedu.pickall.adapter;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cn.xidianedu.pickall.R;

/**
 * Created by ShiningForever on 2016/8/29.
 */
public class ForumFragmentPagerAdapter extends FragmentPagerAdapter {
    Context context;
    List<Fragment> fragmentList;
    String[] titles = {"所有话题", "我的话题", "我的回复"};

    public ForumFragmentPagerAdapter(FragmentManager fm, Context context, List<Fragment> fragmentList) {
        super(fm);
        this.context = context;
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_forum, null);
        TextView textView = (TextView) view.findViewById(R.id.tab_forum_tv);
        textView.setText(titles[position]);
        return view;
    }
}
