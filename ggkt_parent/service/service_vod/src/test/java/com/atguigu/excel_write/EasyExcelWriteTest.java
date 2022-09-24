package com.atguigu.excel_write;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tianjinfei
 * @Version 1.0
 */
public class EasyExcelWriteTest {
    public static void main(String[] args) {
        String fileName = "D:\\atguigu.xlsx";
        EasyExcel.write(fileName,User.class)
                .sheet("用户表")
                .doWrite(data());

    }
    public static List<User> data(){
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setId(i);
            user.setName("Tom"+i);
            list.add(user);
        }
        return list;
    }
}
