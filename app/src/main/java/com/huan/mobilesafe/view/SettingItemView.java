package com.huan.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huan.mobilesafe.R;

/**
 * SettingItemView 设置中心自定义组合控件
 *
 * @author: 欢
 * @time: 2016/2/4 20:51
 */
public class SettingItemView extends RelativeLayout {

    private static final String TAG = "SettingItemViewInfo";

    //指定自定义属性的命名空间
    private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.huan.mobilesafe";

    private TextView tvTitle;
    private TextView tvDescription;
    private CheckBox cbStatus;

    private String mTitle;
    private String mDescriptionOn;
    private String mDescriptionOff;

    public SettingItemView(Context context) {
        super(context);
        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //获取自定义属性的值
        mTitle = attrs.getAttributeValue(NAMESPACE, "title_item");
        mDescriptionOn = attrs.getAttributeValue(NAMESPACE, "description_on");
        mDescriptionOff = attrs.getAttributeValue(NAMESPACE, "description_off");

        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        //将自定义好的布局文件设置给当前的SettingItemView
        View view = View.inflate(getContext(), R.layout.view_setting_item, this);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        cbStatus = (CheckBox) findViewById(R.id.cb_status);

        //将自定义组合控件的属性值赋给基本控件 (标题是固定的)
        setTvTitle(mTitle);
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
     * 设置复选框状态及描述内容
     *
     * @param check
     */
    public void setChecked(Boolean check) {
        //初始化复选框状态
        cbStatus.setChecked(check);

        //根据复选框状态更新描述内容
        if (check) {
            setTvDescription(mDescriptionOn);
        } else {
            setTvDescription(mDescriptionOff);
        }
    }

    /**
     * 获取复选框
     *
     * @return this.cbStatus
     */
    public CheckBox getCbStatus() {
        return this.cbStatus;
    }
}
