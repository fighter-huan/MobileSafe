package com.huan.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huan.mobilesafe.R;

/**
 * SettingItemView 设置中心自定义控件类
 *
 * @author: 欢
 * @time: 2016/2/4 20:51
 */
public class SettingItemView extends RelativeLayout {

    private TextView tvTitle;
    private TextView tvDescription;
    private CheckBox cbStatus;

    public SettingItemView(Context context) {
        super(context);
        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 舒适化布局
     */
    private void initView() {
        //将自定义好的布局文件设置给当前的SettingItemView
        View view = View.inflate(getContext(), R.layout.view_setting_item, this);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        cbStatus = (CheckBox) findViewById(R.id.cb_status);
    }

    /**
     * 设置每一个选项的标题
     *
     * @param title
     */
    public void setTvTitle(String title) {
        tvTitle.setText(title);
    }

    /**
     * 设置每一个选项的描述内容
     *
     * @param description
     */
    public void setTvDescription(String description) {
        tvDescription.setText(description);
    }

    /**
     * 获取复选框对象cbStatus，以便监听
     *
     * @return this.cbStatus
     */
    public CheckBox getCbStatus() {
        return this.cbStatus;
    }
}
