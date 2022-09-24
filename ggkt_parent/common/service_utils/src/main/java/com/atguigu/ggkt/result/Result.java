package com.atguigu.ggkt.result;

import lombok.Data;

/**
 * @author Tianjinfei
 * @Version 1.0
 * 返回统一Json格式数据
 */
@Data
public class Result <T>{
    private Integer code;//状态码
    private String message;//返回信息
    private T data;//返回的数据

    public Result() {
    }
    //成功的方法,没有数据
    /*public static<T> Result<T> ok(){
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("成功");
        return result;
    }*/
    //失败的方法,没有数据
    /*public static<T> Result<T> fail(){
        Result<T> result = new Result<>();
        result.setCode(201);
        result.setMessage("失败");
        return result;
    }*/
    //成功的方法,有数据
    public static<T> Result<T> ok(T data){
        Result<T> result = new Result<>();
        if (data != null){
            result.setData(data);
        }
        result.setCode(20000);
        result.setMessage("成功");
        return result;
    }
    //失败的方法,有数据
    public static<T> Result<T> fail(T data){
        Result<T> result = new Result<>();
        if (data != null) {
            result.setData(data);
        }
        result.setCode(20001);
        result.setMessage("失败");
        return result;
    }
    //自定义状态码
    public Result<T> code(Integer code){

        this.code = code;

        return this;
    }
    //自定义信息
    public Result<T> message(String message){

        this.message = message;

        return this;
    }

}
