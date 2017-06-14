package cn.xidianedu.pickall.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xidianedu.pickall.R;

/**
 * Created by ShiningForever on 2016/8/29.
 */
public class ForumFragment3 extends Fragment {

    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        context = getActivity();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = LayoutInflater.from(context).inflate(R.layout.forum_fragment_3, container, false);
        return view;
    }
}
