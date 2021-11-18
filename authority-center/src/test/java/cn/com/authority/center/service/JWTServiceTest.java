package cn.com.authority.center.service;

import cn.com.common.model.jwt.UsernameAndPassword;
import cn.com.common.utils.security.Md5Utils;
import cn.com.common.utils.security.TokenParseUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <h1>JWT 相关服务测试类</h1>
 */
@Slf4j
@SpringBootTest
public class JWTServiceTest {

    private final JwtService jwtService;

    @Autowired
    public JWTServiceTest(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Test
    public void testGenerateAndParseToken() throws Exception {

        String jwtToken = jwtService.generateToken("zhangguangming", "Gm859230");
        log.info("jwt token is: [{}]", jwtToken);

        UsernameAndPassword userInfo = TokenParseUtil.parseUserInfoFromToken(jwtToken);
        log.info("parse token: [{}]", JSON.toJSONString(userInfo));
    }

    @Test
    public void test(){
        log.info("jwt token is: [{}]", Md5Utils.md5("Gm859230"));
    }

}
