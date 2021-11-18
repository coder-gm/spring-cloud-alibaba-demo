package cn.com.micro.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/10/27 17:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ExcelTarget(value = "emps")
public class Emps implements Serializable {
    private static final long serialVersionUID = 670669111497997044L;

    //导入的列的标题名必须要和name名称一样,要不找不到位置
    @Excel(name = "编号")
    private String id;

    @Excel(name = "姓名")
    private String name;

    @Excel(name = "年龄", orderNum = "3", suffix = "岁", replace = {"188_13", "1833_18"})
    private Integer age;

    @Excel(name = "生日",format = "yyyy-MM-dd HH:mm:ss")
    private Date bir;

    @Excel(name = "状态", orderNum = "4", replace = {"失效_0", "有效_1"})
    private Integer status;

}
