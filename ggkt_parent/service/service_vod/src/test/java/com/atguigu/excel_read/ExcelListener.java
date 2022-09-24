package com.atguigu.excel_read;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Tianjinfei
 * @Version 1.0
 */
public class ExcelListener extends AnalysisEventListener<City> {
    //读取excel表,一行一行的读取,从第二行开始读取,因为第一行是表头
    @Override
    public void invoke(City city, AnalysisContext analysisContext) {
        System.out.println(city);
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头: "+headMap);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
