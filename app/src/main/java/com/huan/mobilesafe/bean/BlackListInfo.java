package com.huan.mobilesafe.bean;

/**
 * BlackListInfo
 *
 * @author: 欢
 * @time: 2016/2/14 16:51
 */
public class BlackListInfo {

    //拦截的电话号码
    private String phone;

    //拦截模式
    //1.电话拦截
    //2.短信拦截
    //3.电话+短信拦截
    private String mode;

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getPhone() {
        return phone;
    }

    public String getMode() {
        return mode;
    }
}
