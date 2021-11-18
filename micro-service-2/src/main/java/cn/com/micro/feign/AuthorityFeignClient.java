package cn.com.micro.feign;

import cn.com.common.model.jwt.UsernameAndPassword;
import cn.com.micro.feign.hystrix.AuthorityFeignClientFallbackFactory;
import cn.com.micro.model.JwtToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/11/9 20:43
 */

//value:意思是需要调用的微服务的服务名
//contextId:调用authority-center这个微服务的ID，如果需要在不同的地方调用authority-center，需要设定不同的contextId
@FeignClient(contextId = "AuthorityFeignClient", value = "authority-center", fallbackFactory = AuthorityFeignClientFallbackFactory.class)
public interface AuthorityFeignClient {


    /**
     * 通过 OpenFeign 访问 authority-center 微服务获取Token
     *
     * @param usernameAndPassword
     * @return
     */
    @PostMapping(value = "/authority-center/authority/token")
    JwtToken getToken(@RequestBody UsernameAndPassword usernameAndPassword);
}
