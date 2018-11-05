package com.mmall.bean.pojo;

import com.mmall.bean.CommonBean;
import com.mmall.desensitized.annotation.Desensitized;
import com.mmall.desensitized.enums.SensitiveTypeEnum;
import lombok.*;

import java.util.Date;
/**
 * @Author duhongming
 * @Email 19919902414@189.cn
 * @Date 2018/10/11 11:07
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User extends CommonBean {

    @Desensitized(type = SensitiveTypeEnum.CHINESE_NAME)
    private String username;

    @Desensitized(type = SensitiveTypeEnum.PASSWORD)
    private String password;

    @Desensitized(type = SensitiveTypeEnum.EMAIL)
    private String email;

    @Desensitized(type = SensitiveTypeEnum.MOBILE_PHONE)
    private String phone;

    private String question;

    private String answer;

    private Integer role;

}