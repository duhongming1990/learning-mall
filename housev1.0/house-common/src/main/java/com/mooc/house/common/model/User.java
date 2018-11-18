package com.mooc.house.common.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
/**
 * @Author duhongming
 * @Email 19919902414@189.cn
 * @Date 2018/11/17 11:25
 */
public class User extends CommonBean{

	private Long id;

	@NotNull
	@Email(message = "Email 格式有误")
	private String email;

	private String phone;
	
	private String name;

	@NotNull
	@Length(min = 6,message = "密码大于等于6位")
	private String passwd;

	@NotNull
	@Length(min = 6,message = "密码大于等于6位")
	private String confirmPasswd;

	/**
	 * 普通用户1
	 * 经纪人2
	 */
	@Min(1)
	@Max(2)
	private Integer type;
	
	private Date   createTime;
	
	private Integer enable;
	
	private String  avatar;
	
	private MultipartFile avatarFile;
	
	private String newPassword;
	
	private String key;
	
	private Long   agencyId;
	
	private String aboutme;
	
	private String agencyName;
}
