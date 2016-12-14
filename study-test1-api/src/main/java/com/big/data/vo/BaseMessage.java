package com.big.data.vo;

/**
 * Created by chargerlink on 2016/12/14.
 */
public  class BaseMessage {

    private  int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private Object msg;

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }
}
