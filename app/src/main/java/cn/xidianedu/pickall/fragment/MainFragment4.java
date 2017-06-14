package cn.xidianedu.pickall.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cn.xidianedu.pickall.R;
import cn.xidianedu.pickall.activity.LoginActivity;
import cn.xidianedu.pickall.activity.SettingActivity;
import cn.xidianedu.pickall.activity.UserInfoActivity;
import cn.xidianedu.pickall.adapter.DividerItemDecoration;
import cn.xidianedu.pickall.adapter.Fragment3ChoiceRecyclerViewAdapter;
import cn.xidianedu.pickall.utils.DensityUtil;

/**
 * Created by ShiningForever on 2016/8/3.
 * 主布局：更多
 */
public class MainFragment4 extends PreferenceFragment implements View.OnClickListener {
    SharedPreferences pref;
    Context context = null;

    int[] mChoiceItemImg1 = new int[]{R.drawable.ic_assignment_gray_24dp, R.drawable.ic_grade_gray_24dp, R.drawable.ic_people_gray_24dp};
    String[] mChoiceItemText1 = new String[]{"我的采摘", "关注的采摘园", "我的评论"};

    int[] mChoiceItemImg2 = new int[]{R.drawable.ic_build_gray_24dp, R.drawable.ic_info_gray};
    String[] mChoiceItemText2 = new String[]{"设置", "关于"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        //获取登录状态，以决定点击时的跳转页面
        pref = context.getSharedPreferences("login_info", Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_4, container, false);
        CardView userCard = (CardView) view.findViewById(R.id.user_card);
        userCard.setOnClickListener(this);

        DividerItemDecoration divider = new DividerItemDecoration(context, LinearLayoutManager.VERTICAL);
        // 分割线左侧距屏幕边缘的边距
        // 55dp为左侧图片的大小
        divider.setmDividerLeftMargin(DensityUtil.dip2px(context, 55));

        RecyclerView rvChoice = (RecyclerView) view.findViewById(R.id.rv_choice_fragment_3);
        rvChoice.setLayoutManager(new LinearLayoutManager(context));
        Fragment3ChoiceRecyclerViewAdapter adapter = new Fragment3ChoiceRecyclerViewAdapter(context, mChoiceItemImg1, mChoiceItemText1);
        rvChoice.setAdapter(adapter);
        rvChoice.addItemDecoration(divider);
        adapter.setOnItemClickListener(new Fragment3ChoiceRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(context, mChoiceItemText1[position], Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView rv2Choice = (RecyclerView) view.findViewById(R.id.rv2_choice_fragment_3);
        rv2Choice.setLayoutManager(new LinearLayoutManager(context));
        Fragment3ChoiceRecyclerViewAdapter adapter2 = new Fragment3ChoiceRecyclerViewAdapter(context, mChoiceItemImg2, mChoiceItemText2);
        rv2Choice.setAdapter(adapter2);

        rv2Choice.addItemDecoration(divider);
        adapter2.setOnItemClickListener(new Fragment3ChoiceRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Toast.makeText(context, mChoiceItemText2[position], Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0:
                        Intent intent = new Intent(context, SettingActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Toast.makeText(context, "developed by lzjohnny95", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_card:
                boolean isLogin = pref.getBoolean("is_login", false);
                if (isLogin) {
                    Intent intent = new Intent(context, UserInfoActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }
}