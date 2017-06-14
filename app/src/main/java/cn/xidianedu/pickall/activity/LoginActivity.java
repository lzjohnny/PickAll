package cn.xidianedu.pickall.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.xidianedu.pickall.R;
import cn.xidianedu.pickall.bean.User;

/**
 * Created by ShiningForever on 2016/8/14.
 * 登录页面的代码，大部分代码都在处理无聊的逻辑：比如记住账号密码，账号密码为空什么的
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText et_Username;
    EditText et_Password;
    CheckBox cb_RemUsername;
    CheckBox cb_RemPassword;
    Button bt_login;
    Button bt_register;
    TextInputLayout usernameWrapper;
    TextInputLayout passwordWrapper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_Username = (EditText) findViewById(R.id.login_username);
        et_Password = (EditText) findViewById(R.id.login_password);
        cb_RemUsername = (CheckBox) findViewById(R.id.login_rem_user);
        cb_RemPassword = (CheckBox) findViewById(R.id.login_rem_pass);
        bt_login = (Button) findViewById(R.id.login_btn);
        bt_register = (Button) findViewById(R.id.register_btn);
        usernameWrapper = (TextInputLayout) findViewById(R.id.username_wrapper);
        passwordWrapper = (TextInputLayout) findViewById(R.id.password_wrapper);

        Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        toolbar.setTitle("登录");
        toolbar.setTitleTextColor(0xFFFFFFFF);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arraw_back_white);

        SharedPreferences pref = getSharedPreferences("login_info", MODE_PRIVATE);
//        boolean isRemUser = pref.getBoolean("rem_id", false);
//        boolean isRemPass = pref.getBoolean("rem_pass", false);
        boolean isRemUser = pref.getBoolean("rem_username", false);
        boolean isRemPass = pref.getBoolean("rem_password", false);

        if (isRemPass) {
//            et_Username.setText(pref.getString("id", ""));
//            et_Password.setText(pref.getString("pass", ""));
            et_Username.setText(pref.getString("username", ""));
            et_Password.setText(pref.getString("password", ""));

            cb_RemUsername.setChecked(true);
            cb_RemPassword.setChecked(true);
        } else if (isRemUser) {
//            et_Username.setText(pref.getString("id", ""));
            et_Username.setText(pref.getString("username", ""));
            cb_RemUsername.setChecked(true);
        }

        bt_login.setOnClickListener(this);
        bt_register.setOnClickListener(this);
        cb_RemPassword.setOnClickListener(this);
        cb_RemUsername.setOnClickListener(this);

        et_Username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                usernameWrapper.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        et_Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordWrapper.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            // 记住密码必须记住用户名
            case R.id.login_rem_user:
                if (!cb_RemUsername.isChecked())
                    cb_RemPassword.setChecked(false);
                break;
            case R.id.login_rem_pass:
                if (cb_RemPassword.isChecked())
                    cb_RemUsername.setChecked(true);
                break;
            case R.id.login_btn:
                String loginUsername = et_Username.getText().toString();
                String loginPassword = et_Password.getText().toString();
                if (TextUtils.isEmpty(loginUsername))
                    usernameWrapper.setError("用户名为空");
                else if (TextUtils.isEmpty(loginPassword))
                    passwordWrapper.setError("密码为空");
                else {
                    login(loginUsername, loginPassword);
                }
                break;
            case R.id.register_btn:
                String registerUsername = et_Username.getText().toString();
                String registerPassword = et_Password.getText().toString();
                if (TextUtils.isEmpty(registerUsername))
                    usernameWrapper.setError("用户名为空");
                else if (TextUtils.isEmpty(registerPassword))
                    passwordWrapper.setError("密码为空");
                else {
                    register(registerUsername, registerPassword);
                }
                break;
        }
    }

    private void login(String username, final String password) {
        //登录发送POST请求
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("username", username);
        query.setLimit(1);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        Toast.makeText(LoginActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
                    } else {
                        if (list.get(0).getPassword().equals(password)) {
                            //用户名密码匹配登陆成功
                            SharedPreferences.Editor editor = getSharedPreferences("login_info", MODE_PRIVATE).edit();
                            editor.putBoolean("is_login", true);
                            editor.apply();
                            Intent intent = new Intent(LoginActivity.this, UserInfoActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "用户名与密码不匹配", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "服务繁忙，请稍候再试", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void register(final String registerUsername, final String registerPassword) {
        //检查用户名是否存在
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("username", registerUsername);
        query.setLimit(1);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    //用户名重复
                    if (list.size() != 0) {
                        Toast.makeText(LoginActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                    } else {
                        doRegister(registerUsername, registerPassword);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "服务繁忙，请稍候再试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void doRegister(String registerUsername, String registerPassword) {
        User user = new User();
        user.setUsername(registerUsername);
        user.setPassword(registerPassword);
        user.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = getSharedPreferences("login_info", MODE_PRIVATE).edit();
                    editor.putBoolean("is_login", true);
                    editor.apply();
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "服务繁忙，请稍候再试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //无论点击登录与否都在onStop中保存username和password
    @Override
    protected void onStop() {
        boolean isRemUser = cb_RemUsername.isChecked();
        boolean isRemPass = cb_RemPassword.isChecked();
        SharedPreferences.Editor editor = getSharedPreferences("login_info", MODE_PRIVATE).edit();
        editor.putBoolean("rem_username", isRemUser);
        editor.putBoolean("rem_password", isRemPass);
        if (isRemPass) {
            editor.putString("username", et_Username.getText().toString());
            editor.putString("password", et_Password.getText().toString());
        } else if (isRemUser) {
            editor.putString("username", et_Username.getText().toString());
        }
        editor.apply();
        super.onStop();
    }
}
