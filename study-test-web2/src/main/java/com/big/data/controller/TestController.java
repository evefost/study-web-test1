package com.big.data.controller;

import com.big.data.constant.AppConfig;
import com.big.data.entity.TestBean;
import com.big.data.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/api/test")
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private TestService testService;

    @Autowired
    private AppConfig config;

    @RequestMapping(value = "serviceVersion", method = RequestMethod.GET)
    @ResponseBody
    public String getVersion() {

        logger.debug("cdebug");
        logger.info("cinfo");
        logger.error("cerror");
        return testService.getVersion();
    }

    @RequestMapping(value = "quetyEnviroment", method = RequestMethod.GET)
    @ResponseBody
    public String quetyEnviroment() {

        String enviroment = testService.getEnviroment();
        logger.info("quetyEnviroment::" + enviroment);
        return enviroment;
    }

    @RequestMapping(value = "queryPackageEnviroment", method = RequestMethod.GET)
    @ResponseBody
    public String queryCurrentEnviroment() {
        return config.getPackageEnviroment();
    }
}
