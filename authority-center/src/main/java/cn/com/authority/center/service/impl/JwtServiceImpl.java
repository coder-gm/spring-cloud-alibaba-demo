package cn.com.authority.center.service.impl;

import cn.com.authority.center.model.constant.AuthorityConstant;
import cn.com.authority.center.model.po.AuthUserInfo;
import cn.com.authority.center.service.AuthUserInfoService;
import cn.com.authority.center.service.JwtService;
import cn.com.common.model.constant.CommonConstant;
import cn.com.common.model.jwt.UsernameAndPassword;
import cn.com.common.utils.nulls.ObjectUtils;
import cn.com.common.utils.security.Md5Utils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Decoder;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

/**
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/10/21 21:27
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class JwtServiceImpl implements JwtService {

    @Autowired
    private AuthUserInfoService authUserInfoService;


    /**
     * 生成JWTToken,使用默认的超时时间
     *
     * @param username 用户名
     * @param password 密码
     * @return
     * @throws Exception
     */
    @Override
    public String generateToken(String username, String password) throws Exception {
        return this.generateToken(username, password, 0);
    }

    /**
     * 生成JWTToken,使用指定的超时时间
     *
     * @param username 用户名
     * @param password 密码
     * @param expire   超时时间(天)
     * @return
     * @throws Exception
     */
    @Override
    public String generateToken(String username, String password, int expire) throws Exception {

        if (expire <= 0) {
            expire = AuthorityConstant.DEFAULT_EXPIRE_DAY;
        }

        password = Md5Utils.md5(password);

        //首先需要验证用户是否能否通过授权校验,即输入的用户名和密码能否匹配数据库记录
        QueryWrapper<AuthUserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        wrapper.eq("password", password);
        AuthUserInfo authUserInfo = authUserInfoService.getOne(wrapper);

        if (!ObjectUtils.isEmpty(authUserInfo)) {

            UsernameAndPassword usernameAndPassword = new UsernameAndPassword(authUserInfo.getUsername(), password);

            // 计算超时时间
            ZonedDateTime zdt = LocalDate.now().plus(expire, ChronoUnit.DAYS)
                    .atStartOfDay(ZoneId.systemDefault());
            Date expireDate = Date.from(zdt.toInstant());

            return Jwts.builder()
                    //jwt payload --> KV (存在用户名密码信息)
                    .claim(CommonConstant.JWT_USER_INFO_KEY, JSON.toJSONString(usernameAndPassword))
                    //jwt id (没有任何作用,就是标识一个JWT ID)
                    .setId(UUID.randomUUID().toString())
                    //jwt 过期时间
                    .setExpiration(expireDate)
                    //jwt 签名 --> 加密
                    .signWith(SignatureAlgorithm.RS256, getPrivateKey())
                    .compact();
        }

        return null;
    }


    /**
     * 注册用户并生成 Token返回
     *
     * @param username 用户名
     * @param password 密码
     * @return
     * @throws Exception
     */

    @Override
    public String registerUserAndGenerateToken(String username, String password) throws Exception {
        //1.先去校验用户名是否存在, 如果存在, 不能重复注册
        QueryWrapper<AuthUserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        AuthUserInfo authUserInfo = authUserInfoService.getOne(wrapper);
        if (!ObjectUtils.isEmpty(authUserInfo)) {
            log.info("用户名:{} 已被注册,不能重复注册!!", authUserInfo.getUsername());
            return null;
        }
        //用户注册,把这个方法提取出来的意思是,可以添加完数据之后,提交事务,这样下面就可以查到数据了
        this.userRegister(username, password);

        return this.generateToken(username, password, 0);
    }

    /**
     * 用户注册
     *
     * @param username
     * @param password
     */
    @Transactional(rollbackFor = Exception.class)
    public AuthUserInfo userRegister(String username, String password) {
        AuthUserInfo authUserInfo = new AuthUserInfo();
        authUserInfo.setUsername(username);
        authUserInfo.setPassword(Md5Utils.md5(password));
        authUserInfoService.save(authUserInfo);

        return authUserInfo;
    }


    /**
     * 根据本地存储的私钥获取到 PrivateKey 对象
     *
     * @return
     * @throws Exception
     */
    private PrivateKey getPrivateKey() throws Exception {
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(new BASE64Decoder().decodeBuffer(AuthorityConstant.PRIVATE_KEY));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(priPKCS8);
    }


}
