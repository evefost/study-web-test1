
package com.im.constant;

import java.nio.charset.Charset;

public interface CIMConstant {

    // 连接空闲时间
    public static final int IDLE_TIME = 60;// 秒

    public static final int PING_TIME_OUT = 30;// 心跳响应 超时为30秒

    public static final Charset ENCODE_UTF8 = Charset.forName("UTF-8");

    public static int CIM_DEFAULT_MESSAGE_ORDER = 1;

    public static final String SESSION_KEY = "account";

    public static final String HEARTBEAT_KEY = "heartbeat";

}