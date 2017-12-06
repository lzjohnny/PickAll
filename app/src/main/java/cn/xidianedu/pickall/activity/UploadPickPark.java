package cn.xidianedu.pickall.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import cn.xidianedu.pickall.R;
import cn.xidianedu.pickall.bean.PickParkBean;

import static android.text.TextUtils.isEmpty;

/**
 * Created by ShiningForever on 2017/5/9.
 */

public class UploadPickPark extends AppCompatActivity implements AdapterView.OnItemSelectedListener, RatingBar.OnRatingBarChangeListener {
    private String[] spinnerContent = {"性价比高", "价格合理", "环境优美", "品种丰富", "服务周到", "交通方便"};

    private EditText title;
    private EditText description;
    private EditText location;
    private RatingBar rating;
    private EditText price;
    private EditText category;
    private Spinner label;

    private float ratingNum;
    private String labelStr;

    private static final String USAGE = "main_list";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pick_park);

        Toolbar toolbar = (Toolbar) findViewById(R.id.upload_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arraw_back_white);

        title = (EditText) findViewById(R.id.upload_title);
        description = (EditText) findViewById(R.id.upload_description);
        location = (EditText) findViewById(R.id.upload_location);
        rating = (RatingBar) findViewById(R.id.upload_rating);
        price = (EditText) findViewById(R.id.upload_price);
        category = (EditText) findViewById(R.id.upload_category);
        label = (Spinner) findViewById(R.id.upload_spinner);

        rating.setOnRatingBarChangeListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerContent);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        label.setAdapter(adapter);
        label.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upload_pick_park_toolbar_menu, menu);
        return true;
    }

    // toolbar按钮点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.upload_pick_toolbar_submit:
                String titleStr = title.getText().toString();
                String descriptionStr = description.getText().toString();
                String locationStr = location.getText().toString();
                String categoryStr = category.getText().toString();
                String priceStr = price.getText().toString();

                if (isEmpty(titleStr) || isEmpty(descriptionStr) || isEmpty(locationStr) || isEmpty(priceStr) || isEmpty(labelStr) || isEmpty(categoryStr) || ratingNum == 0) {
                    Toast.makeText(UploadPickPark.this, "请填写完整信息后保存", Toast.LENGTH_SHORT).show();
                    break;
                }
                PickParkBean bean = new PickParkBean();
                bean.setTitle(titleStr);
                bean.setDescription(descriptionStr);
                bean.setLocation(locationStr);
                bean.setPrice(Float.parseFloat(priceStr));
                bean.setCategory(categoryStr);
                bean.setRating(ratingNum);
                bean.setLabel(labelStr);
                bean.setUsage(USAGE);
                bean.setRatingCount(1);
//                bean.save(new SaveListener<String>() {
//                    @Override
//                    public void done(String s, BmobException e) {
//                        if (e == null) {
//                            Toast.makeText(UploadPickPark.this, "提交成功", Toast.LENGTH_SHORT).show();
//                            finish();
//                        } else {
//                            Toast.makeText(UploadPickPark.this, "提交失败，请重试", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
                break;
        }
        return true;
    }

    //评分监听
    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
        ratingNum = v;
    }

    //下拉菜单监听
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        labelStr = spinnerContent[i];
    }

    //下拉菜单监听
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
