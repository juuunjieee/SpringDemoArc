package com.business.utils;

import redis.clients.jedis.Jedis;

public class JedisUtil {
	
	/**
	 * 创建jedis实例
	 * @return
	 */
	public static Jedis newJedis(){
		Jedis jedis = new Jedis(PropUtils.getString(PropUtils.loadProps("config.properties"),"redis_ip",""), 6379);
		String auth = PropUtils.getString(PropUtils.loadProps("config.properties"),"redis_password","");
		if(!StringUtil.isEmpty(auth)){
			jedis.auth(auth);
		}
		return jedis;
	}
}
