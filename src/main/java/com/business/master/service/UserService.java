package com.business.master.service;  
  
import java.util.List;  
  
import com.business.master.model.UserInfo;  
  
/**   
 * 创建时间：2017-12-27 下午5:15:03   
 * @author hjj   
 */  
  
public interface UserService {  
  
      
    List<UserInfo> getUsers();  
      
    int insert(UserInfo userInfo);

	UserInfo getUserById(int id);  
}