package com.glh.productdetailsui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class ProductDetailActivity extends AppCompatActivity implements MyCustomScrollView
        .ScrollViewListener, View.OnClickListener {

    Context c;

    TextView tvShare, tvCollect, tvShop, tvSettlement;      //底部的四个按钮
    TextView tvGoods, tvComment, tvDetails;             //标题的三个控件.代码逻辑的关键
    View View1, View2, View3;                           //标题的三个控件下面对应的横线.代码逻辑的关键
    MyCustomScrollView scrollView;
    ImageButton        ivBack;
    RelativeLayout     layout;                            //标题栏
    LinearLayout       goods;
    LinearLayout       comment;

    private int commentHeight;                            //评论版块控件的高度
    private int commentHeightQD;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initView();
        initNavigation();
        initTitle();
        initListeners();
    }


    /**
     * 底部控件
     */
    private void initNavigation() {

        tvShare = (TextView) findViewById(R.id.tv_share);
        tvCollect = (TextView) findViewById(R.id.tv_collect);
        tvShop = (TextView) findViewById(R.id.tv_shop);
        tvSettlement = (TextView) findViewById(R.id.tv_settlement);

        tvShare.setOnClickListener(this);
        tvCollect.setOnClickListener(this);
        tvShop.setOnClickListener(this);
        tvSettlement.setOnClickListener(this);

    }

    private Handler handler1 = new Handler();
    private Handler handler2 = new Handler();
    private Handler handler3 = new Handler();

    /**
     * 滑动到顶部
     */
    private void scrollToPosition() {
        handler1.post(new Runnable() {

            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    private void initTitle() {

        tvGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToPosition();
            }
        });


        tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handler2.post(new Runnable() {

                    @Override
                    public void run() {
                        scrollView.smoothScrollTo(0, commentHeightQD);
                    }
                });
            }
        });


        tvDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handler3.post(new Runnable() {

                    @Override
                    public void run() {
                        scrollView.smoothScrollTo(0, commentHeightQD + commentHeight);
                    }
                });
            }
        });
    }


    private void initView() {

        tvGoods = (TextView) findViewById(R.id.goods);
        tvComment = (TextView) findViewById(R.id.commment);
        tvDetails = (TextView) findViewById(R.id.detail);

        View1 = findViewById(R.id.view1);
        View2 = findViewById(R.id.view2);
        View3 = findViewById(R.id.view3);

        scrollView = (MyCustomScrollView) findViewById(R.id.scrollview);
        ivBack = (ImageButton) findViewById(R.id.back);

        layout = (RelativeLayout) findViewById(R.id.title_label);

        goods = ((LinearLayout) findViewById(R.id.ll_goods));
        comment = (LinearLayout) findViewById(R.id.ll_comment);

        ivBack.setOnClickListener(this);


    }


    @Override
    public void onScrollChanged(MyCustomScrollView scrollView, int x, int y, int oldx, int oldy) {
        // TODO Auto-generated method stub
        //        int commentQidian = commentHight - ScreenUtils.getStatusHeight(c) - ScreenUtils
        // .dip2px(c,
        //                10);
        Log.d("yyyyyy", "y" + y + ",commentHeightQD" +
                commentHeightQD + ",commentHeight" + commentHeight);

        try {
            if (y <= 0) {   //设置标题的背景颜色
                layout.setBackgroundColor(Color.argb(0, 250, 250, 250));
                tvGoods.setTextColor(Color.argb(0, 0, 0, 0));
                tvComment.setTextColor(Color.argb(0, 0, 0, 0));
                tvDetails.setTextColor(Color.argb(0, 0, 0, 0));
                View1.setBackgroundColor(Color.argb(0, 0, 0, 0));
                ivBack.setVisibility(View.GONE);
            } else if (y > 0 && y <= 400) {         //滑动距离小于商品图的高度时，设置背景和字体颜色颜色透明度渐变
                float scale = (float) y / 400;
                float alpha = (255 * scale);
                clearAndShowThis(View1);
                layout.setBackgroundColor(Color.argb((int) alpha, 250, 250, 250));
                View1.setBackgroundColor(Color.argb((int) alpha, 0, 0, 0));
                tvGoods.setTextColor(Color.argb((int) alpha, 0, 0, 0));
                tvComment.setTextColor(Color.argb((int) alpha, 0, 0, 0));
                tvDetails.setTextColor(Color.argb((int) alpha, 0, 0, 0));
                ivBack.setVisibility(View.VISIBLE);
            } else if (y > 400 && y < commentHeightQD) {      //当滑动超过商品图而小于评论
                setTitleColor();
                clearAndShowThis(View1);
                //当滑动超过评论起始位置而小于评论结束位置
            } else if (y >= commentHeightQD && y < (commentHeightQD + commentHeight)) {
                setTitleColor();
                clearAndShowThis(View2);
                //当滑动超过评论结束位置（默认为详情）
            } else if (y >= (commentHeightQD + commentHeight)) {
                setTitleColor();
                clearAndShowThis(View3);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置标题的颜色为黑色，背景为白色可见
     */
    private void setTitleColor() {
        layout.setBackgroundColor(Color.argb(255, 250, 250, 250));
        tvGoods.setTextColor(Color.argb(255, 0, 0, 0));
        tvComment.setTextColor(Color.argb(255, 0, 0, 0));
        tvDetails.setTextColor(Color.argb(255, 0, 0, 0));
    }

    private void clearAndShowThis(View currentView) {
        View1.setVisibility(View.INVISIBLE);
        View2.setVisibility(View.INVISIBLE);
        View3.setVisibility(View.INVISIBLE);
        View1.setBackgroundColor(Color.argb(255, 0, 0, 0));
        View2.setBackgroundColor(Color.argb(255, 0, 0, 0));
        View3.setBackgroundColor(Color.argb(255, 0, 0, 0));
        currentView.setVisibility(View.VISIBLE);
    }


    /**
     * 获取顶部图片高度后，设置滚动监听
     */
    private void initListeners() {

        ViewTreeObserver mGoods = goods.getViewTreeObserver();
        ViewTreeObserver mComment = comment.getViewTreeObserver();

        mComment.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                comment.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                commentHeight = comment.getHeight();
            }
        });


        mGoods.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                goods.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                commentHeightQD = goods.getHeight() - ScreenUtils.getStatusHeight(c) - ScreenUtils
                        .dip2px(ProductDetailActivity.this, 40);

                scrollView.setScrollViewListener(ProductDetailActivity.this);
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_share:
                Toast.makeText(this, "分享成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_collect:
                Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_shop:
                Toast.makeText(this, "进入购物车", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_settlement:
                Toast.makeText(this, "进入计算", Toast.LENGTH_SHORT).show();
                break;
            case R.id.back:
                Toast.makeText(this, "返回", Toast.LENGTH_SHORT).show();
        }
    }


}

