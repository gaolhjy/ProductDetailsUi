package com.glh.productdetailsui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;


/**
 * Author:   高磊华
 * Email:    984992087@qq.com
 * Time:     2017/4/23
 * Describe: 带滚动监听的scrollview
 */


public class MyCustomScrollView extends ScrollView {

    public interface ScrollViewListener {

        void onScrollChanged(MyCustomScrollView scrollView, int x, int y,
                             int oldx, int oldy);

    }

    private ScrollViewListener scrollViewListener = null;

    public MyCustomScrollView(Context context) {
        super(context);
    }

    public MyCustomScrollView(Context context, AttributeSet attrs,
                              int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyCustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

}