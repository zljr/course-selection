package cn.edu.zuel.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    public static final String CODE_SUCCESS="200";
    public static final String CODE_AUTH_ERROR="401";
    public static final String CODE_SYS_ERROR="500";

    /*
    * 封装返回前端数据格式
    * */
    private String code;
    private String msg;
    private Object data;


    public static Result success(Object data){
        return new Result(CODE_SUCCESS,"请求成功",data);
    }
    public static Result success(){
        return new Result(CODE_SUCCESS,"请求成功",null);
    }
    public static Result error(String msg){
        return new Result(CODE_SYS_ERROR,msg,null);
    }
    public static Result error(String code,String msg){
        return new Result(code,msg,null);
    }
    public static Result error(){
        return new Result(CODE_SYS_ERROR,"系统错误",null);
    }
}
