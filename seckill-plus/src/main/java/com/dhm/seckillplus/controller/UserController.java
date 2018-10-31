package com.dhm.seckillplus.controller;

import com.dhm.seckillplus.domain.User;
import com.dhm.seckillplus.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/to_list")
    @ResponseBody
    public Result<User> list(User user){
        System.out.println(user.toString());
        return Result.success(user);
    }
}
