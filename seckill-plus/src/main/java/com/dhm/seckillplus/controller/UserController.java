package com.dhm.seckillplus.controller;

import com.dhm.seckillplus.common.response.ResultBean;
import com.dhm.seckillplus.domain.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
    @Author duhongming
    @Email 935720334@qq.com
    @Date 2018/10/31 20:53
*/
@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/to_list")
    public ResultBean<User> list(User user){
        return new ResultBean<>(user);
    }
}
