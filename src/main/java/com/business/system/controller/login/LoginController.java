package com.business.system.controller.login;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**   
 * 创建时间：2018-4-11 下午2:17:27   
 * @author hjj   
 */  
@Controller
public class LoginController {
	private static final Logger logger = LogManager.getLogger(LoginController.class);
	
	@RequestMapping("/timeoutConfirm")  
    public ModelAndView timeoutConfirm(HttpServletRequest request){ 
    	ModelAndView mv = new ModelAndView();
        mv.setViewName("/jsp/login/timeout.jsp");
        return mv;
    }
	
	@RequestMapping("/noPermission")  
    public ModelAndView noPermission(HttpServletRequest request){ 
    	ModelAndView mv = new ModelAndView();
        mv.setViewName("/jsp/login/nopermission.jsp");
        return mv;
    }
}
