package cn.com.authority.center.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户权限表
 * </p>
 *
 * @author Zhang Guang Ming
 * @since 2021-10-21
 */
@Data
public class AuthUserInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 职务
     */
    private String post;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String tel;

    /**
     * 账号过期日期
     */
    private LocalDateTime expirationTime;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 创建人
     */
    private String createdUser;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    /**
     * 更新人
     */
    private String updatedUser;

    /**
     * 是否删除(0:已删除,1:未删除)
     */
    private String isDelete;

    /**
     * 备注
     */
    private String remark;

    /**
     * 所属公司ID
     */
    private String coId;

    /**
     * 额外信息
     */
    private String extraInfo;


}
