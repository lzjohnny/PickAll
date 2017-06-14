package cn.xidianedu.pickall.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.rollviewpager.adapter.StaticPagerAdapter;

import cn.xidianedu.pickall.R;

/**
 * Created by ShiningForever on 2017/2/8.
 */

public class MainFrag1RollPagerViewAdapter extends StaticPagerAdapter{
    private int[] imgs = new int[]{
            R.drawable.loop_img_1,
            R.drawable.loop_img_2,
            R.drawable.loop_img_3,
    };

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        view.setImageResource(imgs[position]);
        return view;
    }

    @Override
    public int getCount() {
        return imgs.length;
    }
}
