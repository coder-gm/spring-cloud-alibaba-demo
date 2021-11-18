package cn.com.micro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author zgming
 */
@SpringBootApplication(scanBasePackages = {
        "cn.com.micro",
        "cn.com.common.handle",
        "cn.com.common.config",
})
@EnableDiscoveryClient
@EnableFeignClients //启用open-feign
@EnableCircuitBreaker //开启Hystrix 熔断
@ServletComponentScan
public class MicroService2App {

    public static void main(String[] args) {
        SpringApplication.run(MicroService2App.class, args);
    }

}
