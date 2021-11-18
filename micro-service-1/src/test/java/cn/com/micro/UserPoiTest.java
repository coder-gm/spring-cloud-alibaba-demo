package cn.com.micro;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.com.micro.model.Emps;
import cn.com.micro.model.Order;
import cn.com.micro.model.User;
import cn.com.micro.utils.easypoi.WorkBookUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

/**
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/10/27 9:52
 */
//@SpringBootTest
public class UserPoiTest {


    @Test
    public void test0() throws IOException {


        List<Order> orderList = new ArrayList<>();
        orderList.add(new Order("001", "连衣裙"));
        orderList.add(new Order("002", "裤子"));
        orderList.add(new Order("003", "卫衣"));

        List<User> userList = new ArrayList<>();
        userList.add(new User("001", "张三", 12, new Date(), 1, 0, "E:\\桌面\\资料\\工作资料\\上海弗兆实业资料\\图片\\Logo图片\\logo.png"));
        userList.add(new User("002", "李四", 13, new Date(), 0, 0, "E:\\桌面\\资料\\工作资料\\上海弗兆实业资料\\图片\\Logo图片\\logo.png"));
        userList.add(new User("003", "赵六", 14, new Date(), 1, 0, "E:\\桌面\\资料\\工作资料\\上海弗兆实业资料\\图片\\Logo图片\\logo.png"));
        userList.add(new User("004", "沈七", 15, new Date(), 0, 0, "E:\\桌面\\资料\\工作资料\\上海弗兆实业资料\\图片\\Logo图片\\logo.png"));
        userList.add(new User("005", "王八", 16, new Date(), 1, 0, "E:\\桌面\\资料\\工作资料\\上海弗兆实业资料\\图片\\Logo图片\\logo.png"));


        ExportParams params = new ExportParams();
        params.setSheetName("用户列表");
        params.setSheetName("用户信息");

        //导出excel
        // 一万以下数据量使用ExcelExportUtil.exportExcel()方法,
        // 一万条以上就使用ExcelExportUtil.exportBigExcel()这种方法)

        //参数1:ExportParams 导出配置对象 (title,sheetName)
        //参数2:导出的类型
        //参数3:导出的数据集合
        Workbook workbook = ExcelExportUtil.exportExcel(params, User.class, userList);

        //将excel写入指定位置
        FileOutputStream fileOutputStream = new FileOutputStream("E:\\桌面\\excel\\用户列表数据.xlsx");
        workbook.write(fileOutputStream);

        //关闭流
        fileOutputStream.close();
        workbook.close();


    }

    @Test
    public void test1() throws IOException {
        List<Map<String, Object>> lists = new ArrayList<>();


        //sheet1
        List<User> userList1 = new ArrayList<>();
        userList1.add(new User("001", "张三", 12, new Date(), 1, 0, "E:\\桌面\\资料\\工作资料\\上海弗兆实业资料\\图片\\Logo图片\\logo.png"));
        userList1.add(new User("002", "李四", 13, new Date(), 0, 0, "E:\\桌面\\资料\\工作资料\\上海弗兆实业资料\\图片\\Logo图片\\logo.png"));
        userList1.add(new User("003", "赵六", 14, new Date(), 1, 0, "E:\\桌面\\资料\\工作资料\\上海弗兆实业资料\\图片\\Logo图片\\logo.png"));
        userList1.add(new User("004", "沈七", 15, new Date(), 0, 0, "E:\\桌面\\资料\\工作资料\\上海弗兆实业资料\\图片\\Logo图片\\logo.png"));
        userList1.add(new User("005", "王八", 16, new Date(), 1, 0, "E:\\桌面\\资料\\工作资料\\上海弗兆实业资料\\图片\\Logo图片\\logo.png"));


        Map<String, Object> temp1 = WorkBookUtils.createSheet("表1", "表头大标题", User.class, userList1);
        lists.add(temp1);


        //sheet2
        List<User> userList2 = new ArrayList<>();
        userList2.add(new User("001", "张三", 12, new Date(), 1, 0, "E:\\桌面\\资料\\工作资料\\上海弗兆实业资料\\图片\\Logo图片\\logo.png"));
        userList2.add(new User("002", "李四", 13, new Date(), 0, 0, "E:\\桌面\\资料\\工作资料\\上海弗兆实业资料\\图片\\Logo图片\\logo.png"));
        userList2.add(new User("003", "赵六", 14, new Date(), 1, 0, "E:\\桌面\\资料\\工作资料\\上海弗兆实业资料\\图片\\Logo图片\\logo.png"));
        userList2.add(new User("004", "沈七", 15, new Date(), 0, 0, "E:\\桌面\\资料\\工作资料\\上海弗兆实业资料\\图片\\Logo图片\\logo.png"));
        userList2.add(new User("005", "王八", 16, new Date(), 1, 0, "E:\\桌面\\资料\\工作资料\\上海弗兆实业资料\\图片\\Logo图片\\logo.png"));


        Map<String, Object> temp2 = WorkBookUtils.createSheet("表2", "表头大标题", User.class, userList2);
        lists.add(temp2);

        Workbook workbook = WorkBookUtils.mutiSheet(lists);
        //将excel写入指定位置
        FileOutputStream fileOutputStream = new FileOutputStream("E:\\桌面\\excel\\用户数据.xlsx");
        workbook.write(fileOutputStream);

        //关闭流
        fileOutputStream.close();
        workbook.close();

    }


    /**
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {

        ImportParams importParams = new ImportParams();
        importParams.setTitleRows(0); //标题列占据几行 (意思是:不让程序去读取了)
        importParams.setHeadRows(1); //标题列占据几行 (意思是:不让程序去读取了)
        importParams.setStartSheetIndex(1);//读取第几个sheet页的数据
        importParams.setImportFields(new String[]{"编号", "状态"}); //导入的excel中必须有编号,状态 列


        //参数1:导入的excel文件流
        //参数2:导入的类型
        //参数3:导入的配置对象
        List<Emps> empsList = ExcelImportUtil.importExcel(new File("E:\\桌面\\excel\\用户列表数据.xlsx"), Emps.class, importParams);

        empsList.stream().forEach(e -> {
            System.out.println(e.toString());
        });


    }

    @Test
    public void test3() {
        String aa="202111";
        BigInteger bigInteger = new BigInteger(aa.substring(0, 4));
        System.out.println(bigInteger.subtract(BigInteger.ONE).toString());

    }

}
