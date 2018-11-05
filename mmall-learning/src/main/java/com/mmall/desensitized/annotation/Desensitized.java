package com.mmall.desensitized.annotation;

import com.mmall.desensitized.enums.RoleTypeEnum;
import com.mmall.desensitized.enums.SensitiveTypeEnum;

import java.lang.annotation.*;

/**
 * @Author duhongming
 * @Email 19919902414@189.cn
 * @Date 2018/9/6 14:52
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Desensitized {
    /*脱敏类型(规则)*/
    SensitiveTypeEnum[] type() default SensitiveTypeEnum.CHINESE_NAME;

    /*判断注解是否生效的方法*/
    String isEffictiveMethod() default "";


    RoleTypeEnum[] role() default RoleTypeEnum.ALL_ROLES;
}