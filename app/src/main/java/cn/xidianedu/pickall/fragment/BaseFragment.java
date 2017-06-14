package cn.xidianedu.pickall.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ShiningForever on 2017/2/1.
 * 已废弃，所有Fragment类的基类，供xUtils框架使用，已不再使用xUtils框架
 * 先留着以后也许会有用吧
 */

public class BaseFragment extends Fragment {
//    private boolean injected = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        injected = true;
//        return x.view().inject(this, inflater, container);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        if (!injected) {
//            x.view().inject(this, this.getView());
//        }
    }
}
