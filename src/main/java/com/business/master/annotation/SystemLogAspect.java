package com.business.master.annotation;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import redis.clients.jedis.Jedis;

import com.business.master.model.SystemLog;
import com.business.master.service.SystemLogService;
import com.business.utils.JedisUtil;
import com.business.utils.PropUtils;
import com.business.utils.StringUtil;


/**
 * @author 郝浚杰 
 * @E-mail: email
 * @version 创建时间：2018-01-05 下午4:29:05
 * @desc 切点类 
 */

@Aspect
@Component
public class SystemLogAspect {

    //注入Service用于把日志保存数据库  
    @Autowired  //这里我用resource注解，一般用的是@Autowired，他们的区别如有时间我会在后面的博客中来写
    private SystemLogService systemLogService;  
    
    private  static  final Logger logger = LoggerFactory.getLogger(SystemLogAspect. class);  
    
    private  static long start = 0;
    
    //Controller层切点  
    @Pointcut("execution (* com.business.master.controller..*.*(..))")  
    public  void controllerAspect() {  
    }  
    
    /** 
     * 前置通知 用于拦截Controller层记录用户的操作 
     * 
     * @param joinPoint 切点 
     */ 
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {
        System.out.println("==========执行controller前置通知===============");
        start = System.currentTimeMillis();
        if(logger.isInfoEnabled()){
            logger.info("before " + joinPoint);
        }
    }    
    
    //配置controller环绕通知,使用在方法aspect()上注册的切入点
     /* @Around("controllerAspect()")
      public void around(JoinPoint joinPoint){
          System.out.println("==========开始执行controller环绕通知===============");
          long start = System.currentTimeMillis();
          try {
              ((ProceedingJoinPoint) joinPoint).proceed();
              long end = System.currentTimeMillis();
              if(logger.isInfoEnabled()){
                  logger.info("around " + joinPoint + "\tUse time : " + (end - start) + " ms!");
              }
              System.out.println("==========结束执行controller环绕通知===============");
          } catch (Throwable e) {
              long end = System.currentTimeMillis();
              if(logger.isInfoEnabled()){
                  logger.info("around " + joinPoint + "\tUse time : " + (end - start) + " ms with exception : " + e.getMessage());
              }
          }
      }*/
    
