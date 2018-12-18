package com.dhm.seckillplus.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;

@Data
public class User {
    /**
     * 可用来自定义属性标签名称
     */
    @JsonProperty("userId")
    private Long id;
    private String nickname;

    /**
     * 可用来忽略不想输出某个属性的标签
     */
    @JsonIgnore
    private String password;
    /**
     * 可用来忽略不想输出某个属性的标签
     */
    @JsonIgnore
    private String salt;

    /**
     * 可用来动态包含属性的标签，如可以不包含为 null 值的属性
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String head;
    private Date registerDate;
    private Date lastLoginDate;
    private Integer loginCount;
}
