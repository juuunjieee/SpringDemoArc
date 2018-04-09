package com.business.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xml.internal.security.utils.Base64;
  
public class TokenUtil {  
  
    //private static final String SECRET = "XX#$%()(#*!()!KL<><MQLMNQNQJQK sdfkjsdrow32234545fdf>?N<:{LWPW";  
	
	private static final String SECRET = "07a23553-976c";
      
    private static final String EXP = "exp";  
      
    private static final String PAYLOAD = "payload";  
    
    private static final  String MAC_INSTANCE_NAME = "HMacSHA256";
    /** 
     * get jwt String of object 
     * @param object 
     *            the POJO object 
     * @param maxAge 
     *            the milliseconds of life time 
     * @return the jwt token 
     */  
    /*public static <T> String sign(T object, long maxAge) {  
        try {  
            final JWTSigner signer = new JWTSigner(SECRET);  
            final Map<String, Object> claims = new HashMap<String, Object>();  
            ObjectMapper mapper = new ObjectMapper();  
            String jsonString = mapper.writeValueAsString(object);  
            claims.put(PAYLOAD, jsonString);  
            claims.put(EXP, System.currentTimeMillis() + maxAge);  
            return signer.sign(claims);  
        } catch(Exception e) {  
            return null;  
        }  
    }*/  
     
    /** 
     * get jwt String of object 
     * @param object 
     * @return the jwt token 
     */  
    /*public static <T> String sign(T object) {  
        try {  
            final JWTSigner signer = new JWTSigner(SECRET);  
            final Map<String, Object> claims = new HashMap<String, Object>();  
            ObjectMapper mapper = new ObjectMapper(); 
            String jsonString = mapper.writeValueAsString(object);  
            claims.put(PAYLOAD,jsonString);  
            return signer.sign(claims);  
        } catch(Exception e) {  
            return null;  
        }  
    }*/
      
    /** 
     * get the object of jwt if not expired 
     * @param jwt 
     * @return POJO object 
     */  
    /*public static<T> T unsign(String jwt, Class<T> classT) {  
        final JWTVerifier verifier = new JWTVerifier(SECRET);  
        try {  
            final Map<String,Object> claims= verifier.verify(jwt);  
            if (claims.containsKey(EXP) && claims.containsKey(PAYLOAD)) {  
                long exp = (Long)claims.get(EXP);  
                long currentTimeMillis = System.currentTimeMillis();  
                if (exp > currentTimeMillis) {  
                    String json = (String)claims.get(PAYLOAD);  
                    ObjectMapper objectMapper = new ObjectMapper();  
                    return objectMapper.readValue(json, classT);  
                }  
            }  
            return null;  
        } catch (Exception e) {  
            return null;  
        }  
    }*/
    
    public static String Hmacsha256(String secret, String message) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmac_sha256 = Mac.getInstance(MAC_INSTANCE_NAME);
        SecretKeySpec key = new SecretKeySpec(secret.getBytes(), MAC_INSTANCE_NAME);
        hmac_sha256.init(key);
        byte[] buff = hmac_sha256.doFinal(message.getBytes());
        return Base64.encode(buff);
    }
    
    /**
     * 
     * @param object 生成token的对象
     * @throws Exception
     */
    // java jwt
    public static <T> String overWriteSign(T object) throws Exception{
        String secret = SECRET;
        String header = "{\"typ\":\"JWT\",\"alg\":\"HS256\"}";
        //String claim = "{\"iss\":\"cnooc\", \"sub\":\"yrm\", \"username\":\"yrm\", \"admin\":true}";
        ObjectMapper mapper = new ObjectMapper();  
        String claim = mapper.writeValueAsString(object);
        //String claim = "{\"Id\":1,\"UserName\":\"hyp\",\"LoginName\":\"hyp\"}";
        String base64Header = Base64.encode(header.getBytes());
        String base64Claim = Base64.encode(claim.getBytes());
        String signature = Hmacsha256(secret, base64Header + "." + base64Claim);
        String jwt = base64Header + "." + base64Claim  + "." + signature;
        return jwt;
    }
    
    /**
     * 解析token，并且返回map对象
     * @param token 
     * @return tokenMap
     */
    public static Map<String,Object> tokenDecode(String token){
    	//String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJJZCI6MSwiTG9naW5OYW1lIjoiaHlwIiwiVXNlck5hbWUiOiJoeXAifQ==.a7bwIZx6sq9a/M2o7xxgS2CG5Q38vp/Qk298hDz9NQc=";
    	Map<String,Object> tokenMap = new HashMap<String,Object>();
		try {
		    DecodedJWT jwt = JWT.decode(token);
		    Map<String, Claim> claims = jwt.getClaims();
		    for(String key : claims.keySet()){
		    	String str = claims.get(key).asString();
		    	if(str==null){
		    		Integer i = claims.get(key).asInt();
		    		if(i!=null){
		    			tokenMap.put(key, i);
		    		}
		    	}else{
		    		tokenMap.put(key, str);
		    	}
		    }
		    
//		    String str = JsonUtil.getJsonStr(tokenMap);
//		    System.out.println(str);
		} catch (JWTDecodeException exception){
		    //Invalid token
			exception.printStackTrace();
		}
		return tokenMap;
    }
}
