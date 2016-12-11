package com.big.data.dao;

import com.big.data.entity.TestBean;

/**
 * Created by xieyang on 16/11/27.
 */
public interface TestMapper {

    TestBean getNameById(Integer id);
}
