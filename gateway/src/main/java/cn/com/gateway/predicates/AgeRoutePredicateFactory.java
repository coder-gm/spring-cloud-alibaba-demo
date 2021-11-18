package cn.com.gateway.predicates;


import com.alibaba.cloud.commons.lang.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.GatewayPredicate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * 自定义断言工厂
 * <p>
 * 注意: 类名要求: 断言标识+RoutePredicateFactory
 *
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/11/9 9:04
 */

@Slf4j
@Component
public class AgeRoutePredicateFactory extends AbstractRoutePredicateFactory<AgeRoutePredicateFactory.Config> {

    public AgeRoutePredicateFactory() {
        super(AgeRoutePredicateFactory.Config.class);
    }

    /**
     * 用于从配置文件中获取参数值到配置类中的属性上
     *
     * @return
     */
    @Override
    public List<String> shortcutFieldOrder() {
        //这里的顺序要跟配置文件中的参数顺序一致
        return Arrays.asList("minAge", "maxAge");
    }


    /**
     * 断言逻辑
     *
     * @param config
     * @return
     */
    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return new GatewayPredicate() {
            @Override
            public boolean test(ServerWebExchange serverWebExchange) {
                ServerHttpRequest request = serverWebExchange.getRequest();
                String ageStr = request.getQueryParams().getFirst("age");
                if (StringUtils.isEmpty(ageStr)) {
                    return false;
                } else {
                    Integer age = Integer.parseInt(ageStr);

                    if (age >= config.getMinAge() && age <= config.getMaxAge()) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }


        };

    }


    /**
     * 自定义一个配置类,用于接收配置文件的参数
     * <p>
     * AbstractRoutePredicateFactory中的泛型用于接收断言中的参数
     */
    @Validated
    public static class Config {
        @NotNull
        private Integer minAge;
        @NotNull
        private Integer maxAge;

        public Integer getMinAge() {
            return minAge;
        }

        public void setMinAge(Integer minAge) {
            this.minAge = minAge;
        }

        public Integer getMaxAge() {
            return maxAge;
        }

        public void setMaxAge(Integer maxAge) {
            this.maxAge = maxAge;
        }
    }


}
