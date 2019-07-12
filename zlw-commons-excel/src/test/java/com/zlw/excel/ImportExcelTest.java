package com.zlw.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;


public class ImportExcelTest {

    public static void main(String[] args) throws Exception {
        //导入  excel --->List
        List<JavaBean> list = importExcel();

        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).toString());
        }
        String[] titles = {"ID", "员工编号", "员工账号", "公司名称", "电话号码", "创建时间", "顺序", "费用"};
        String[] fields = {"id", "empNo", "account", "company", "phone", "createTime", "order", "dub"};

        //导出 List --->HSSFWorkbook
        Workbook book = ExportExcelSupport.exportByListBean(ExcelVersion.EXCEL2007, titles, list, fields);
        FileOutputStream output = new FileOutputStream("F://workbook.xls");
        book.write(output);
        output.flush();
    }

    private static List<JavaBean> importExcel() throws FileNotFoundException, Exception {
        File file = new File("F://template.xls");
        InputStream in = new FileInputStream(file);
        List<String> filedList = new ArrayList<String>();
        filedList.add("id");
        filedList.add("empNo");
        filedList.add("account");
        filedList.add("company");
        filedList.add("phone");
        filedList.add("createTime");
        filedList.add("order");
        filedList.add("dub");
        return ImportExcelSupport.excelToList(ExcelVersion.EXCEL2007, in, JavaBean.class, filedList, 2, 1);

    }


}
