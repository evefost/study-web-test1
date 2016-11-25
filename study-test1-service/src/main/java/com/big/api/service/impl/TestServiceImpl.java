package com.big.api.service.impl;

import com.big.api.service.TestService;
import org.springframework.stereotype.Service;

/**
 * Created by chargerlink on 2016/11/25.
 */
@Service("testService")
public class TestServiceImpl implements TestService {

    public String getVersion() {
        return "1.0";
    }
}
