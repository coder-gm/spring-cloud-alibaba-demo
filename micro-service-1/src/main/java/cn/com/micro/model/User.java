package cn.com.micro.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/10/27 9:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ExcelTarget(value = "user")
public class User implements Serializable {


    private static final long serialVersionUID = -4040295753213172219L;

    @Excel(name = "编号", width = 15.0, orderNum = "0")
    private String id;

    @Excel(name = "姓名", width = 15.0, orderNum = "1")
    private String name;

    @Excel(name = "年龄", width = 15.0, orderNum = "3", suffix = "岁", replace = {"188_13", "1833_18"})
    private Integer age;

    @Excel(name = "生日", width = 35.0, format = "yyyy-MM-dd HH:mm:ss", orderNum = "2")
    private Date bir;


    @Excel(name = "状态", width = 15.0, orderNum = "4", replace = {"失效_0", "有效_1"})
    private Integer status;

    @ExcelIgnore //表示不会被导入到excel中
    private Integer isDelete;


    //type = 2 这是必须的
    @Excel(name = "头像", type = 2, width = 10.0, height = 10.0, orderNum = "5")
    private String phone;
}
