package com.bjpowernode.p2p.util;

import java.util.HashMap;

/**
 * 张新宇
 * 2020/7/29
 * 相应结果对象
 */
public class Result extends HashMap<String,Object> {

    public static Result success(){
        Result result=new Result();
        result.put("code", 1);
        result.put("success", true);
        return result;
    }

    public static Result error(String message){
        Result result=new Result();
        result.put("code", -1);
        result.put("message", message);
        result.put("success", false);
        return result;
    }

    public static Result success(String data) {
        Result result=new Result();
        result.put("code", 1);
        result.put("data", data);
        result.put("success", true);
        return result;
    }
}
