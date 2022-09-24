package com.atguigu.excel_read;

import com.alibaba.excel.EasyExcel;

/**
 * @author Tianjinfei
 * @Version 1.0
 */
public class EasyExcelReadTest {
    public static void main(String[] args) {
        //需要读取的文件路径
        String fileName="D:\\2017年中国主要城市房价工资比排行榜.xlsx";
        EasyExcel.read(fileName,City.class,new ExcelListener()).sheet().doRead();//sheet默认读取第一个工作表
    }

}
