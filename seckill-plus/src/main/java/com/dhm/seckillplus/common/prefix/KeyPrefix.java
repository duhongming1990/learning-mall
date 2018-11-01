package com.dhm.seckillplus.common.prefix;
/**
 * @Author duhongming
 * @Email 19919902414@189.cn
 * @Date 2018/11/1 11:22
 */
public interface KeyPrefix {
	/**
	 * TTL Key淘汰时间（秒）
 	 * @return
	 */
	public int expireSeconds();

	/**
	 * redis前缀
	 * @return
	 */
	public String getPrefix();
	
}
