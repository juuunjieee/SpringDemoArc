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
import com.business.utils.TokenUtil;
import com.business.utils.PropUtils;
import com.business.utils.ResponseData;
  
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
        mv.setViewName("/login/login.jsp");
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
    	Jedis jedis = new Jedis(PropUtils.getString(PropUtils.loadProps("config.properties"),"redis_ip",""), 6379);
    	String username = request.getParameter("username");
    	String loginname = request.getParameter("loginname");
    	String password = request.getParameter("pwd");
    	ResponseData responseData = new ResponseData();
        if ("hyp".equals(loginname) && "123456".equals(password)) {  
        	responseData.setSuccess(true);
        	responseData.setCode(200);
        	responseData.setMsg("登录成功");
            responseData.putDataValue("UserName", username);
            responseData.putDataValue("password", password);
            responseData.putDataValue("LoginTime", new Date());
            TokenModel tm = new TokenModel(1,loginname,username);
            //String token = JWT.sign(tm, 30L * 24L * 3600L * 1000L); 
            String token;
			try {
				token = TokenUtil.overWriteSign(tm);
				Map<String,Object> map = TokenUtil.tokenDecode(token);
				System.out.println(map);
				if (token != null) {  
	                responseData.setDetail(token);  
	            }
			} catch (Exception e) {
				// TODO Auto-generated catch block
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
    
    
    /*public static String Hmacsha256(String secret, String message) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmac_sha256 = Mac.getInstance(MAC_INSTANCE_NAME);
        SecretKeySpec key = new SecretKeySpec(secret.getBytes(), MAC_INSTANCE_NAME);
        hmac_sha256.init(key);
        byte[] buff = hmac_sha256.doFinal(message.getBytes());
        return Base64.encodeBase64URLSafeString(buff);
    }

    // java jwt
    public void testJWT(TokenModel object) throws InvalidKeyException, NoSuchAlgorithmException, JsonGenerationException, JsonMappingException, IOException {
        String secret = "07a23553-976c";
        String header = "{\"typ\":\"JWT\",\"alg\":\"HS256\"}";
        //String claim = "{\"iss\":\"cnooc\", \"sub\":\"yrm\", \"username\":\"yrm\", \"admin\":true}";
        ObjectMapper mapper = new ObjectMapper();  
        String claim = mapper.writeValueAsString(object);
        //String claim = "{\"Id\":1,\"UserName\":\"hyp\",\"LoginName\":\"hyp\"}";
        String base64Header = Base64.encodeBase64URLSafeString(header.getBytes());
        String base64Claim = Base64.encodeBase64URLSafeString(claim.getBytes());
        String signature = Hmacsha256(secret, base64Header + "." + base64Claim);
        
        String jwt = base64Header + "." + base64Claim  + "." + signature;
        System.out.println(jwt);
    }*/
    
}