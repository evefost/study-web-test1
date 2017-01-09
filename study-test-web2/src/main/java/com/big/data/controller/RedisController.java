package com.big.data.controller;

import com.big.data.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/redis")
public class RedisController {
    private static final Logger logger = LoggerFactory.getLogger(RedisController.class);

    @Autowired
    TestService testService;

    @RequestMapping(value = "cacheData2", method = RequestMethod.GET)
    @ResponseBody
    public String cacheData2() {
        logger.info("test redis");
        return testService.testRedis();
    }


}
