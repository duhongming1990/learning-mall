package org.seckill.dto;

import lombok.Data;

@Data
public class SeckillResult<T>{

    private boolean success;

    private T data;

    private String error;
}
