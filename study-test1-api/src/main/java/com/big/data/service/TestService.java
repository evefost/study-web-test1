package com.big.data.service;

import com.big.data.entity.TestBean;

public interface TestService {

    String getVersion();

    TestBean getNameById(Integer id);

    String getEnviroment();

    String testRedis();
}
