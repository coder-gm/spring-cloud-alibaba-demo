package cn.com.micro;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.com.micro.model.ClassGrade;
import cn.com.micro.model.Students;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/10/27 20:08
 */
public class TemplatePoiTest {


    @Test
    public void fe_map() throws Exception {
        TemplateExportParams params = new TemplateExportParams("template/专项支出用款申请书_map.xlsx");
        String[] sheetNameArray = {"sheet名称"};
        params.setSheetName(sheetNameArray);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", "2014-12-25");
        map.put("money", 2000000.00);
        map.put("upperMoney", "贰佰万");
        map.put("company", "执笔潜行科技有限公司");
        map.put("bureau", "财政局");
        map.put("person", "JueYue");
        map.put("phone", "1879740****");
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for (int i = 0; i < 4; i++) {
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("id", i + 1 + "");
            lm.put("zijin", i * 10000 + "");
            lm.put("bianma", "A001");
            lm.put("mingcheng", "设计");
            lm.put("xiangmumingcheng", "EasyPoi " + i + "期");
            lm.put("quancheng", "开源项目");
            lm.put("sqje", i * 10000 + "");
            lm.put("hdje", i * 10000 + "");

            listMap.add(lm);
        }
        map.put("maplist", listMap);

        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        File savefile = new File("E:\\桌面\\excel");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("E:\\桌面\\excel\\专项支出用款申请书_map.xlsx");
        workbook.write(fos);
        fos.close();
    }


    @Test
    public void test3() throws IOException {

        // 存放数据map
        Map<String, Object> map = new HashMap<String, Object>();

        // 查询数据,此处省略
        List<ClassGrade> classGradeList = new ArrayList<>();
        int count1 = 0;
        // 学生信息
        Students students1 = new Students("张三", "男", 20, "北京市东城区", "篮球");
        Students students2 = new Students("李四", "男", 17, "北京市西城区", "游泳");
        Students students3 = new Students("淑芬", "女", 34, "北京市丰台区", "唱歌，跳舞");
        Students students4 = new Students("仲达", "男", 55, "北京市昌平区", "象棋，足球");


        // sheet1内容
        classGradeList.add(new ClassGrade("1", "信科", students1));
        classGradeList.add(new ClassGrade("2", "生工", students2));
        classGradeList.add(new ClassGrade("3", "化工", students3));
        classGradeList.add(new ClassGrade("4", "信科", students4));
        map.put("classGradeList", classGradeList);


        // sheet2内容
        List<Students> studentsList = new ArrayList<>();
        studentsList.add(students1);
        studentsList.add(students2);
        studentsList.add(students3);
        studentsList.add(students4);
        map.put("studentsList", studentsList);


        // 设置导出配置
        // 获取导出excel指定模版
        TemplateExportParams params = new TemplateExportParams("template/班级学生信息.xlsx", true);
        // 设置sheetName，若不设置该参数，则使用得原本得sheet名称
        String[] sheetNameArray = {"班级信息", "学生信息"};
        params.setSheetName(sheetNameArray);
        // 导出excel
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        File savefile = new File("E:\\桌面\\excel");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("E:\\桌面\\excel\\班级学生信息.xlsx");
        workbook.write(fos);
        fos.close();

    }


    public static void main(String[] args) {

        String name="{{ }}";

        String substring = name.substring(name.indexOf("{{") + 2, name.indexOf("}}"));

        System.out.println(substring);
    }

}