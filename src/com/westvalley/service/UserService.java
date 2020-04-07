package com.westvalley.service;

import com.westvalley.dao.UserDao;
import com.westvalley.util.LogUtil;

public class UserService {

    private LogUtil log = LogUtil.getLogger(getClass());


    private UserDao userDao;

    public UserService() {
        this.userDao = new UserDao();
    }

    public String getUserNameById(int id) {
        return userDao.getUserNameById(id);
    }

}
