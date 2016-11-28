//package com.im.manage.push;
//
//import com.im.manage.session.ContextHolder;
//import com.im.sdk.protocol.Message;
//import com.im.sdk.protocol.Message.Data.Cmd;
//import com.opensymphony.xwork2.ActionSupport;
//import com.opensymphony.xwork2.ModelDriven;
//import org.apache.struts2.ServletActionContext;
//
///**
// * 后台推送
// * @author xie
// *
// */
//public class MessageAction extends ActionSupport implements ModelDriven<PushMessage> {
//
//	private static final long serialVersionUID = 1L;
//	PushMessage message = new PushMessage();
//
//	@Override
//	public PushMessage getModel() {
//		return message;
//	}
//
//	public String send() throws Exception {
//
//		ServletActionContext.getResponse().setContentType("text/json;charset=UTF-8");
//		// 向客户端 发送消息
//		Message.Data.Builder msg = Message.Data.newBuilder();
//		msg.setCmd(Cmd.CHAT_TXT_VALUE);
//		msg.setCreateTime(System.currentTimeMillis());
//		msg.setReceiverId(message.getReceiver());
//		msg.setContent(message.getContent());
//		((IMessagePusher) ContextHolder.getBean("messagePusher")).pushMessage(msg);
//		return null;
//	}
//
//}
