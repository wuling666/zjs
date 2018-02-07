package com.yks.oms.service;

import com.yks.oms.entity.User;
import com.yks.oms.pojo.BaseDataVO;

/**
 * @author sunshen
 * @date 2018-01-31
 */
public interface UserService {


    BaseDataVO login(User user);
}
