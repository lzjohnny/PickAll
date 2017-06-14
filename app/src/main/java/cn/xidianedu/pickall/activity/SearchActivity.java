package cn.xidianedu.pickall.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import cn.xidianedu.pickall.R;

/**
 * Created by ShiningForever on 2016/8/2.
 */
public class SearchActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    public void back(View v) {
        this.finish();
    }

    public void search(View v){
        Toast.makeText(this, "搜索", Toast.LENGTH_SHORT).show();
    }
}
