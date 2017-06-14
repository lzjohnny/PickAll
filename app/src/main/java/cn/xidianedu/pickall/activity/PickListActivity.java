package cn.xidianedu.pickall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.xidianedu.pickall.R;
import cn.xidianedu.pickall.adapter.DividerItemDecoration;
import cn.xidianedu.pickall.adapter.PickListActivityAdapter;
import cn.xidianedu.pickall.bean.PickParkBean;

/**
 * Created by ShiningForever on 2017/5/10.
 */

public class PickListActivity extends AppCompatActivity {
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_list);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");

        Toolbar toolbar = (Toolbar) findViewById(R.id.pick_list_toolbar);
        toolbar.setTitle(title);
        toolbar.setNavigationIcon(R.drawable.ic_arraw_back_white);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.pick_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(divider);
        final PickListActivityAdapter adapter = new PickListActivityAdapter(this);
        recyclerView.setAdapter(adapter);

        BmobQuery<PickParkBean> query = new BmobQuery<>();
        query.addWhereEqualTo("category", title);
        query.findObjects(new FindListener<PickParkBean>() {
            @Override
            public void done(List<PickParkBean> list, BmobException e) {
                if (e == null) {
                    Log.d("----------", list.toString());
                    adapter.addSrcAndNotify(list);
                } else {
                    Toast.makeText(PickListActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                    Log.i("Bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
