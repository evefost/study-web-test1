package com.kafka;

import com.big.data.service.impl.MessageServiceImpl;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Germmy on 2016/7/10.
 */
public class KafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
    private static Properties props = new Properties();
    private  static Producer<String,Object> producer;

    public final static String TOPIC="TEST-TOPIC";
    static {
        try {
            InputStream e = Thread.currentThread().getContextClassLoader().getResourceAsStream("properties/producer.properties");
            if(e == null) {
                logger.error("启动加载消息生产者失败，找不到加载文件！！！！");
            } else {
                props.load(e);
                boolean on = Boolean.valueOf(props.getProperty("producer.on-off", "true")).booleanValue();
                if(on) {
                    ProducerConfig config = new ProducerConfig(props);
                    producer = new Producer(config);
                }
            }
        } catch (IOException var3) {
            logger.error("启动加载消息生产者失败！！！！");
        }
    }

    public static void sendTopic(String key, Object value) {
        if(null == producer) {
            logger.warn("producer can\'t be null!");
        } else {
            KeyedMessage keyedMessage = new KeyedMessage(key, value);
            producer.send(keyedMessage);
        }
    }

    private  KafkaProducer(){
//        Properties props=new Properties();
//        props.put("metadata.broker.list","192.168.134.129:9092");
//        props.put("serializer.class","kafka.serializer.StringEncoder");
//        props.put("key.serializer.class","kafka.serializer.StringEncoder");
//        props.put("request.required.acks","-1");
//        props.put("producer.type","async");
//        producer=new Producer<String, String>(new ProducerConfig(props)) ;
    }


    void produce(){
//        int messageNo=1000;
//        final int COUNT=10000;
//        while(messageNo<COUNT){
//            String key=String.valueOf(messageNo);
//            String data="hello kafka message"+key;
//            producer.send(new KeyedMessage<String,String>(TOPIC,key,data));
//            System.out.println(data);
//            messageNo++;
//        }
    }

    public  static  void main(String[] args){
        new KafkaProducer().produce();
    }

}
