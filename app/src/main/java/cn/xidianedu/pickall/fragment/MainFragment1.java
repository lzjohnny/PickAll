package cn.xidianedu.pickall.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.xidianedu.pickall.R;
import cn.xidianedu.pickall.activity.SearchActivity;
import cn.xidianedu.pickall.adapter.DividerItemDecoration;
import cn.xidianedu.pickall.adapter.MainFrag1RecyclerViewAdapter;
import cn.xidianedu.pickall.bean.MainHeadData;
import cn.xidianedu.pickall.bean.PickParkBean;
//getContext() API不适合？

/**
 * Created by ShiningForever on 2016/8/3.
 * 主布局：采摘
 */

public class MainFragment1 extends Fragment implements View.OnClickListener {
    Context mContext;
    MainFrag1RecyclerViewAdapter adapter = null;

    private RecyclerView recyclerView;
    private LinearLayout linearLayoutLocation;  //当前位置按钮
    private SwipeRefreshLayout refresh;
    private RelativeLayout searchBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

        getMainHeadData();
        getMainListData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_1, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.main_frag_1_recycle_view);
        linearLayoutLocation = (LinearLayout) view.findViewById(R.id.main_frag_1_location);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.main_frag_1_swipe_refresh);
        searchBar = (RelativeLayout) view.findViewById(R.id.search_bar);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 下拉刷新监听先写在这里
                getMainHeadData();
            }
        });

        searchBar.setOnClickListener(this);
        adapter = new MainFrag1RecyclerViewAdapter(mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        DividerItemDecoration divider = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(divider);
//        linearLayoutLocation.setOnClickListener(this);

        linearLayoutLocation.setOnClickListener(this);
    }

    public void getMainHeadData() {
        BmobQuery<MainHeadData> query = new BmobQuery<>();
        query.addWhereEqualTo("usage", "main_head");
        query.order("order");
        query.findObjects(new FindListener<MainHeadData>() {
            @Override
            public void done(List<MainHeadData> list, BmobException e) {
                refresh.setRefreshing(false);
                if (e == null) {
                    adapter.addHeadSrc(list);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "图片加载失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getMainListData() {
        BmobQuery<PickParkBean> query = new BmobQuery<>();
        query.addWhereEqualTo("usage", "main_list");
        query.findObjects(new FindListener<PickParkBean>() {
            @Override
            public void done(List<PickParkBean> list, BmobException e) {
                if (e == null) {
                    adapter.addListSrc(list);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "列表加载失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_frag_1_location:
                break;
            case R.id.search_bar:
                Intent intent = new Intent(mContext, SearchActivity.class);
                startActivity(intent);
                break;
        }
    }
}