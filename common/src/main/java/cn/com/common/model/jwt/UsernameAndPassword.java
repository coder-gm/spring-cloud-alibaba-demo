package cn.com.common.model.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/10/23 12:53
 */
@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class UsernameAndPassword {

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户名
     */
    private String password;

}
