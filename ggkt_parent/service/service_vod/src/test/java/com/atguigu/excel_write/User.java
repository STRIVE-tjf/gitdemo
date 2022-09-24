package com.atguigu.excel_write;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author Tianjinfei
 * @Version 1.0
 */
@Data
public class User {
    @ExcelProperty(value = "用户编号")
    private Integer id;
    @ExcelProperty(value = "用户姓名")
    private String name;
}
