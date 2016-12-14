package com.big.data.service.impl;

import com.big.data.constant.AppConfig;
import com.big.data.dao.TestMapper;
import com.big.data.entity.TestBean;
import com.big.data.service.MessageService;
import com.big.data.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chargerlink on 2016/11/25.
 */
@Service("testService")
public class TestServiceImpl implements TestService {
    private static final Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);

    @Autowired
    private TestMapper testDao;
    @Autowired
    private AppConfig appConfig;

    @Autowired
    private MessageService messageService;

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
        TestBean t =  testDao.getNameById(1);
        String env = appConfig.getPackageEnviroment() + "==" + t.getName();
        return env;
    }
}
