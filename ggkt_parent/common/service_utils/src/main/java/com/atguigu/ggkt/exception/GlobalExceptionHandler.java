package com.atguigu.ggkt.exception;

import com.atguigu.ggkt.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Tianjinfei
 * @Version 1.0
 * 全局异常处理器
 */
@ControllerAdvice//底层运用Aop的思想(在不改变原代码的情况下新功能或对以前的功能进行增强)
public class GlobalExceptionHandler {
    //全局异常
    @ExceptionHandler(Exception.class)//对所有异常进行处理
    @ResponseBody
    public Result error(Exception e){
        System.out.println("全局....");
        e.printStackTrace();
        return Result.fail(null).message("执行了全局异常处理");
    }
    //特定异常
    @ResponseBody
    @ExceptionHandler(ArithmeticException.class)
    public Result error(ArithmeticException e){
        System.out.println("特定......");
        e.printStackTrace();
        return Result.fail(null).message("执行了特定异常");
    }

    //自定义异常
    @ResponseBody
    @ExceptionHandler(GgktException.class)
    public Result error(GgktException e){
        e.printStackTrace();
        return Result.fail(null).code(e.getCode()).message(e.getMessage());
    }
}
