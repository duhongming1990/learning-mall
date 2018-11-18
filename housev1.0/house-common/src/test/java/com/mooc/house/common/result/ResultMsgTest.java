package com.mooc.house.common.result;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class ResultMsgTest {
    @Test
    public void joinerStr() throws UnsupportedEncodingException {
        Map<String,String> urlMap = Maps.newHashMap();
        urlMap.put("userName", URLEncoder.encode("杜洪明", "utf-8"));
        urlMap.put("nickName", URLEncoder.encode("阿杜杜不是阿木木", "utf-8"));
        urlMap.put("isNull", null);
        String joinerStr = Joiner.on("&").useForNull("").withKeyValueSeparator("=").join(urlMap);
        System.out.println("joinerStr = " + joinerStr);
    }

}