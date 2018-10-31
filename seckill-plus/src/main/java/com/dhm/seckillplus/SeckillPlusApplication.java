package com.dhm.seckillplus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class SeckillPlusApplication
		extends SpringBootServletInitializer

	{

	public static void main(String[] args) {
		SpringApplication.run(SeckillPlusApplication.class, args);
	}

	//3打成war包
//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//		return builder.sources(SeckillPlusApplication.class);
//	}
}
