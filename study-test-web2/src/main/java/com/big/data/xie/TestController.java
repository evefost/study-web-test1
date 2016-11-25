package com.big.data.xie;

import com.big.api.service.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by chargerlink on 2016/11/24.
 */
@Controller
@RequestMapping("/api/test")
public class TestController {
    private TestService testService;
    @RequestMapping(value = "registerBox", method = RequestMethod.POST)
    @ResponseBody
    public String registerBox() {

        return "123344";
    }
}
