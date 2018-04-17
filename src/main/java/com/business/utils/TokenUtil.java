package com.business.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.business.master.model.TokenModel;
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
    	token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJMb2dpbk5hbWUiOiJoeXAiLCJVc2VyTmFtZSI6Imh5cCIsImlzcyI6ImF1dGgwIiwiSWQiOjEsImV4cCI6MTUyMzQ0MDA1NH0.ocmswv1ULBbMvJRUvJ0OPq4wacCQ-3XsXDrGt6E2q_k";
    	Map<String,Object> tokenMap = new HashMap<String,Object>();
		try {
		    DecodedJWT jwt = JWT.decode(token);
		    Map<String, Claim> claims = jwt.getClaims();
		    for(String key : claims.keySet()){
		    	String str = claims.get(key).asString();
		    	if(str==null){
		    		Long i = claims.get(key).asLong();
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
    public static void decodeToken(String token){
    	token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1MjM0MzYzODAsImhvc3RhZGRyZXNzIjoiMTE5LjEyOS4yMjcuNSIsImhvc3RuYW1lIjoiMTE5LjEyOS4yMjcuNSJ9._tTPScYwUZImYXIkiXPBxrbHzMziYZqV-5yJODYVwfc";
    	try {
    	    Algorithm algorithm = Algorithm.HMAC256("secret");
    	    JWTVerifier verifier = JWT.require(algorithm)
    	        .withIssuer("auth0")
    	        .build(); //Reusable verifier instance
    	    DecodedJWT jwt = verifier.verify(token);
    	    System.out.println(jwt);
    	} catch (UnsupportedEncodingException exception){
    	    //UTF-8 encoding not supported
    		exception.printStackTrace();
    	} catch (JWTVerificationException exception){
    	    //Invalid signature/claims
    		exception.printStackTrace();
    	}
    }
    public static String createToken(TokenModel tm){
    	try {
    		Date date = new Date(System.currentTimeMillis());
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(date);
    		cal.add(Calendar.SECOND, 120);
    		date = cal.getTime();
    	    Algorithm algorithm = Algorithm.HMAC256("secret");
    	    String token = JWT.create()
    	        .withIssuer("auth0")
    	        .withClaim("Id", tm.getId())
    	        .withClaim("LoginName", tm.getLoginName())
    	        .withClaim("UserName", tm.getUserName())
    	        .withExpiresAt(date)
    	        .sign(algorithm);
    	    System.out.println(token);
    	    return token;
    	} catch (UnsupportedEncodingException exception){
    	    //UTF-8 encoding not supported
    		exception.printStackTrace();
    	} catch (JWTCreationException exception){
    	    //Invalid Signing configuration / Couldn't convert Claims.
    		exception.printStackTrace();
    	}
		return null;
    }
}
