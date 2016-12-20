package com.kafka;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Germmy on 2016/7/10.
 */
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    private static Properties props = new Properties();
    private static ConsumerConnector consumer;

    public final static String TOPIC = "TEST-TOPIC";

    static {
        try {
            InputStream e = Thread.currentThread().getContextClassLoader().getResourceAsStream("properties/consumer.properties");
            if (e == null) {
                logger.error("启动加载消息消费都失败，找不到加载文件！！！！");
            } else {
                props.load(e);
                boolean on = Boolean.valueOf(props.getProperty("consumer.on-off", "true")).booleanValue();
                if (on) {
                    ConsumerConfig config = new ConsumerConfig(props);
                    long begin = System.currentTimeMillis();
                    consumer = Consumer.createJavaConsumerConnector(config);
                    logger.debug("connect zookeeper time:" + (System.currentTimeMillis() - begin) / 1000L + "s");
                }
            }
        } catch (Exception var5) {
            var5.printStackTrace();
            logger.error("启动加载消息消费者失败！！！！", var5);
        }

    }

    private KafkaConsumer() {
        Properties props = new Properties();
        props.put("zookeeper.connect", "192.168.134.129:2181");
        props.put("group.id", "jd-group");//消费组是什么概念？

        props.put("zookeeper.session.timeout.ms", "60000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "smallest");

        props.put("serializer.class", "kafka.serializer.StringEncoder");

        ConsumerConfig config = new ConsumerConfig(props);

        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(config);
    }


    void consume() {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(KafkaConsumer.TOPIC, new Integer(1));
        StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
        StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());

        Map<String, List<KafkaStream<String, String>>> consumerMap =
                consumer.createMessageStreams(topicCountMap, keyDecoder, valueDecoder);

        KafkaStream<String, String> stream = consumerMap.get(KafkaConsumer.TOPIC).get(0);
        ConsumerIterator<String, String> it = stream.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().message());
        }
    }


    public static void main(String[] args) {
        new KafkaConsumer().consume();
    }

}
