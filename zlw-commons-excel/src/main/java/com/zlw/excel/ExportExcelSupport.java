package com.zlw.excel;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


/**
 * 导出excel
 *
 * @version V1.0
 * @date 2012-7-13 下午02:14:55
 */
public abstract class ExportExcelSupport {

    public static <T> Workbook exportByListBean(String[] titles, List<T> dataList, String[] fields) throws Exception {
        return exportByListBean(ExcelVersion.EXCEL2007, null, titles, dataList, fields);
    }


    /**
     * 导出Excel，即是将List<T>转换成HSSFWorkbook对象,T:普通java Bean
     * <pre>
     * <b>Notice </b>： titles 顺序必须与fields 顺序一致。后边可以考虑用Map来实现，key 即为field，value为抬头名称
     * <pre>
     * @param titles excel表格中显示的抬头
     * @param dataList 数据列表List对象
     * @param fields dataList中Map对应的key
     * @return
     */
    public static <T> Workbook exportByListBean(String version, String[] titles, List<T> dataList, String[] fields) throws Exception {
        return exportByListBean(version, null, titles, dataList, fields);
    }

    /**
     * 导出Excel，即是将List<T>转换成HSSFWorkbook对象,T:普通java Bean
     * <pre>
     * <b>Notice </b>： titles 顺序必须与fields 顺序一致。后边可以考虑用Map来实现，key 即为field，value为抬头名称
     * <pre>
     * @param sheetName 导出目标excel文件的sheet名字，默认sheet1
     * @param titles excel表格中显示的抬头
     * @param dataList 数据列表List对象
     * @param fields dataList中Map对应的key
     * @return
     */
    public static <T> Workbook exportByListBean(String version, String sheetName, String[] titles, List<T> dataList, String[] fields) throws Exception {
        return exportByListBean(version, sheetName, titles, dataList, fields, null);
    }

    /**
     * 导出Excel，即是将List<T>转换成HSSFWorkbook对象,T:普通java Bean
     * <pre>
     * <b>Notice </b>： titles 顺序必须与fields 顺序一致。后边可以考虑用Map来实现，key 即为field，value为抬头名称
     * <pre>
     * @param sheetName 导出目标excel文件的sheet名字，默认sheet1
     * @param titles excel表格中显示的抬头
     * @param dataList 数据列表List对象
     * @param fields dataList中Map对应的key
     * @param cellStyle excel表格的样式
     * @return
     */
    public static <T> Workbook exportByListBean(String version, String sheetName, String[] titles, List<T> dataList, String[] fields, HSSFCellStyle cellStyle) throws Exception {
        Workbook workbook = POIExcelUtil.createWorkbook(version);
        Sheet sheet = POIExcelUtil.createSheet(workbook, sheetName);
        POIExcelUtil.setHeader(workbook, sheet, titles, cellStyle);
        POIExcelUtil.setWorkbookDataByBean(sheet, dataList, fields, cellStyle);
        return workbook;
    }


    /**
     * 导出Excel，即是将List<Map<String, Object>>转换成HSSFWorkbook对象
     * <pre>
     * <b>Notice </b>： titles 顺序必须与fields 顺序一致。后边可以考虑用Map来实现，key 即为field，value为抬头名称
     * <pre>
     * @param titles excel表格中显示的抬头
     * @param dataList 数据列表List对象
     * @param fields dataList中Map对应的key
     * @return
     */
    public static Workbook exportByListMap(String version, String[] titles, List<Map<String, Object>> dataList, String[] fields) {
        return exportByListMap(version, null, titles, dataList, fields);
    }


    /**
     * 导出Excel，即是将List<T>转换成HSSFWorkbook对象
     * <pre>
     * <b>Notice </b>： titles 顺序必须与fields 顺序一致。后边可以考虑用Map来实现，key 即为field，value为抬头名称
     * <pre>
     * @param sheetName 导出目标excel文件的sheet名字，默认sheet1
     * @param titles excel表格中显示的抬头
     * @param dataList 数据列表List对象
     * @param fields dataList中Map对应的key
     * @return
     */
    public static Workbook exportByListMap(String version, String sheetName, String[] titles, List<Map<String, Object>> dataList, String[] fields) {
        return exportByListMap(version, sheetName, titles, dataList, fields, null);
    }


    /**
     * 导出Excel，即是将List<T>转换成HSSFWorkbook对象
     * <pre>
     * <b>Notice </b>： titles 顺序必须与fields 顺序一致。后边可以考虑用Map来实现，key 即为field，value为抬头名称
     * <pre>
     * @param sheetName 导出目标excel文件的sheet名字，默认sheet1
     * @param titles excel表格中显示的抬头
     * @param dataList 数据列表List对象
     * @param fields dataList中Map对应的key
     * @param cellStyle excel表格的样式
     * @return
     */
    public static Workbook exportByListMap(String version,
                                           String sheetName,
                                           String[] titles,
                                           List<Map<String, Object>> dataList,
                                           String[] fields,
                                           HSSFCellStyle cellStyle) {

        Workbook workbook = POIExcelUtil.createWorkbook(version);
        Sheet sheet = POIExcelUtil.createSheet(workbook, sheetName);
        POIExcelUtil.setHeader(workbook, sheet, titles, cellStyle);
        POIExcelUtil.setWorkbookData(sheet, dataList, fields, cellStyle);
        return workbook;
    }
}
