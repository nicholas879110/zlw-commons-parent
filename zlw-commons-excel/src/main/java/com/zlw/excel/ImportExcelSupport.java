package com.zlw.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.zlw.excel.exception.ExcelImportException;
import org.apache.commons.beanutils.BeanUtilsBean2;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 导入Excel 即是将excel表格中按指定格式存放的数据转换成List，目前紧支持xls格式，xlsx格式暂不支持
 *
 * @author fukui
 */
public abstract class ImportExcelSupport {


    private static Logger logger = LoggerFactory.getLogger(ImportExcelSupport.class);


    /**
     * 将指定excel表格数据转换成指定List
     *
     * @param filePath     excel文件输入File对象
     * @param entityClass  需要转换的目标Class对象
     * @param filedList    目标对象包含的属性，【注意】：list中的属性顺序（包含数据类型）需要excel表格中的一致
     * @param startRowNum  需要导入excel数据 开始行号，如需要从第三行开始导入，则startRowNum=2
     * @param startCollNum 需要导入excel数据 开始列号，如需要从第二列开始导入，则startCollNum=1
     * @return Lst<T>
     * @throws Exception
     */
    public static <T> List<T> excelToList(
            String filePath,
            Class<T> entityClass,
            List<String> filedList,
            int startRowNum,
            int startCollNum) throws Exception {
        logger.info("开始从文件 : {} 导入数据", filePath);
        File file = new File(filePath);
        if (!file.exists()) {
            throw new ExcelImportException("指定文件 : {} 不存在", filePath);
        }
        return excelToList(file, entityClass, filedList, startRowNum, startCollNum);
    }

    /**
     * 将指定excel表格数据转换成指定List
     *
     * @param file         excel文件输入File对象
     * @param entityClass  需要转换的目标Class对象
     * @param filedList    目标对象包含的属性，【注意】：list中的属性顺序（包含数据类型）需要excel表格中的一致
     * @param startRowNum  需要导入excel数据 开始行号，如需要从第三行开始导入，则startRowNum=2
     * @param startCollNum 需要导入excel数据 开始列号，如需要从第二列开始导入，则startCollNum=1
     * @return Lst<T>
     * @throws Exception
     */
    public static <T> List<T> excelToList(
            File file,
            Class<T> entityClass,
            List<String> filedList,
            int startRowNum,
            int startCollNum) throws Exception {

        String fileName = file.getName();
        if (!fileName.endsWith(ExcelVersion.XLS) && !fileName.endsWith(ExcelVersion.XLSX)) {
            logger.error(fileName + "不是excel文件");
            throw new IOException(fileName + "不是excel文件");
        }
        String version = ExcelVersion.getVersion(fileName);
        InputStream in = new FileInputStream(file);
        return excelToList(version, in, entityClass, filedList, startRowNum, startCollNum);
    }


    /**
     * 将指定excel表格数据转换成指定List
     *
     * @param in           excel文件输入流
     * @param version      excel版本 2007/2011
     * @param entityClass  需要转换的目标Class对象
     * @param filedList    目标对象包含的属性，【注意】：list中的属性顺序（包含数据类型）需要excel表格中的一致
     * @param startRowNum  需要导入excel数据 开始行号，如需要从第三行开始导入，则startRowNum=2
     * @param startCollNum 需要导入excel数据 开始列号，如需要从第二列开始导入，则startCollNum=1
     * @return Lst<T>
     * @throws Exception
     */
    @SuppressWarnings({"resource", "rawtypes", "unchecked"})
    public static <T> List<T> excelToList(String version, InputStream in,
                                          Class<T> entityClass,
                                          List<String> filedList,
                                          int startRowNum,
                                          int startCollNum) throws Exception {
        Workbook workbook = null;
        try {
            if (version.equalsIgnoreCase(ExcelVersion.EXCEL2003)) {
                workbook = new HSSFWorkbook(in);
            } else if (version.equalsIgnoreCase(ExcelVersion.EXCEL2007)) {
                workbook = new XSSFWorkbook(in);
            } else {
                throw new RuntimeException("poi can not support");
            }
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        int excelSheetsNumber = workbook.getNumberOfSheets();
        if (excelSheetsNumber <= 0) {
            return new ArrayList();
        }
        List<T> list = new ArrayList();
        for (int sheet = 0; sheet < excelSheetsNumber; sheet++) {
            Sheet sheetAt = workbook.getSheetAt(sheet);
            if (sheetAt == null) {
                logger.warn("导入的excel 第【{}】sheet 为空", sheet);
                continue;
            }
            for (int rowNum = startRowNum; rowNum <= sheetAt.getLastRowNum(); rowNum++) {
                Row row = sheetAt.getRow(rowNum);
                if (row == null) {
                    logger.warn("导入的excel 第【{}】sheet 的【{}】行为空", sheet, rowNum);
                    continue;
                }
                list.add(rowToObject(row, entityClass, filedList, startCollNum));
            }
        }
        return list;
    }

    private static <T> T rowToObject(Row row, Class<T> entityClass, List<String> filedList, int startCollNum) throws InstantiationException, IllegalAccessException,
            InvocationTargetException {
        T bean = entityClass.newInstance();
        for (int cellNum = startCollNum; cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell == null) {
                continue;
            }
            // 与excel中列对应的Javabean属性
            String filed = filedList.get(cellNum - startCollNum);
            Object value = getValue(cell);
            logger.debug("  cell [ {} ] value : {} ", cellNum, value);
            BeanUtilsBean2.getInstance().setProperty(bean, filed, value);
        }
        return bean;
    }

    @SuppressWarnings("static-access")
    private static Object getValue(Cell cell) {
        if (cell.getCellType() == CellType.BOOLEAN) {
            return cell.getBooleanCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.BLANK) {
            return StringUtils.EMPTY;
        } else {
            return cell.getStringCellValue();
        }
    }

}
