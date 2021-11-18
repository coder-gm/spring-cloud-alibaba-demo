package cn.com.common.utils.security;

import cn.com.common.model.constant.CommonConstant;
import cn.com.common.model.jwt.UsernameAndPassword;
import cn.com.common.utils.nulls.ObjectUtils;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import sun.misc.BASE64Decoder;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;

/**
 * JWT Token 解析工具类
 *
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/10/23 11:46
 */
public class TokenParseUtil {


    /**
     * 从 JWT Token 中解析 UsernameAndPassword 对象
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static UsernameAndPassword parseUserInfoFromToken(String token) throws Exception {

        if (ObjectUtils.isEmpty(token)) {
            return null;
        }

        Jws<Claims> claimsJws = parseToken(token, getPublicKey());
        Claims body = claimsJws.getBody();

        //如果 Token 已经过期 返回 null
        if (body.getExpiration().before(Calendar.getInstance().getTime())) {
            return null;
        }

        //返回Token中保存的用户信息
        return JSON.parseObject(body.get(CommonConstant.JWT_USER_INFO_KEY).toString(), UsernameAndPassword.class);

    }

    /**
     * 通过公钥去解析 JWT Token
     *
     * @param token
     * @param publicKey
     * @return
     */
    public static Jws<Claims> parseToken(String token, PublicKey publicKey) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }

    /**
     * 根据本地存储的公钥获取到 PublicKey 对象
     *
     * @return
     */
    private static PublicKey getPublicKey() throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(new BASE64Decoder().decodeBuffer(CommonConstant.PUBLIC_KEY));

        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }


}
