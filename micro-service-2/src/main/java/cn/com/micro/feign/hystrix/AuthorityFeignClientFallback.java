package cn.com.micro.feign.hystrix;

import cn.com.common.model.jwt.UsernameAndPassword;
import cn.com.micro.feign.AuthorityFeignClient;
import cn.com.micro.model.JwtToken;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * AuthorityFeignClient类的后备 fallback
 *
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/11/13 11:18
 */
@Slf4j
@Component
public class AuthorityFeignClientFallback implements AuthorityFeignClient {


    @Override
    public JwtToken getToken(UsernameAndPassword usernameAndPassword) {
        log.info("授权冒充客户端通过冒充请求错误获得令牌 (Hystrix Fallback): [{}]", JSON.toJSONString(usernameAndPassword));
        return new JwtToken("执行了OpenFeign的后备策略");
    }

}
