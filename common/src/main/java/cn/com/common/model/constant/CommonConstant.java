package cn.com.common.model.constant;

/**
 * 通用模块常量定义
 *
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/10/21 20:57
 */
public class CommonConstant {


    /**
     * RSA公钥,所有模块都可以使用这个公钥
     */
    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0aN1dpjkLBxqvfUi+/2GQp/nIX3LnvgeSNi8FCJJBNvbJvSdNR7p4dtG7vK69fgmpINZc0Bcmd2rT1sz1QcT8v8U+csjItsNlY" +
            "HuvmD0eEM1oIgY4JnZR1KIu0c58FKW2V3U5mIuuoWZ+S25CtVUvvnBeP5iZ4EokcpqjycGffgVyyCK3C+GA3HpeFCFkcL8Bd0hfdjKV3ZIHLWprM+ZIAjLHTzQuSobbb8fbJWSvh6Ut2ugE5suGCnczhDuFDXgYg9eBPOrTh4EfH5cD" +
            "30Zg2UgexA6cwN/HAWxwFg9E7OCQwula+V0pa1bKsduVhT8DFz6kjxEAdLHi16dV1y5ywIDAQAB";


    /**
     * JWT 中存储用户信息的 key
     */
    public static final String JWT_USER_INFO_KEY = "user-info-token";


    /**
     * 授权中心模块的ServiceID (注册中的名称)
     */
    public static final String AUTHORITY_CENTER_SERVICE_ID = "authority-center";

}
