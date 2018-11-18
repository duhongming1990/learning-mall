package com.mooc.house.biz.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

public class MailServiceTest {

    @Test
    public void randomAlphabetic(){
        String randomAlphabetic = RandomStringUtils.randomAlphabetic(10);
        System.out.println("randomAlphabetic = " + randomAlphabetic);
    }

}