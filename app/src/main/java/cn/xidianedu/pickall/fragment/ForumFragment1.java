package cn.xidianedu.pickall.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.xidianedu.pickall.R;
import cn.xidianedu.pickall.adapter.ForumFragment1RecyclerViewAdapter;
import cn.xidianedu.pickall.bean.ForumInfo;
import cn.xidianedu.pickall.bean.ForumItem;
import cn.xidianedu.pickall.http.AsyncHttpClient;
import cn.xidianedu.pickall.http.TextResponseHandler;

/**
 * Created by ShiningForever on 2016/8/29.
 * 小组论坛-所有话题
 */
public class ForumFragment1 extends Fragment {
    private static final int DATA_COMPLETE = 1;
    private static final int DATA_FAIL = 0;

    Context context;
    RecyclerView recyclerView;
    ForumFragment1RecyclerViewAdapter adapter;
    List<ForumItem> forumItemList = new ArrayList<>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DATA_COMPLETE:
                    adapter.notifyDataSetChanged();
                    break;
                case DATA_FAIL:
                    Toast.makeText(context, "更新数据失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        final Gson gson = new Gson();
        context = getActivity();

//        forumItemList.add(new ForumItem("http://1.mcdev.cn", "标题1", "标签1", "用户1", "1970.1.1", "1"));
//        forumItemList.add(new ForumItem("http://1.mcdev.cn", "标题2", "标签2", "用户2", "1970.2.2", "2"));
//        forumItemList.add(new ForumItem("http://1.mcdev.cn", "标题3", "标签3", "用户3", "1970.3.3", "3"));
//        forumItemList.add(new ForumItem("http://1.mcdev.cn", "标题4", "标签4", "用户4", "1970.4.4", "4"));
//        forumItemList.add(new ForumItem("http://1.mcdev.cn", "标题5", "标签5", "用户5", "1970.5.5", "5"));

        super.onCreate(savedInstanceState);
        //网络请求
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://10.0.2.2/forum_page_1.json", new TextResponseHandler() {
            @Override
            public void onSuccess(String response) {
                ForumInfo forumInfo = gson.fromJson(response, ForumInfo.class);
                forumItemList = forumInfo.getForum_item_list();

                Log.d("ForumFragment1", "onSuccess");
                handler.sendEmptyMessage(DATA_COMPLETE);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.d("ForumFragment1", "onFailure");
                handler.sendEmptyMessage(DATA_FAIL);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(context).inflate(R.layout.forum_fragment_1, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.forum_fragment_1_rv);

        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        adapter = new ForumFragment1RecyclerViewAdapter(forumItemList, context);
        recyclerView.setAdapter(adapter);
        return view;
    }

}
