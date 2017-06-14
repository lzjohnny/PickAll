package cn.xidianedu.pickall.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Created by ShiningForever on 2017/2/1.
 * 所有Activity的基类，没什么用也先留着，以后扩展用
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        x.view().inject(this);
    }
}
