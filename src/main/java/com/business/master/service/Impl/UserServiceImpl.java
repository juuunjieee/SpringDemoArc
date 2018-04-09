package com.business.master.service.Impl;  
  
import java.util.List;

import com.business.master.model.UserInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.business.master.service.UserService;
import com.business.master.dao.UserInfoMapper;
  
/** 
 * 创建时间：2017-12-27 下午5:22:59 
 * @author hjj 
 */  
@Service("userService")  
public class UserServiceImpl implements UserService {  
  
    @Autowired  
    private UserInfoMapper userInfoMapper;  
  
    @Override  
    public UserInfo getUserById(int id) {  
        return userInfoMapper.selectByPrimaryKey(id);  
    }  
  
    @Override  
    public List<UserInfo> getUsers() {  
        return userInfoMapper.selectAll();  
    }  
  
    @Override  
    public int insert(UserInfo userInfo) {  
        int result = userInfoMapper.insert(userInfo);  
        System.out.println(result);  
        return result;  
    }  
  
}