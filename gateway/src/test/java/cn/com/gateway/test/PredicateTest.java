package cn.com.gateway.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/10/24 16:09
 */
@SpringBootTest
public class PredicateTest {


    public static List<String> MICRO_SERVICE = Arrays.asList(
            "nacos", "authority", "gateway", "ribbon", "feign", "hystrix", "e-commerce"
    );



    /**
     * <h2>test 方法主要用于参数符不符合规则, 返回值是 boolean</h2>
     * */
    @Test
    public void testPredicateTest() {
        Predicate<String> letterLengthLimit = s -> s.length() > 5;
        MICRO_SERVICE.stream().filter(letterLengthLimit).forEach(System.out::println);
    }


}
