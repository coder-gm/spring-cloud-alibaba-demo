package cn.com.authority.center;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author zgming
 */
@MapperScan("cn.com.authority.center.mapper")
@SpringBootApplication(scanBasePackages = {
        "cn.com.authority.center",
        "cn.com.common.handle",
        "cn.com.common.config",
})
@EnableDiscoveryClient
public class AuthorityCenterApp {

    public static void main(String[] args) {
        SpringApplication.run(AuthorityCenterApp.class, args);
    }

}
