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
@RequestMapping("/api/test")
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private TestService testService;

    @RequestMapping(value = "serviceVersion", method = RequestMethod.GET)
    @ResponseBody
    public String registerBox() {

        logger.debug("cdebug");
        logger.info("cinfo");
        logger.error("cerror");
        return testService.getVersion();
    }
}
