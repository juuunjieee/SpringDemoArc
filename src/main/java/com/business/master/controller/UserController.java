package com.business.master.controller;  
  
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import redis.clients.jedis.Jedis;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.business.master.annotation.Log;
import com.business.master.model.TokenModel;
import com.business.master.model.UserInfo;
import com.business.master.service.UserService;
import com.business.utils.JedisUtil;
import com.business.utils.StringUtil;
import com.business.utils.TokenUtil;
import com.business.utils.PropUtils;
import com.business.utils.ResponseData;
import com.business.utils.WapInterface;
  
/**   
 * 创建时间：2017-12-28 下午1:17:27   
 * @author hjj   
 */  
@Controller  
@RequestMapping("/user")  
public class UserController {  
	private static final Logger logger = LogManager.getLogger(UserController.class);	
    @Autowired  
    private UserService userService;  
      
   /* @RequestMapping("/showInfo/{userId}")  
    public ModelAndView showUserInfo(@PathVariable int userId){  
    	ModelAndView mv = new ModelAndView();
        UserInfo userInfo = userService.getUserById(userId);
        mv.addObject("userInfo", userInfo);
        mv.addObject("attr", "123");
        mv.setViewName("/user/showInfo");
        return mv;
    } */ 
    
    @RequestMapping("/testaop")
    @Log(operationType="add操作:",operationName="添加用户") 
    public void testaop(HttpServletRequest request){
    	UserInfo userInfo = new UserInfo();
    	String userName = request.getParameter("userName");
    	String password = request.getParameter("password");
        int unumber = Integer.valueOf(request.getParameter("unumber"));
        userInfo.setId(6);
        userInfo.setUsername(userName);
        userInfo.setUnumber(unumber);
        userInfo.setPassword(password);
        userService.insert(userInfo);
    }
    
    @RequestMapping("/showInfo")  
    public ModelAndView showUserInfo(HttpServletRequest request){ 
    	/*String ip = AddrUtils.getIpAddress(request);
    	System.out.println("ip："+ip);
    	Jedis jedis = new Jedis("119.29.106.136", 6379);
    	Map<String,String> m = new HashMap<String,String>();
    	jedis.hset("uu", "name", "as1");
    	jedis.hset("uu", "name1", "as2");
    	jedis.expire("uu", 60);*/
    	ModelAndView mv = new ModelAndView();
    	/*int userId = Integer.valueOf(request.getParameter("userId"));
        UserInfo userInfo = userService.getUserById(userId);
        mv.addObject("userInfo", userInfo);
        mv.addObject("attr", "123");*/
        mv.setViewName("/jsp/login/login.jsp");
        return mv;
    } 
      
    @RequestMapping("/showInfos") 
    @ResponseBody
    public Object showUserInfos(HttpServletRequest request){  
        List<UserInfo> userInfos = userService.getUsers();  
        String s = (String) request.getSession().getAttribute("springtest");
        return userInfos;  
    }  
    
    @RequestMapping("/login")
    @ResponseBody  
    public ResponseData login(HttpServletRequest request) {
    	//    	Properties conf = PropUtils.loadProps("config.properties");
//    	String jedisIp = PropUtils.getString(PropUtils.loadProps("config.properties"),"redis_ip","");
    	Jedis jedis = JedisUtil.newJedis();
    	String username = "hyp";
    	String loginname = request.getParameter("loginname");
    	String password = request.getParameter("pwd");
    	ResponseData responseData = new ResponseData();
        if ("hyp".equals(loginname) && "123456".equals(password)) {  
        	responseData.setSuccess(true);
        	responseData.setCode(WapInterface.SUCCESS);
        	responseData.setMsg("登录成功");
            responseData.putDataValue("UserName", username);
            responseData.putDataValue("password", password);
            responseData.putDataValue("LoginTime", new Date());
            long nowMillis = System.currentTimeMillis();
            long expMillis = nowMillis + 1000*60;//设置token六十秒过期
            TokenModel tm = new TokenModel(1,loginname,username,expMillis);
            String token = "";
			try {
				token = TokenUtil.createToken(tm);
				//TokenUtil.decodeToken(token);
				Map<String,Object> map = TokenUtil.tokenDecode(token);
				System.out.println(map);
				//jedis.hset("data", "token", token);
				//jedis.expire("data", 60);
				if (StringUtil.isNotEmpty(token)) {  
	                responseData.putDataValue("token", token);
	            }
			} catch (Exception e) {
				e.printStackTrace();
			}
            jedis.hset("user", "username", username);  
        }else{
        	responseData.setSuccess(false);
        	responseData.setCode(-1);
        	responseData.setMsg("登录失败，密码或者账号错误");
        }  
        return responseData;
    }
    
}