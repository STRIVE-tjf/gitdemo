package com.atguigu;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * @Description:
 * @Author: Hanzao
 * @Date: 2022/03/24/22:21
 */
public class FastGenerator {
    public static void main(String[] args) {
        String url= "jdbc:mysql://localhost:3306/glkt_live?useSSL=true&useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8";
        String username = "root";
        String password = "1916820112";
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("tianjf") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("D:\\IJ-projects\\ggkt_parent\\service\\service_live\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.atguigu.ggkt") // 设置父包名
                            .moduleName("live") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "D:\\IJ-projects\\ggkt_parent\\service\\service_live\\src\\main\\java\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("live_course","live_course_account","live_course_config","live_course_description","live_course_goods","live_visitor") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