    /** 
     * 后置通知 用于拦截Controller层记录用户的操作 
     * 
     * @param joinPoint 切点 
     */  
    @After("controllerAspect()")  
    public  void after(JoinPoint joinPoint) {  
    	Jedis jedis = JedisUtil.newJedis();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();  
        String username = jedis.hget("user", "username");
        String ip = request.getRemoteAddr().equals("0:0:0:0:0:0:0:1")?"::1":request.getRemoteAddr();
        Enumeration enu = request.getParameterNames();
    	long end = System.currentTimeMillis();
    	if(logger.isInfoEnabled()){
            logger.info("around " + joinPoint + "\tUse time : " + (end - start) + " ms!");
        }
         try {  
            
            String targetName = joinPoint.getTarget().getClass().getName();  
            String methodName = joinPoint.getSignature().getName();  
            Object[] arguments = joinPoint.getArgs();  
            Class targetClass = Class.forName(targetName);  
            Method[] methods = targetClass.getMethods();
            String operationType = "";
            String operationName = "";
             for (Method method : methods) {  
                 if (method.getName().equals(methodName)) {  
                    Class[] clazzs = method.getParameterTypes();  
                     if (clazzs.length == arguments.length) {  
                    	 Log log = method.getAnnotation(Log.class);
                    	 if(log!=null){
                    		 operationType = log.operationType();
                    		 operationName = log.operationName();
                    		 break;  
                    	 }
                    }  
                }  
            }
            //*========控制台输出=========*//  
            System.out.println("=====controller后置通知开始=====");  
            System.out.println("请求方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()")+"."+operationType);  
            System.out.println("方法描述:" + operationName);  
            System.out.println("请求人:" + username);  
            System.out.println("请求IP:" + ip);  
            //*========数据库日志=========*//  
            SystemLog log = new SystemLog();  
            log.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            log.setDescription(operationName);  
            log.setMethod((joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()")+"."+operationType);  
            log.setLogType("0");  
            log.setRequestIp(ip);  
            log.setExceptioncode( null);  
            log.setExceptionDetail( null);  
            log.setParams(null);  
            log.setCreateBy(username);  
            log.setCreateDate(new Date());  
            //保存数据库  
            systemLogService.insert(log);  
            System.out.println("=====controller后置通知结束=====");  
        }  catch (Exception e) {  
            //记录本地异常日志  
            logger.error("==后置通知异常==");  
            logger.error("异常信息:{}", e.getMessage());  
        }  
    } 
    
    //配置后置返回通知,使用在方法aspect()上注册的切入点
      @AfterReturning("controllerAspect()")
      public void afterReturn(JoinPoint joinPoint){
          System.out.println("=====执行controller后置返回通知=====");  
              if(logger.isInfoEnabled()){
                  logger.info("afterReturn " + joinPoint);
              }
      }
    
    /** 
     * 异常通知 用于拦截记录异常日志 
     * 
     * @param joinPoint 
     * @param e 
     */  
     @AfterThrowing(pointcut = "controllerAspect()", throwing="e")  
     public  void doAfterThrowing(JoinPoint joinPoint, Throwable e) {  
    	 Jedis jedis = JedisUtil.newJedis();
	     HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();  
	     String username = jedis.hget("user", "username");
	     String params = ""; 
	     String ip = request.getRemoteAddr().equals("0:0:0:0:0:0:0:1")?"::1":request.getRemoteAddr();
	     Enumeration enu = request.getParameterNames();
	     while(enu.hasMoreElements()){  
	    	 String paraName=(String)enu.nextElement();  
	    	 params += (paraName+":"+request.getParameter(paraName)) + ",";
    	 }
	     params = params.substring(0,params.lastIndexOf(",")); 
         try {  
             
             String targetName = joinPoint.getTarget().getClass().getName();  
             String methodName = joinPoint.getSignature().getName();  
             Object[] arguments = joinPoint.getArgs();  
             Class targetClass = Class.forName(targetName);  
             Method[] methods = targetClass.getMethods();
             String operationType = "";
             String operationName = "";
              for (Method method : methods) {  
                  if (method.getName().equals(methodName)) {  
                     Class[] clazzs = method.getParameterTypes();  
                      if (clazzs.length == arguments.length) {  
                    	  Log log = method.getAnnotation(Log.class);
                     	 if(log!=null){
                     		 operationType = log.operationType();
                     		 operationName = log.operationName();
                     		 break;  
                     	 }  
                     }  
                 }  
             }
             /*========控制台输出=========*/  
            System.out.println("=====异常通知开始=====");  
            System.out.println("异常代码:" + e.getClass().getName());  
            System.out.println("异常信息:" + e.getMessage());  
            System.out.println("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()")+"."+operationType);  
            System.out.println("方法描述:" + operationName);  
            System.out.println("请求人:" + username);  
            System.out.println("请求IP:" + ip);  
            System.out.println("请求参数:" + params);  
               /*==========数据库日志=========*/  
            SystemLog log = new SystemLog();
            log.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            log.setDescription(operationName);  
            log.setExceptioncode(e.getClass().getName());  
            log.setLogType("1");  
            String exceptionDetail = StringUtil.getThrowInfo(e);
            log.setExceptionDetail(exceptionDetail);  
            log.setMethod((joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));  
            log.setParams(params);  
            log.setCreateBy(username);  
            log.setCreateDate(new Date());  
            log.setRequestIp(ip);  
            //保存数据库  
            systemLogService.insert(log);  
            System.out.println("=====异常通知结束=====");  
        }  catch (Exception ex) {  
            //记录本地异常日志  
            logger.error("==异常通知异常==");  
            logger.error("异常信息:{}", ex.getMessage());  
        }  
         /*==========记录本地异常日志==========*/  
        logger.error("异常方法:{}异常代码:{}异常信息:{}参数:{}", joinPoint.getTarget().getClass().getName() + joinPoint.getSignature().getName(), e.getClass().getName(), e.getMessage(), params);  
  
    }  
}