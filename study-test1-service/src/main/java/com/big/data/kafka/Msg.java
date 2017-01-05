package com.big.data.kafka;

import java.io.Serializable;

/**
 * Created by chargerlink on 2017/1/5.
 */
public class Msg implements Serializable{

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
