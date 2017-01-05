package com.big.data.service;

public interface KafkaProducerService {

	 void sendDefaultInfo(String str);

	 void sendMesage(String topic, String str);

	 void sendMesage(String topic, Object message);
}
