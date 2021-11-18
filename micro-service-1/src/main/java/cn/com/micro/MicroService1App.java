package cn.com.micro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author zgming
 */
@SpringBootApplication(scanBasePackages = {
        "cn.com.micro.service",
        "cn.com.common.handle",
        "cn.com.common.config",
})
@EnableDiscoveryClient
public class MicroService1App {

    public static void main(String[] args) {
        SpringApplication.run(MicroService1App.class, args);
    }

}
