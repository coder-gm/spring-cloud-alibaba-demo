package cn.com.hystrix.dashborad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * @author zgming
 */
@SpringBootApplication(scanBasePackages = {
        "cn.com.hystrix.dashborad",
        "cn.com.common.handle.exception",
        "cn.com.common.config",
})
@EnableDiscoveryClient
@EnableHystrixDashboard  //开启Hystrix Dashboard
public class HystrixDashboradApp {

    public static void main(String[] args) {
        SpringApplication.run(HystrixDashboradApp.class, args);
    }

}
