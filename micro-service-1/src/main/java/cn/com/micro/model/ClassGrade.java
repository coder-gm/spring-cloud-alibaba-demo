package cn.com.micro.model;

import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/10/27 23:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ExcelTarget(value = "classGrade")
public class ClassGrade {

    private String index;
    private String className;
    private Students students;

}
