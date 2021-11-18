package cn.com.authority.center.service;

/**
 * JWT 相关接口定义
 *
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/10/21 21:21
 */
public interface JwtService {


    /**
     * 生成JWTToken,使用默认的超时时间
     *
     * @param username 用户名
     * @param password 密码
     * @return
     * @throws Exception
     */
    String generateToken(String username, String password) throws Exception;

    /**
     * 生成JWTToken,使用指定的超时时间
     *
     * @param username 用户名
     * @param password 密码
     * @param expire   超时时间(天)
     * @return
     * @throws Exception
     */
    String generateToken(String username, String password, int expire) throws Exception;


    /**
     * 注册用户并生成 Token返回
     *
     * @param username 用户名
     * @param password 密码
     * @return
     * @throws Exception
     */
    String registerUserAndGenerateToken(String username, String password) throws Exception;


}
