package cn.com.micro.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 *
 * 使用  Ribbon  之前的配置，增强 RestTemplate
 *
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/11/9 14:28
 */
@Configuration
public class RibbonConfig {


    /**
     * 注入 RestTemplate
     *
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
