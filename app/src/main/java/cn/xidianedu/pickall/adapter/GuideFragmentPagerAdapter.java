package cn.xidianedu.pickall.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by ShiningForever on 2016/7/31.
 */

public class GuideFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mGuideFragmentList;
    public GuideFragmentPagerAdapter(FragmentManager manager, List<Fragment> fragmentList) {
        super(manager);
        mGuideFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mGuideFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mGuideFragmentList.size();
    }
}
