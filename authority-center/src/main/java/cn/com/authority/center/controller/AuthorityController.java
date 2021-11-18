package cn.com.authority.center.controller;

import cn.com.authority.center.model.vo.JwtToken;
import cn.com.authority.center.service.JwtService;
import cn.com.common.handle.exception.BusinessException;
import cn.com.common.model.jwt.UsernameAndPassword;
import cn.com.common.utils.nulls.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/10/22 13:45
 */
@Slf4j
@RestController
@RequestMapping("/authority")
public class AuthorityController {


    private final JwtService jwtService;

    @Autowired
    public AuthorityController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * 从授权中心获取Token (其实就是登录功能)
     *
     * @param usernameAndPassword 入参
     * @return
     * @throws Exception
     */
    @PostMapping("/token")
    public JwtToken token(@RequestBody UsernameAndPassword usernameAndPassword) throws Exception {
        if (ObjectUtils.isEmpty(usernameAndPassword)) {
            throw new BusinessException("入参不能为空!!");
        }

        log.info("从授权中心获取Token入参：{}", usernameAndPassword);
        return new JwtToken(jwtService.generateToken(usernameAndPassword.getUsername(), usernameAndPassword.getPassword()));
    }


    /**
     * 注册用户并返回当前注册用户的 Token, 即通过授权中心创建用户
     *
     * @param usernameAndPassword 入参
     * @return
     * @throws Exception
     */
    @PostMapping("/register")
    public JwtToken register(@RequestBody UsernameAndPassword usernameAndPassword) throws Exception {
        if (ObjectUtils.isEmpty(usernameAndPassword)) {
            throw new BusinessException("入参不能为空!!");
        }

        log.info("注册用户并返回当前注册用户的 Token入参：{}", usernameAndPassword);
        return new JwtToken(jwtService.registerUserAndGenerateToken(usernameAndPassword.getUsername(), usernameAndPassword.getPassword()));
    }


}
