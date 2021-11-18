package cn.com.micro.controller;

import cn.com.micro.model.User;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/11/8 21:01
 */
@Slf4j
@RestController
@RequestMapping("/userinfo")
public class UserInfoController {

    @GetMapping("/getUserList")
    public List<User> getUserList() {
        List<User> list = new ArrayList<>();
        User user = new User();
        user.setAge(1111);
        user.setBir(new Date());
        user.setId("12323232");
        list.add(user);
        list.add(user);
        list.add(user);
        list.add(user);
        list.add(user);

        log.info("结果:{}", JSON.toJSONString(list));

        return list;
    }
}
