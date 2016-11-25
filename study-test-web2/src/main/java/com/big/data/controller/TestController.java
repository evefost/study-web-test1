package com.big.data.controller;

import com.big.api.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private TestService testService;

    @RequestMapping(value = "serviceVersion", method = RequestMethod.GET)
    @ResponseBody
    public String registerBox() {
        return testService.getVersion();
    }
}
