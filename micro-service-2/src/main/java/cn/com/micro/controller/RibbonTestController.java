package cn.com.micro.controller;

import cn.com.common.model.jwt.UsernameAndPassword;
import cn.com.micro.model.JwtToken;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/11/9 11:52
 */
@Slf4j
@RestController
@RequestMapping("/ribbonTest")
public class RibbonTestController {

    /**
     * 获取注册中心服务，有负载均衡的效果
     */
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/getJwtToken")
    public String getJwtToken(@RequestBody UsernameAndPassword usernameAndPassword) {

        String requestUrl = String.format("http://%s/authority-center/authority/token", "authority-center");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JwtToken token = restTemplate.postForObject(
                requestUrl,
                new HttpEntity<>(JSON.toJSONString(usernameAndPassword), headers),
                JwtToken.class
        );

        log.info("token:{}", token);
        if (null != token) {
            return token.getToken();
        }
        return null;
    }


}
