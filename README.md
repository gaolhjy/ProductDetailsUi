# 1.效果图
 
 ![这里写图片描述](http://img.blog.csdn.net/20170701214412871?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvZ2FvbGg4OQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
 
# 2.实现思路

## (1)总体思路:

   滑动时,标题栏颜色发生渐变.标题栏点击后,定位到具体的位置.整个界面可以分为标题栏、界面2部分。而界面分为图片、评论、商户详情3部分.
  
## (2)如何实现标题栏的渐变:
   先在布局中写好，根据滑动位置的情况，将标题栏的颜色（包括字体颜色）由最浅的颜色，逐渐变成最终稳定的颜色（比如我的是黑色）

## (3)如何实现点击标题后定位到具体位置：
   说白一点，就是获取图片、评论、商户详情这三个控件的高度。点击事件后，滑动相应高度即可    


# 3.实现细节

## (1)布局     
3.1.1公司使用的自定义ScrollView.
3.1.2.图片、评论、商户详情三部分控件的高度需要,布局中需要体现.具体代码见源码
## (2)逻辑实现
3.2.1.自定义的ScrollView.
    监听新旧横向、纵向的位置,通过接口回调的方式传递给activity进行相应操作.代码如下:

```
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
```
3.2.2.标题栏的渐变
     主要是相关api的调用,一个是.setBackgroundColor 和.setTextColor,另外一个是Color.argb
     由于在图片控件区域标题栏渐变效果,所以需要进行相关计算:
 

```
 float scale = (float) y / 400;     //y是控件纵向高度.400为图片的高度
 float alpha = (255 * scale);
```
由于不能整除且尽量精准,我们使用float类型.颜色的GRB值范围是0-255.这个没啥好解释的

```
layout.setBackgroundColor(Color.argb((int) alpha, 250, 250, 250));
View1.setBackgroundColor(Color.argb((int) alpha, 0, 0, 0));
tvGoods.setTextColor(Color.argb((int) alpha, 0, 0, 0));
tvComment.setTextColor(Color.argb((int) alpha, 0, 0, 0));                
tvDetails.setTextColor(Color.argb((int) alpha, 0, 0, 0));
```

3.2.3.控件的测量:
对于控件的测试,网上有比较多的资料,也比较杂,有的甚至是坑人的.下面链接这篇文章[(相关链接.直接点击即可)](http://blog.csdn.net/johnny901114/article/details/7839512),无论是排版还是内容讲解,都很不错.对控件宽高测试不是很清楚的,可以看看.
在真实的项目中,一般第三种使用最多.代码如下:
` ViewTreeObserver mGoods = goods.getViewTreeObserver();
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
        });`

其中涉及一个小的细节知识点:dp与px.代码中的数值,一般都是dp,而我们需要的又是px,所以需要转换.

```
/**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
```
dp与px之间的转换网上的工具类比较多,大家在平时多积累对提高开发效率有好处

3.2.4.点击标题后定位到相应位置:
获取控件高度后,结合自定义scrollView的滚动接口回调,就可以实现.以点击"评论"为例:

```
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
```

3.2.5.其他细节:
在UI界面滚动过程中,主要分为5部分.
一是进入界面时最初始的位置: 
二是滑动距离小于商品图的高度时
三是当滑动超过商品图而小于评论
四是当滑动超过评论起始位置而小于评论结束位置
五是当滑动超过评论结束位置
其中,第一部分默认看不见.
第二部分是标题的渐变过程,逻辑见2.标题栏的渐变
第三、四、五逻辑是一模一样的,主要是哪个控件显示,哪个控件掩藏

```
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
```


# 4.其他

 (1).[我的博客](http://blog.csdn.net/gaolh89/article/details/74080088)
 (2).如果您觉得这篇博客对您有帮助,star就是对我最大的鼓励!
