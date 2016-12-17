
package com.im.manage.session;

import com.im.server.core.IMSession;
import com.im.server.util.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * session管理
 *
 * @author xie
 */
public class DefaultSessionManager implements SessionManager {

    private static final AtomicInteger connectionsCounter = new AtomicInteger(0);
    /**
     * key:clientId;
     * value:IMSession
     */
    private static HashMap<String, IMSession> sessions = new HashMap<String, IMSession>();
    /**
     * 用户maping
     * key:uid;
     * value:clientId
     */
    private static HashMap<String, String> loginUsers = new HashMap<String, String>();

    public void addSession(IMSession session) {
        if (session != null) {
            sessions.put(session.getClientId(), session);
            if (!StringUtils.isEmpty(session.getUid())) {
                loginUsers.put(session.getUid(), session.getClientId());
            }
            connectionsCounter.incrementAndGet();
        }
    }

    public IMSession getSession(String clientId) {

        return sessions.get(clientId);
    }

    public Collection<IMSession> getSessions() {
        return sessions.values();
    }

    public void removeSession(IMSession session) {
        sessions.remove(session.getClientId());
        loginUsers.remove(session.getUid());
        session.close(true);
    }

    public void removeSession(String account) {
        sessions.remove(account);
    }


    public String getAccount(IMSession ios) {
        // TODO Auto-generated method stub
        return null;
    }


    public boolean containsSession(IMSession ios) {
        // TODO Auto-generated method stub
        return false;
    }

}
