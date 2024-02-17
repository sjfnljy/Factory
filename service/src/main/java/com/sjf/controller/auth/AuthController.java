package com.sjf.controller.auth;

import com.sjf.common.Result;
import com.sjf.dto.admin.MenuDto;
import com.sjf.dto.auth.LoginDto;
import com.sjf.dto.auth.ValidateCodeDto;
import com.sjf.service.auth.AuthService;
import com.sjf.vo.auth.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Description: 登录认证请求处理器
 * @Author: SJF
 * @Date: 2024/1/13 12:16
 */

@Api(tags = "系统认证管理接口")
@RestController
@RequestMapping("/factory/admin/auth")
public class AuthController {

    @Resource
    private AuthService authService;

    @ApiOperation(value = "生成验证码")
    @GetMapping("/generate/validateCode")
    public Result<ValidateCodeDto> generateValidateCode(){
        return authService.generateValidateCode();
    }


    @ApiOperation(value = "用户登录")
    @PostMapping("login")
    public Result<LoginDto> login(@RequestBody LoginVo loginVo) {
        return authService.tryLogin(loginVo);
    }

    @ApiOperation(value = "获取用户信息")
    @GetMapping("info")
    public Result<Map<String,Object>> info(HttpServletRequest request){
        return authService.getUserInfo(request);
    }

    @GetMapping("/routers")
    public Result< List<MenuDto>> menus(@RequestHeader("token") String token) {
        return authService.getRouters(token);
    }

    @ApiOperation(value = "用户注销登录")
    @PostMapping("logout")
    public Result<Object> logout(@RequestHeader("token") String token){
        return authService.logout(token);
    }
}
