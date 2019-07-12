package com.zlw.excel;

import org.apache.commons.beanutils.BeanUtilsBean2;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Map;

import static org.apache.poi.ss.usermodel.IndexedColors.AQUA;


/**
 * POI操作excel工具类
 *
 * @version V1.0
 */
public class POIExcelUtil {

    /**
     * 创建了一个excel文件
     *
     * @return
     */
    public static Workbook createWorkbook(String version) {
        if (version.equalsIgnoreCase(ExcelVersion.EXCEL2003)) {
            return new HSSFWorkbook();
        } else if (version.equalsIgnoreCase(ExcelVersion.EXCEL2007)) {
            return new XSSFWorkbook();
        } else {
            throw new RuntimeException("poi not support");
        }

    }


    /**
     * 创建一个excel sheet
     *
     * @param sheetName sheet名字，若为空则默认为‘sheet1’
     * @return
     */
    public static Sheet createSheet(Workbook workbook, String sheetName) {
        if (StringUtils.isBlank(sheetName)) {
            sheetName = ExcelConstants.DEFAUT_SHEET_NAME;
        }
        return workbook.createSheet(sheetName);
    }

    /**
     * 设置表头
     *
     * @param workbook  工作簿对象
     * @param sheet     工作表
     * @param titles    表头名称
     * @param cellStyle 表格样式,，不需要添加样式，设置为null
     */
    public static void setHeader(Workbook workbook, Sheet sheet, String[] titles, CellStyle cellStyle) {
        // 默认设置表头为第一行
        Row titleRow = sheet.createRow(ExcelConstants.DEFAUT_HEADER_SHOW_LINE);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = titleRow.createCell(i, CellType.STRING);// 创建数据列
            if (cellStyle != null) {
                cell.setCellStyle(cellStyle);
            }
            cell.setCellValue(titles[i]);
        }
    }


    public static <T> void setWorkbookDataByBean(Sheet sheet, List<T> dataList, String[] fields, CellStyle cellStyle) throws Exception {
        if (dataList != null) {
            int line = ExcelConstants.DEFAUT_HEADER_SHOW_LINE + 1;
            for (T bean : dataList) {
                Row dataRow = sheet.createRow(line++);
                int col = 0;
                for (int i = 0; i < fields.length; i++) {
                    Cell cell = dataRow.createCell(col++);
                    if (cellStyle != null) {
                        cell.setCellStyle(cellStyle);
                    }
                    String value = BeanUtilsBean2.getInstance().getProperty(bean, fields[i]);
                    cell.setCellValue(value);
                }
            }
        }
    }


    /**
     * 设置工作表数据
     *
     * @param sheet
     * @param dataList
     * @param fields
     * @param cellStyle
     */
    public static void setWorkbookData(Sheet sheet, List<Map<String, Object>> dataList, String[] fields, CellStyle cellStyle) {
        if (dataList != null) {
            int line = ExcelConstants.DEFAUT_HEADER_SHOW_LINE + 1;
            for (Map<String, Object> data : dataList) {
                Row dataRow = sheet.createRow(line++);
                int col = 0;
                for (int i = 0; i < fields.length; i++) {
                    Cell cell = dataRow.createCell(col++);
                    if (cellStyle != null) {
                        cell.setCellStyle(cellStyle);
                    }
                    String value = getValue(data.get(fields[i]));
                    cell.setCellValue(value);
                }
            }
        }
    }

    private static String getValue(Object o) {
        return (o == null) ? "" : o.toString();
    }

    /**
     * 设置标题样式
     *
     * @param workbook
     * @param isFull   : 是否填充单元格
     * @return
     */
    public static CellStyle setCellStyle(Workbook workbook, boolean isFull) {
        CellStyle cellstyle = workbook.createCellStyle();
        if (isFull) {
            cellstyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // 填充单元格
            cellstyle.setFillForegroundColor(AQUA.index); // 填绿色
        }
        cellstyle.setBorderBottom(BorderStyle.THIN);
        cellstyle.setBottomBorderColor((short) 0);
        cellstyle.setBorderLeft(BorderStyle.THIN);
        cellstyle.setLeftBorderColor((short) 0);
        cellstyle.setBorderRight(BorderStyle.THIN);
        cellstyle.setRightBorderColor((short) 0);
        cellstyle.setBorderTop(BorderStyle.THIN);
        cellstyle.setTopBorderColor((short) 0);
        cellstyle.setAlignment(HorizontalAlignment.CENTER); // 居中显示
        return cellstyle;
    }

}
