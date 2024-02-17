package com.sjf.service.auth;

import com.sjf.common.Result;
import com.sjf.dto.admin.MenuDto;
import com.sjf.dto.auth.LoginDto;
import com.sjf.dto.auth.ValidateCodeDto;
import com.sjf.vo.auth.LoginVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Description: 登录认证 Service 接口
 * @Author: SJF
 * @Date: 2024/1/13 12:15
 */

public interface AuthService {
    /**
     * 根据传输的用户登录对象尝试登录
     * @param loginVo: 传输的用户登录对象
     * @return: 登录的结果信息
     */
    Result<LoginDto> tryLogin(LoginVo loginVo);

    /**
     * 解析请求获取用户信息
     * @param request: 请求
     * @return: 用户信息
     */
    Result<Map<String, Object>> getUserInfo(HttpServletRequest request);

    /**
     * 生成用户登录验证码
     * @return: 后端返回的用户验证码
     */
    Result<ValidateCodeDto> generateValidateCode();

    /**
     * 注销用户登录。
     * @param token: 请求头中的 token
     * @return: 注销的结果信息
     */
    Result<Object> logout(String token);

    /**
     * 获取当前用户的路由信息
     * @return: 当前用户的路由信息
     */
    Result<List<MenuDto>> getRouters(String token);
}
