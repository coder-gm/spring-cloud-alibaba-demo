package cn.com.micro.feign.hystrix;

import cn.com.common.model.jwt.UsernameAndPassword;
import cn.com.micro.feign.AuthorityFeignClient;
import cn.com.micro.model.JwtToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * AuthorityFeignClient类的后备 FallbackFactory
 *
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/11/13 11:18
 */
@Slf4j
@Component
public class AuthorityFeignClientFallbackFactory implements FallbackFactory<AuthorityFeignClient> {


    @Override
    public AuthorityFeignClient create(Throwable throwable) {

        log.warn("调用授权接口异常信息:{},{}", throwable.getMessage(), throwable);

        return new AuthorityFeignClient() {

            @Override
            public JwtToken getToken(UsernameAndPassword usernameAndPassword) {
                return new JwtToken("执行了OpenFeign的后备策略");
            }
        };
    }
}