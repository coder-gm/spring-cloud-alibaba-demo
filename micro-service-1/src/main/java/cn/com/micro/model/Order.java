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
 * @Date 2021/10/27 16:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ExcelTarget(value = "order")
public class Order implements Serializable {
    private static final long serialVersionUID = 5376952926338931301L;


    @Excel(name = "订单编号", width = 30.0, orderNum = "6")
    private String no;


    @Excel(name = "订单名称", width = 35.0, orderNum = "7")
    private String name;


}
