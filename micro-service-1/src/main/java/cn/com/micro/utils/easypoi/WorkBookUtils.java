package cn.com.micro.utils.easypoi;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author Guang Ming Zhang
 * @Date 2021/10/27 17:17
 */
public class WorkBookUtils {
    /**
     * 创建workbook,
     * 通过maplist填充Excel内容
     * 返回workbook
     * <p>
     * 进一步使用可以写入流,e.g.
     * FileOutputStream fos = new FileOutputStream(file);
     * workbook.write(fos);
     */
    public static Workbook mutiSheet(List<Map<String, Object>> mapListList) {
        Workbook workbook = null;
        workbook = ExcelExportUtil.exportExcel(mapListList, ExcelType.XSSF);
        return workbook;
    }



    /**
     * 创建一个表格并填充内容
     * 返回map供工作簿使用
     *
     * @param sheetName
     * @param title
     * @param clazz
     * @param data
     * @return
     */
    public static Map<String, Object> createSheet(String sheetName, String title, Class<?> clazz, List<?> data) {
        ExportParams exportParams = new ExportParams(title, sheetName, ExcelType.XSSF);

        Map<String, Object> map = new HashMap<>(3);

        //new ExportParams("title"+i, "sheetName"+i, ExcelType.XSSF)
        map.put("title", exportParams);
        map.put("entity", clazz);
        map.put("data", data);

        return map;
    }




}
