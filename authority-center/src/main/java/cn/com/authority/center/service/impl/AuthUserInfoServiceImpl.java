package cn.com.authority.center.service.impl;

import cn.com.authority.center.model.po.AuthUserInfo;
import cn.com.authority.center.mapper.AuthUserInfoMapper;
import cn.com.authority.center.service.AuthUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户权限表 服务实现类
 * </p>
 *
 * @author Zhang Guang Ming
 * @since 2021-10-21
 */
@Service
public class AuthUserInfoServiceImpl extends ServiceImpl<AuthUserInfoMapper, AuthUserInfo> implements AuthUserInfoService {

}
