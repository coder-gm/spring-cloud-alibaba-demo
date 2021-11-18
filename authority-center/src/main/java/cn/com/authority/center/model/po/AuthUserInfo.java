package cn.com.authority.center.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户权限表
 * </p>
 *
 * @author Zhang Guang Ming
 * @since 2021-10-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("auth_user_info")
public class AuthUserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 昵称
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * 职务
     */
    @TableField("post")
    private String post;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 手机号
     */
    @TableField("tel")
    private String tel;

    /**
     * 账号过期日期
     */
    @TableField("expiration_time")
    private LocalDateTime expirationTime;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

    /**
     * 创建人
     */
    @TableField("created_user")
    private String createdUser;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private LocalDateTime updatedTime;

    /**
     * 更新人
     */
    @TableField("updated_user")
    private String updatedUser;

    /**
     * 是否删除(0:已删除,1:未删除)
     */
    @TableField("is_delete")
    @TableLogic(value = "1", delval = "0")
    private String isDelete;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 所属公司ID
     */
    @TableField("co_id")
    private String coId;

    /**
     * 额外信息
     */
    @TableField("extra_info")
    private String extraInfo;


}
