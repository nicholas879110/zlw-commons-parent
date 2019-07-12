package com.zlw.excel;

import java.io.File;

public class ExcelVersion {
    public static final String EXCEL2003 = "2003";
    public static final String EXCEL2007 = "2007";

    public final static String XLS = "xls";
    public final static String XLSX = "xlsx";

    public static String getVersion(String filePath) {
        String version = "";
        if (filePath.endsWith(ExcelVersion.XLS)) {
            version = ExcelVersion.EXCEL2003;
        } else if (filePath.endsWith(ExcelVersion.XLSX)) {
            version = ExcelVersion.EXCEL2007;
        } else {
            throw new RuntimeException("poi not support！");
        }
        return version;
    }

    public static String getVersion(File file) {
        String version = getVersion(file.getName());
        return version;
    }


}
