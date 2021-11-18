package cn.com.micro.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/10/27 15:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ExcelTarget(value = "card")
public class Card implements Serializable {
    private static final long serialVersionUID = -741795218093058305L;


    @Excel(name = "身份证号", width = 30.0, orderNum = "6")
    private String no;


    @Excel(name = "籍贯", width = 35.0, orderNum = "7")
    private String address;


}
