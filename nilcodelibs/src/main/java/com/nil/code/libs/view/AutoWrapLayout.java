package com.nil.code.libs.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.nil.code.libs.R;

/**
 * by code_nil on 2017/4/21 0021.
 * 自动换行布局，当布局中包含的控件超过最大width时将会自动换行
 */

public class AutoWrapLayout extends LinearLayout {
    private int singleLeft = 0;
    private int singleTop = 0;
    private int singleRight = 0;
    private int singleBottom = 0;
    private int maxWidth = 0;
    //整个视频的高度
    private int maxHeight = 0;
    //一共有多少行
    private int heightCount = 1;
    //一共视频占据多少高度
    int containHeight = 0;

    public AutoWrapLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AutoWrapLayout);
        singleLeft = (int) ta.getDimension(R.styleable.AutoWrapLayout_singleLeft, 0);
        singleTop = (int) ta.getDimension(R.styleable.AutoWrapLayout_singleTop, 0);
        singleRight = (int) ta.getDimension(R.styleable.AutoWrapLayout_singleRight, 0);
        singleBottom = (int) ta.getDimension(R.styleable.AutoWrapLayout_singleBottom, 0);
        ta.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        maxHeight = MeasureSpec.getSize(heightMeasureSpec);
        int childWidthCount = 0;
        //测量子布局
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            childWidthCount += (singleLeft + child.getMeasuredWidth() + singleRight);

            //初始化 无论如何都会有一行
            if (containHeight == 0) {
                containHeight = singleTop + child.getMeasuredHeight() + singleBottom;
            }

            //宽度计算，越界换行
            if (childWidthCount > maxWidth) {
                containHeight += (singleTop + child.getMeasuredHeight() + singleBottom);
                //重置宽度
                childWidthCount = 0;
                heightCount++;
            }
        }
        setMeasuredDimension(maxWidth, maxHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //确定top起始位置
        int topStart = 0;
        //实现换行
        int count = getChildCount();
        int row = 1;
        int left;
        int top;
        int right = 0;
        int bottom;
        //执行居中处理
        //1判定是否越界
        if (containHeight < maxHeight) {
            topStart = maxHeight / 2 - containHeight / 2;
        }
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            int childWidth = view.getMeasuredWidth();
            int childHeight = view.getMeasuredHeight();
            left = singleLeft + right;
            right = left + childWidth + singleRight;
            top = topStart + singleTop + childHeight * (row - 1);
            bottom = top + childHeight + singleBottom;
            if (right > maxWidth) {
                row++;
                right = 0;
                left = singleLeft + right;
                right = left + childWidth + singleRight;
                top = topStart + singleTop + childHeight * (row - 1);
                bottom = top + childHeight + singleBottom;
            }
            //重新绘制布局
            view.layout(left, top, right, bottom);
        }
    }
}
