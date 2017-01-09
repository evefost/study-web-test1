package com.big.data.service.impl;

import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.big.data.constant.AppConfig;
import com.big.data.dao.TestMapper;
import com.big.data.entity.TestBean;
import com.big.data.service.MessageService;
import com.big.data.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by chargerlink on 2016/11/25.
 */
@Service("testService")
public class TestServiceImpl extends SuperServiceImpl<TestMapper, TestBean> implements TestService {
    private static final Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);
    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Autowired
    private TestMapper testDao;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private MessageService messageService;
    private int index;

    public String getVersion() {
        logger.debug("sdebug");
        logger.info("sinfo");
        logger.error("serror");
        return "1.0";
    }


    public TestBean getNameById(Integer id) {
        return testDao.getNameById(id);

    }

    public String getEnviroment() {
        logger.info("getEnviroment");
        TestBean t = testDao.getNameById(1);
        //TestBean t = selectById(1l);
        String env = appConfig.getPackageEnviroment() + "==" + t.getName();
        return env;
    }

    public String testRedis() {
        logger.debug("debug testRedis");
        logger.error("errot testRedis");
        String s = "redis-test" + UUID.randomUUID().toString();
        index++;
        redisTemplate.opsForList().leftPush(s, s + "---" + index);
        return s + "-" + index;
    }
}
