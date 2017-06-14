package cn.xidianedu.pickall.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import cn.xidianedu.pickall.R;

/**
 * Created by ShiningForever on 2016/8/20.
 */
public class ImageFragment extends Fragment implements View.OnClickListener {
    ImageView imageView;
    ImageButton imageButton;
    RelativeLayout relativeLayout;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_head_image, container, false);
        imageView = (ImageView) view.findViewById(R.id.head_img_iv);
        imageButton = (ImageButton) view.findViewById(R.id.head_img_download_btn);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.head_img_bg);
        imageButton.setOnClickListener(this);
        relativeLayout.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.head_img_download_btn:
                Toast.makeText(context, "下载图片!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.head_img_bg:
                // 点击其他位置取消头像图片显示
                getFragmentManager().popBackStack();
                break;
        }
    }
}
