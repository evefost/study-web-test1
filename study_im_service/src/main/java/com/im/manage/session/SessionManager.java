
package com.im.manage.session;

import com.im.sdk.protocol.Message.Data;
import com.im.server.core.IMSession;

import java.util.Collection;


public interface SessionManager {


    /**
     * 添加新的session
     */
    public void addSession(IMSession session);

    /**
     * @param clientId 客户端session的 key 一般可用 用户账号来对应session
     * @return
     */
    IMSession getSession(String clientId);

    /**
     * 获取所有session
     *
     * @return
     */
    public Collection<IMSession> getSessions();

    /**
     * 删除session
     *
     * @param session
     */
    public void removeSession(IMSession session);


    /**
     * 删除session
     *
     * @param clientId
     */
    public void removeSession(String clientId);

    /**
     * session是否存在
     *
     * @param ios
     */
    public boolean containsSession(IMSession ios);

    /**
     * session获取对应的 用户 key
     *
     * @param ios
     */
    public String getAccount(IMSession ios);

    public boolean isAreadyLogin(Data data);
}