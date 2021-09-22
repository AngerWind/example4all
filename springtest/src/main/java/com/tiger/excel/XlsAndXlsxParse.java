package com.tiger.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title XlsAndXlsxPaser
 * @date 2021/9/8 18:26
 * @description
 */
public class XlsAndXlsxParse {

    public void test() {

        String excelPath = "C:\\Users\\Tiger.Shen\\Desktop\\tableconvert_excel_s1axw3.xlsx";

        try {
            //String encoding = "GBK";
            File excel = new File(excelPath);
            if (excel.isFile() && excel.exists()) {

                //.是特殊字符，需要转义！！！！！
                String[] split = excel.getName().split("\\.");
                Workbook wb;
                //根据文件后缀（xls/xlsx）进行判断
                try (FileInputStream fis = new FileInputStream(excel);) {
                    if ("xls".equals(split[1])) {
                        wb = new HSSFWorkbook(fis);
                    } else if ("xlsx".equals(split[1])) {
                        wb = new XSSFWorkbook(fis);
                    } else {
                        System.out.println("文件类型错误!");
                        return;
                    }
                }

                //读取sheet 0
                Sheet sheet = wb.getSheetAt(0);

                // 获取行数
                int firstRowIndex = sheet.getFirstRowNum();
                int lastRowIndex = sheet.getLastRowNum();
                System.out.println("firstRowIndex: " + firstRowIndex);
                System.out.println("lastRowIndex: " + lastRowIndex);

                // 遍历行
                for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {
                    System.out.println("rIndex: " + rIndex);
                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        int firstCellIndex = row.getFirstCellNum();
                        int lastCellIndex = row.getLastCellNum();
                        // 遍历列
                        for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {
                            Cell cell = row.getCell(cIndex);
                            if (cell != null) {
                                System.out.println(cell.toString());
                            }
                        }
                    }
                }
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
