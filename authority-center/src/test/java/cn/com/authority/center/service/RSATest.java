package cn.com.authority.center.service;

import cn.hutool.core.codec.Base64;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * RSA256 非对称加密:生成公钥和私钥
 *
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/10/21 20:34
 */
@Slf4j
@SpringBootTest
public class RSATest {

    @Test
    public void generateKeyBytes() throws Exception {
        //获取加密算法实例
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        //不能低于2048 否则会报错
        keyPairGenerator.initialize(2048);

        //生成公钥和私钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey aPublic = keyPair.getPublic();//公钥
        log.info("公钥:{}", Base64.encode(aPublic.getEncoded()));

        PrivateKey aPrivate = keyPair.getPrivate();//私钥
        log.info("私钥:{}", Base64.encode(aPrivate.getEncoded()));


    }

}
