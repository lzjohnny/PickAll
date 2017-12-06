package cn.xidianedu.pickall.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.xidianedu.pickall.R;
import cn.xidianedu.pickall.adapter.DividerItemDecoration;
import cn.xidianedu.pickall.adapter.UserInfoRecyclerViewAdapter;
import cn.xidianedu.pickall.bean.UserInfo;
import cn.xidianedu.pickall.fragment.ImageFragment;
import cn.xidianedu.pickall.http.AsyncHttpClient;
import cn.xidianedu.pickall.http.TextResponseHandler;
import cn.xidianedu.pickall.utils.CommonUtil;

/**
 * onCreate阶段从网络加载用户信息（昵称，介绍等）
 * 读取成功后写入本地XML文件
 * onRestart阶段从本地XML读取信息更新UI
 * （UserInfoEditActivity更新设置后，不需要从网络重新加载）
 * Created by ShiningForever on 2016/8/12.
 */
public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int INFO_COMPLETE = 1;
    private static final int INFO_FAIL = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case INFO_COMPLETE:
                    UserInfo userInfo = (UserInfo) msg.obj;
                    String id = userInfo.getId();
                    String name = userInfo.getName();
                    String sex = userInfo.getSex();
                    String signature = userInfo.getSignature();
                    String grade = userInfo.getGrade();
                    String course_num = userInfo.getCourse_num();

                    // 更新UI
                    showUserInfo(id, name, sex, signature, grade, course_num);
                    // 写入本地XML文件
                    SharedPreferences.Editor editor = prefUser.edit();
                    editor.putString("id", id);
                    editor.putString("name", name);
                    editor.putString("sex", sex);
                    editor.putString("signature", signature);
                    editor.putString("grade", grade);
                    editor.putString("course_num", course_num);
                    editor.apply();
                    break;
                case INFO_FAIL:
                    Toast.makeText(UserInfoActivity.this, "无法获取到用户信息", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    SharedPreferences prefLogin = null;
    SharedPreferences prefUser = null;
    RecyclerView recyclerView = null;

    List<String> contentList = null;
    String[] sexItems = {" 男 ", " 女 "};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);

        prefLogin = getSharedPreferences("login_info", MODE_PRIVATE);
        prefUser = getSharedPreferences("user_info", MODE_PRIVATE);

        String id = prefLogin.getString("id", "error");
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://10.0.2.2/id_" + id + ".json", new TextResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                UserInfo userInfo = gson.fromJson(response, UserInfo.class);

                Message message = Message.obtain();
                message.what = INFO_COMPLETE;
                message.obj = userInfo;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Throwable e) {
                Message message = Message.obtain();
                message.what = INFO_FAIL;
                handler.sendMessage(message);
            }
        });
        Toolbar userInfoToolBar = (Toolbar) findViewById(R.id.user_info_toolbar);
        userInfoToolBar.setTitle(id);
        setSupportActionBar(userInfoToolBar);

        userInfoToolBar.setNavigationIcon(R.drawable.ic_arraw_back_white);

        recyclerView = (RecyclerView) findViewById(R.id.user_info_rv);


        Button btnExit = (Button) findViewById(R.id.user_info_exit);
        btnExit.setOnClickListener(this);

        ImageView imageView = (ImageView) findViewById(R.id.user_info_img);
        imageView.setOnClickListener(this);
    }

    // 从本地XML读取信息更新UI
    @Override
    protected void onRestart() {
        super.onRestart();

        String id = prefUser.getString("id", "error");
        String name = prefUser.getString("name", "error");
        String sex = prefUser.getString("sex", "error");
        String signature = prefUser.getString("signature", "error");
        String grade = prefUser.getString("grade", "error");
        String course_num = prefUser.getString("course_num", "error");

        showUserInfo(id, name, sex, signature, grade, course_num);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            //返回按钮点击事件
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_add:
//                Toast.makeText(this, "编辑", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, UserInfoEditActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onDestroy() {
        Log.d("UserInfoActivity", "destory");
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.user_info_exit:
                SharedPreferences.Editor editor = getSharedPreferences("login_info", MODE_PRIVATE).edit();
                editor.putBoolean("is_login", false);
                editor.apply();
                this.finish();
                break;
            case R.id.user_info_img:
                ImageFragment imageFragment = new ImageFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(android.R.id.content, imageFragment);
                transaction.addToBackStack(null);
                transaction.commit();
//                Toast.makeText(UserInfoActivity.this, "查看头像", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private void showUserInfo(String id, String name, String sex, String signature, String grade, String course_num) {
        if (contentList == null)
            contentList = new ArrayList<>();
        else
            contentList.clear();
        contentList.add(id);
        contentList.add(name);
        contentList.add(sexItems[Integer.parseInt(sex)]);
        contentList.add(signature);
        contentList.add(grade);
        contentList.add(course_num);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        UserInfoRecyclerViewAdapter adapter = new UserInfoRecyclerViewAdapter(this, contentList);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        divider.setmDividerLeftMargin(CommonUtil.dip2px(20));
        divider.setmDividerRightMargin(CommonUtil.dip2px(20));
        recyclerView.addItemDecoration(divider);
        adapter.setOnItemClickListener(new UserInfoRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(UserInfoActivity.this, "屠龙宝刀点击就送!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
