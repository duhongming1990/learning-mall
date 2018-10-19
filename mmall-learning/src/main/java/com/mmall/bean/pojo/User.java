package com.mmall.bean.pojo;

import com.mmall.bean.CommonBean;
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

    private String username;

    private String password;

    private String email;

    private String phone;

    private String question;

    private String answer;

    private Integer role;

}