package cn.xidianedu.pickall.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xidianedu.pickall.R;

/**
 * Created by ShiningForever on 2016/7/31.
 */
public class GuideFragment1 extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.guide_fragment_1, container, false);
        return view;
    }
}
