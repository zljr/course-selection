package cn.edu.zuel.controller;

import cn.edu.zuel.annotation.AuthAccess;
import cn.edu.zuel.common.Result;
import cn.edu.zuel.model.User;
import cn.edu.zuel.service.UserService;
import cn.hutool.core.util.StrUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class WebController {
    @Resource
    private UserService userService;

    /*
     * 这是测试接口连通的，应该放行
     * */
    @AuthAccess
    @GetMapping("/")
    public Result hello(){
        return Result.success("success");
    }
    @PostMapping("/login")
    public Result login(@RequestBody User user){
        if(StrUtil.isBlank(user.getUsername())||StrUtil.isBlank(user.getPassword())){
            return Result.error("输入数据不合法");
        }
        return Result.success(userService.login(user));
    }

    /*
     * 这里是注册，应该放行
     * */
    @AuthAccess
    @PostMapping("/register")
    public Result register(@RequestBody User user){
        if(StrUtil.isBlank(user.getUsername())||StrUtil.isBlank(user.getPassword())||StrUtil.isBlank(user.getRole())){
            return Result.error("输入数据不合法");
        }
        user =userService.register(user);
        return Result.success(user);
    }

    @AuthAccess
    @PutMapping("/password")
    public Result resetPassword(@RequestBody User user){
        if(StrUtil.isBlank(user.getUsername())||StrUtil.isBlank(user.getTelephone())){
            return Result.error("输入数据不合法");
        }
        userService.resetPassword(user);
        return Result.success();
    }
}
