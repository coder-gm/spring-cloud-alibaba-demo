package cn.com.authority.center.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/10/21 21:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken {


    /**
     * token
     */
    private String token;

}
