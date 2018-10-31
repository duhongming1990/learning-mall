package com.dhm.seckillplus;

import com.dhm.seckillplus.dao.redis.RedisDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SeckillPlusApplicationTests {

	@Autowired
	private RedisDao redisUtils;
	@Test
	public void contextLoads() {
		//String
		String string_result = redisUtils.set("string_key","string_value",0);
		String string_value = redisUtils.get("string_key");

		//Map
		Map<String,String> map = new HashMap<>();
		map.put("map_key1","map_value1");
		map.put("map_key2","map_value2");
		map.put("map_key3","map_value3");
		String map_result = redisUtils.hmset("map_key",map,0);
		Map<String,String> map_value = redisUtils.hgetAll("map_key");

		//List
		List<String> list = new ArrayList<>();
		list.add("list_value1");
		list.add("list_value2");
		list.add("list_value3");
		list.add("list_value4");
		long list_result = redisUtils.rpush("list_key",list,0);
		List<String> list_value = redisUtils.lrange("list_key");

		//Set
		Set<String> set = new HashSet<>();
		set.add("set_value1");
		set.add("set_value2");
		set.add("set_value3");
		set.add("set_value4");
		long set_result = redisUtils.sadd("set_key",set,0);
		Set<String> set_value = redisUtils.smembers("set_key");

		//Zset
		Map<String,Double> zset = new HashMap<>();
		zset.put("map_key1",10.1);
		zset.put("map_key2",9.8);
		zset.put("map_key3",10.5);
		long zset_result = redisUtils.zset("zset_key",zset,0);
		Set<String> zset_value = redisUtils.zrange("zset_key",0,2);

		//pipeline
		Map<String,String> pipeline = new HashMap<>();
		pipeline.put("pipeline_key1","pipeline_value1");
		pipeline.put("pipeline_key2","pipeline_value2");
		pipeline.put("pipeline_key3","pipeline_value3");
		pipeline.put("pipeline_key4","pipeline_value4");
		redisUtils.pipeline(pipeline);

	}

}
