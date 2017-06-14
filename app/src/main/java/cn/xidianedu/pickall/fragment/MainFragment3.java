package cn.xidianedu.pickall.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.xidianedu.pickall.R;
import cn.xidianedu.pickall.adapter.ForumFragmentPagerAdapter;

/**
 * Created by ShiningForever on 2016/8/3.
 * 主布局：论坛
 */
public class MainFragment3 extends Fragment implements View.OnClickListener{
    Context context;

    List<Fragment> fragmentList;
    TabLayout tabLayout;
    ViewPager viewPager;
    FloatingActionButton button;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        context = getActivity();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_3, container, false);
        initView(view);
        initValue();
        return view;
    }

    private void initValue() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new ForumFragment1());
        fragmentList.add(new ForumFragment2());
        fragmentList.add(new ForumFragment3());

        ForumFragmentPagerAdapter adapter = new ForumFragmentPagerAdapter(getFragmentManager(), context, fragmentList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null)
                tab.setCustomView(adapter.getTabView(i));
        }
        button.setOnClickListener(this);
    }

    private void initView(View v) {
        tabLayout = (TabLayout) v.findViewById(R.id.forum_tab_layout);
        viewPager = (ViewPager) v.findViewById(R.id.forum_view_pager);
        button = (FloatingActionButton) v.findViewById(R.id.forum_fab);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(context, "新话题", Toast.LENGTH_SHORT).show();
    }
}
