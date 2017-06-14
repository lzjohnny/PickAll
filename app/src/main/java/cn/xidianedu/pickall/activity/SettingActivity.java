package cn.xidianedu.pickall.activity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.preference.TwoStatePreference;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import cn.xidianedu.pickall.R;

/**
 * Created by ShiningForever on 2016/8/18.
 */
public class SettingActivity extends AppCompatActivity {
    private Toolbar mToolbar = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initViews();
        setToolbar();
        initFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
//                Toast.makeText(this, "finish!", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.setting_toolbar);
    }

    private void setToolbar() {
        mToolbar.setTitle("设置");
        mToolbar.setNavigationIcon(R.drawable.ic_arraw_back_white);
        setSupportActionBar(mToolbar);
    }

    private void initFragment() {
        // 将SettingFragment嵌入指定的布局中
        getFragmentManager().beginTransaction().replace(R.id.setting_content, new SettingFragment()).commit();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

    // 推荐使用PreferenceFragment，方便嵌入其他activity
    public static class SettingFragment extends PreferenceFragment {
        TwoStatePreference hideSignature;
        TwoStatePreference hideGrade;

        // 设置匿名模式开关联动效果
        private OnSharedPreferenceChangeListener mListener = new OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals("pref_private_hide")) {
                    boolean hide = sharedPreferences.getBoolean("pref_private_hide", false);
                    if (hide) {
                        Toast.makeText(getActivity(), "匿名模式已开启", Toast.LENGTH_SHORT).show();
                        hideSignature.setChecked(true);
                        hideGrade.setChecked(true);
                    } else {
                        Toast.makeText(getActivity(), "匿名模式已关闭", Toast.LENGTH_SHORT).show();
                        hideSignature.setChecked(false);
                        hideGrade.setChecked(false);
                    }
                }
            }
        };

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            hideSignature = (SwitchPreference) findPreference("pref_private_hide_signature");
            hideGrade = (SwitchPreference) findPreference("pref_private_hide_grade");
        }

        @Override
        public void onResume() {
            PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(mListener);
            super.onResume();
        }

        @Override
        public void onPause() {
            PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(mListener);
            super.onPause();
        }
    }
}
