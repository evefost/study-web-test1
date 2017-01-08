package com.big.data.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/redis")
public class RedisController {
    private static final Logger logger = LoggerFactory.getLogger(RedisController.class);
    //Main
    // @Autowired
    //RedisTemplate<String, String> redisTemplate;
    //private int index;


    @RequestMapping(value = "cacheData2", method = RequestMethod.GET)
    @ResponseBody
    public String cacheData2() {
//        String s = "aaa" + UUID.randomUUID().toString();
//        index++;
//        redisTemplate.opsForList().leftPush(s, s + "---" + index);
        return null;//s + "-" + index;
    }


}
