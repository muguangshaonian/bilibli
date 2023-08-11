package com.mju.service;

import com.mju.dao.UserDao;
import com.mju.domain.User;
import com.mju.domain.UserInfo;
import com.mju.domain.constant.UserConstant;
import com.mju.domain.exception.ConditionException;
import com.mju.service.util.MD5Util;
import com.mju.service.util.RSAUtil;
import com.mju.service.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public void addUser(User user) {
        String phone = user.getPhone();
        if (StringUtils.isEmpty(phone)){
            throw new ConditionException("手机号不能为空");
        }
        User dbUser = getUserByPhone(phone);
        if (dbUser!=null){
            throw new ConditionException("该手机已被注册过");
        }
        Date now = new Date();
        String salt = String.valueOf(now.getTime());
        String password = user.getPassword();
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解密失败");
        }
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");

        user.setPassword(md5Password);
        user.setSalt(salt);
        user.setCreateTime(now);
        userDao.addUser(user);
        //添加用户信息表
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setCreateTime(now);
        userInfo.setBirth(UserConstant.DEFAULT_BIRTH);
        userInfo.setNick(UserConstant.DEFAULT_NICK);
        userInfo.setGender(UserConstant.GENDER_MALE);
        userDao.addUserInfo(userInfo);
    }
    public User getUserByPhone(String phone){
        return userDao.getUserByPhone(phone);
    }

    public String login(User user) throws Exception {
        String phone = user.getPhone();
        if (StringUtils.isEmpty(phone)){
            throw new ConditionException("手机号不能为空");
        }
        User dbUser = getUserByPhone(phone);
        String password = user.getPassword();
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解密失败");
        }
        String salt = dbUser.getSalt();
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");

        if (!md5Password.equals(dbUser.getPassword())){
            throw new ConditionException("密码错误");
        }
        String token = TokenUtil.generateToken(dbUser.getId());
        return token;

    }

    public User getUserInfo(Long userId) {
        User user = userDao.getUserById(userId);
        UserInfo userInfo = userDao.getUserInfoById(userId);
        user.setUserInfo(userInfo);
        return user;
    }
}
