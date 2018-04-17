package com.business.system.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.business.utils.StringUtil;
import com.business.utils.TokenUtil;

public class CustomFilter implements Filter{
	private static final Logger logger = LogManager.getLogger(CustomFilter.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("-----CustomFilter init");
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		String uri = request.getRequestURI();
		try {
			
			if (uri.startsWith("/") && uri.length() == 1) {
				chain.doFilter(request, response);
				return;
			}
			if(uri.endsWith("/login")){
				chain.doFilter(request, response);
				return;
			}
			if (uri.endsWith("/timeoutConfirm")||uri.endsWith("/noPermission")) {
				chain.doFilter(request, response);
				return;
			}
			if (uri.endsWith(".jsp")) {
				chain.doFilter(request, response);
				return;
			}
			if(uri.endsWith("/showInfo")){
				chain.doFilter(request, response);
				return;
			}
			//判断是否过期
			String token = request.getHeader("Token");
			if(!uri.endsWith("/login")){
				if(StringUtil.isEmpty(token)){
					// 如果操作员未登录或登录超时，重定向到后台登录界面
					logger.info("==>请求已拦截 url：" + uri + ",会话超时");
					
					//String urlTo = request.getContextPath() + "/timeoutConfirm";
					String urlTo = "/timeoutConfirm";
					//response.sendRedirect(urlTo);
					request.getRequestDispatcher(urlTo).forward(request, response);  
					return;
				}else{
					Map<String, Object> tokenMap = TokenUtil.tokenDecode(token);
					long nowMillis = System.currentTimeMillis();
					long oldMillis = (long) tokenMap.get("Exp");
					if(nowMillis>oldMillis){
						String urlTo = "/timeoutConfirm";
						request.getRequestDispatcher(urlTo).forward(request, response);  
						return;
					}
					String nopermission = "noper";
					if("noper".equals(nopermission)){
						String urlTo = "/noPermission";
						request.getRequestDispatcher(urlTo).forward(request, response);  
						return;
					}
				}
			}
			//其他情况
			chain.doFilter(request, response);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {
		logger.info("-----CustomFilter destroy");
	}

}
