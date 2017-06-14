package cn.xidianedu.pickall.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.QueryListener;
import cn.xidianedu.pickall.R;
import cn.xidianedu.pickall.bean.PickParkBean;

/**
 * Created by ShiningForever on 2017/5/11.
 */

public class PickDetailActivity extends AppCompatActivity implements RatingBar.OnRatingBarChangeListener {
    private String oid;
    private TextView desc;
    private TextView loca;
    private RatingBar rating;
    private RatingBar myRating;
    private TextView price;
    private TextView category;
    private TextView label;

    Toolbar toolbar;
    private float ratingNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_detail);

        desc = (TextView) findViewById(R.id.pick_detail_description);
        loca = (TextView) findViewById(R.id.pick_detail_location);
        rating = (RatingBar) findViewById(R.id.pick_detail_rating);
        myRating = (RatingBar) findViewById(R.id.pick_detail_my_rating);
        price = (TextView) findViewById(R.id.pick_detail_price);
        category = (TextView) findViewById(R.id.pick_detail_category);
        label = (TextView) findViewById(R.id.pick_detail_label);

        Intent intent = getIntent();
        oid = intent.getStringExtra("oid");
        Log.d("--------oid------", oid);

        BmobQuery<PickParkBean> query = new BmobQuery<>();
        query.getObject(oid, new QueryListener<PickParkBean>() {
            @Override
            public void done(PickParkBean pickParkBean, BmobException e) {
                if (e == null) {
                    initView(pickParkBean);
                } else {
                    Toast.makeText(PickDetailActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    Log.d("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

        toolbar = (Toolbar) findViewById(R.id.pick_detail_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arraw_back_white);

        myRating.setOnRatingBarChangeListener(this);
    }

    private void initView(PickParkBean pickParkBean) {
        toolbar.setTitle(pickParkBean.getTitle());
        desc.setText(pickParkBean.getDescription());
        loca.setText(pickParkBean.getLocation());
        rating.setRating(pickParkBean.getRating());
        price.setText(String.format("%s", pickParkBean.getPrice()));
        category.setText(pickParkBean.getCategory());
        label.setText(pickParkBean.getLabel());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.upload_pick_toolbar_submit:
                //为哪些采摘园评过分，存起来用于个性化推荐
                SharedPreferences.Editor editor = this.getSharedPreferences("RATING_RECORD", MODE_PRIVATE).edit();
                editor.putFloat(oid, ratingNum);
                editor.apply();

                //上传评分参数的Map
                Map<String, String> ratingMap = new HashMap<>();
                ratingMap.put("oid", oid);
                ratingMap.put("score", String.valueOf(ratingNum));
                AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
                ace.callEndpoint("rate", new JSONObject(ratingMap), new CloudCodeListener() {
                    @Override
                    public void done(Object o, BmobException e) {
                        if (e == null) {
                            Toast.makeText(PickDetailActivity.this, "评分已更新", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(PickDetailActivity.this, "更新评分失败", Toast.LENGTH_SHORT).show();
                            Log.e("YunMethodRate", e.getMessage());
                        }
                    }
                });
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upload_pick_park_toolbar_menu, menu);
        return true;
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
        ratingNum = v;
    }
}
