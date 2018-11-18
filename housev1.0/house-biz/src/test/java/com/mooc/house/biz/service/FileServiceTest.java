package com.mooc.house.biz.service;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.time.Instant;

public class FileServiceTest {
    @Test
    public void substringAfterLast() {
        String str = "D:\\data\\1542506339\\IMG_0906.JPG";
        String separator = "D:\\data";
        String result = StringUtils.substringAfterLast(str,separator);
        System.out.println("result = " + result);
    }

    @Test
    public void getEpochSecond(){
        long epochSecond = Instant.now().getEpochSecond();
        System.out.println("epochSecond = " + epochSecond);
    }

}