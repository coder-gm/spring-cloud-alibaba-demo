package cn.com.micro.model;

import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/10/27 9:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ExcelTarget(value = "userinfo")
public class Students implements Serializable {


    private static final long serialVersionUID = -4040295753213172219L;


    private String studentName;

    private String sex;

    private Integer age;

    private String address;

    private String hobby;
}
