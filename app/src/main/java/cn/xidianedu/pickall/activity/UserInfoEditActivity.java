package cn.xidianedu.pickall.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import cn.xidianedu.pickall.R;
import cn.xidianedu.pickall.http.AsyncHttpClient;
import cn.xidianedu.pickall.http.TextResponseHandler;

/**
 * 修改头像未实现
 * 第116行更新资料需更新服务端数据，缺乏服务端支持，待完善
 * Created by ShiningForever on 2016/8/19.
 */
public class UserInfoEditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    public static final int MALE = 0;
    public static final int FEMALE = 1;

    String[] sexItems = {" 男 "," 女 "};
    String id = "";
    String name = "";
    int sex = MALE;
    String signature = "";

    SharedPreferences pref;
    EditText etName;
    EditText etSignature;
    Spinner sSex;
    AsyncHttpClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_edit);
        initData();
        initViews();
    }

    private void initData() {
        pref = getSharedPreferences("user_info", MODE_PRIVATE);
        id = pref.getString("id", "error");
        name = pref.getString("name", "error");
        sex = Integer.parseInt(pref.getString("sex", "0"));
        signature = pref.getString("signature", "error");

        client = new AsyncHttpClient();
//        Log.d("initData", name);
//        Log.d("initData", sex + "");
//        Log.d("initData", signature);
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.user_info_edit_toolbar);
        toolbar.setTitle("编辑个人信息");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arraw_back_white);

        sSex = (Spinner) findViewById(R.id.user_info_edit_sex);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sexItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sSex.setAdapter(adapter);
        sSex.setOnItemSelectedListener(this);

        etName = (EditText) findViewById(R.id.user_info_edit_name);
        etSignature = (EditText) findViewById(R.id.user_info_edit_signature);
        etName.setText(name);
        etSignature.setText(signature);
        sSex.setSelection(sex, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_info_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_check:
                // 保存设置
                name = etName.getText().toString();
                signature = etSignature.getText().toString();

                SharedPreferences.Editor editor = pref.edit();
                editor.putString("name", name);
                editor.putString("sex", Integer.toString(sex));
                editor.putString("signature", signature);
                editor.apply();

                Map<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("sex", Integer.toString(sex));
                map.put("signature", signature);
                client.post("http://10.0.2.2/user_" + id + ".json", map, new TextResponseHandler() {
                    @Override
                    public void onSuccess(String response) {

                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                });
                Toast.makeText(this, "设置已保存", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
//        Toast.makeText(this, sexItems[pos], Toast.LENGTH_SHORT).show();
        sex = pos;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
