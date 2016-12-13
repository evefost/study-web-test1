package com.big.data.constant;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by xieyang on 16/12/11.
 */
public final class AppConfig {

    @Value("${package_enviroment}")
    private String packageEnviroment;


    public String getPackageEnviroment() {
        return packageEnviroment;
    }

}
