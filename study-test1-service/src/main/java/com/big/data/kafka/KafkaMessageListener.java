package com.big.data.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.MessageListener;

public class KafkaMessageListener implements MessageListener<String, Object>{
	
	private static final Logger logger = LoggerFactory.getLogger(KafkaMessageListener.class);
	
	public void onMessage(ConsumerRecord<String, Object> data) {
		logger.info("---------------Consumer---top-------------"+data.topic());
		logger.info("---------------Consumer---Message---------"+data.value());
		if(data.value() instanceof  Msg){
			Msg m = (Msg) data.value();
			logger.info("--------自定主Msg类型-------"+m.getMessage());
		}

		if(data.value() instanceof  String){
			String m = (String) data.value();
			logger.info("--------String类型-------"+m);
		}

	}

}
