package cn.xidianedu.pickall.behavior;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ShiningForever on 2016/8/30.
 */
public class ScrollAwareFabBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {
    public ScrollAwareFabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        float y = Math.abs(dependency.getY());
//        Log.d("ScrollAwareFabBehavior", "" + y);
        child.setTranslationY((float) (1.5 * y));
        return true;
    }
}
