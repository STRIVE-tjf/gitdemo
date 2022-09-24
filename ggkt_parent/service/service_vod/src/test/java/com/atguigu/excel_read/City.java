package com.atguigu.excel_read;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author Tianjinfei
 * @Version 1.0
 */
@Data
public class City {
    @ExcelProperty(value = "城市",index = 0)
    private String cityName;
    @ExcelProperty(value = "平均房价",index = 1)
    private String averageHousePrice;
    @ExcelProperty(value = "平均工资",index = 2)
    private String averageSalary;
    @ExcelProperty(value = "房价工资比",index = 3)
    private float housePriceSalaryRatio;

}
