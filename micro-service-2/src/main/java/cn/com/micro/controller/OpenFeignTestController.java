package cn.com.micro.controller;

import cn.com.common.model.jwt.UsernameAndPassword;
import cn.com.micro.feign.AuthorityFeignClient;
import cn.com.micro.model.JwtToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/11/9 20:55
 */
@Slf4j
@RestController
@RequestMapping("/openFeignTest")
public class OpenFeignTestController {

    @Qualifier("cn.com.micro.feign.AuthorityFeignClient")
    @Autowired
    private AuthorityFeignClient authorityFeignClient;


    /**
     * 通过 OpenFeign 访问 authority-center 微服务获取Token
     *
     * @param usernameAndPassword
     * @return
     */
    @PostMapping(value = "/getToken")
    public JwtToken getToken(@RequestBody UsernameAndPassword usernameAndPassword) {
        return authorityFeignClient.getToken(usernameAndPassword);
    }

}
