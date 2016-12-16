package com.im.protocol;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by chargerlink on 2016/12/16.
 */
public class JsonMsg implements Serializable{

    private Integer cmd;
    private String content;
    private String id;
    private Long createTime;
    private String clientId;
    private String receitverId;
    private String senderId;

    public Integer getCmd() {
        return cmd;
    }

    public void setCmd(Integer cmd) {
        this.cmd = cmd;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getReceitverId() {
        return receitverId;
    }

    public void setReceitverId(String receitverId) {
        this.receitverId = receitverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
