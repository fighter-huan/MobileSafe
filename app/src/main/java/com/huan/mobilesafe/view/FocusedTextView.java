package com.huan.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * FocusedTextView 自定义TextView强制获取焦点，实现跑马灯效果
 *
 * @author: 欢
 * @time: 2016/2/4 19:19
 */
public class FocusedTextView extends TextView {

    //有style样式调用此方法
    public FocusedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //用Java代码new对象时调用此方法
    public FocusedTextView(Context context) {
        super(context);
    }

    //有属性时调用此方法
    public FocusedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //强制获取焦点
    @Override
    public boolean isFocused() {
        return true;
    }

    //设置跑马灯的重复次数为 -1，表示无限次
    @Override
    public void setMarqueeRepeatLimit(int marqueeLimit) {
        super.setMarqueeRepeatLimit(-1);
    }
}
