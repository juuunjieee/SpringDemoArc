package com.business.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 字符串工具类
 */
public class StringUtil {
	/**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String... strArr) {
    	for (String str : strArr) {
			if (StringUtil.isEmpty(str)) {
				return true;
			}
		}
		return false;
    }
    
	/**
	 * 字符串为 null 或者为 "" 时返回 true
	 */
	public static boolean isEmpty(String str) {
		return str == null || "".equals(str.trim()) || "null".equals(str) ? true
				: false;
	}
    
    /**
     * 判断字符串是否非空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    /**
     * 获取异常信息
     * @param e
     * @return
     */
    public static String getThrowInfo(Throwable e){   
        StringWriter sw = new StringWriter();   
        PrintWriter pw = new PrintWriter(sw, true);   
        e.printStackTrace(pw);   
        pw.flush();   
        sw.flush();   
        return sw.toString();   
    }
}
