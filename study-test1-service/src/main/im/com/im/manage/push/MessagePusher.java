package com.im.manage.push;

import com.im.manage.session.SessionManager;
import com.im.protocol.Message;
import com.im.server.core.IMSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 推送消息接口实现
 *
 * @author xie
 */
public class MessagePusher implements IMessagePusher {

    private final Log log = LogFactory.getLog(getClass());
    private SessionManager sessionManager;

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void pushMessage(Message.Data.Builder msg) {
        IMSession session = sessionManager.getSession(msg.getReceiverId());

		/*服务器集群时，可以在此 判断当前session是否连接于本台服务器，如果是，继续往下走，如果不是，将此消息发往当前session连接的服务器并 return
		if(!session.isLocalhost()){//判断当前session是否连接于本台服务器，如不是
			
			MessageDispatcher.execute(msg, session.getHost());
			return;
		}
		*/

        //if (session != null && session.isConnected()) {
			
			
			/*//如果用户标示了DeviceToken 且 需要后台推送（Pushable=1） 说明这是ios设备需要使用anps发送
			
			if(StringUtil.isNotEmpty(session.getDeviceToken())&&session.getPushable()==User.PUSHABLE)
			{
				try {
					deliverByANPS(msg,session.getDeviceToken());
					msg.setStatus(Message.STATUS_SEND);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					msg.setStatus(Message.STATUS_NOT_SEND);
				}
			}else
			{
				
				//推送消息
				session.deliver(MessageUtil.transform(msg));
			}*/

        //推送消息
        session.write(msg);

        //}else{
			
			/*User target = ((UserService)ContextHolder.getBean("userServiceImpl")).getUserByAccount(msg.getReceiver());
			//如果用户标示了DeviceToken 且 需要后台推送（Pushable=1） 说明这是ios设备需要使用anps发送
			if(StringUtil.isNotEmpty(target.getDeviceToken())&&target.getPushable()==User.PUSHABLE)
			{
				try {
					deliverByANPS(msg,target.getDeviceToken());
					msg.setStatus(Message.STATUS_SEND);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					msg.setStatus(Message.STATUS_NOT_SEND);
				}
				
			} */
        //未发送
        //}
        try {
            //可以在这保存消息到数据库
            //((MessageService)ContextHolder.getBean("messageServiceImpl")).save(msg);
        } catch (Exception e) {
            log.warn(" Messages insert to database failure!!");
        }
    }


}
