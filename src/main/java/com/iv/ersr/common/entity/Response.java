package com.iv.ersr.common.entity;

import cn.hutool.core.util.ObjectUtil;
import com.iv.ersr.common.entity.enums.Result;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 自定义返回结果对象
 * </p>
 *
 * @author moxiaoyu
 * @since 2021-12-24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    /**
     * 响应码
     */
    private Integer resultCode;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应数据
     */
    private Object data;

    public static Response createResponse(Result result){
        return Response.createResponse(result, result.toString(), null);
    }

    public static Response createResponse(Result result, String message){
        return Response.createResponse(result, message, null);
    }

    public static Response createResponse(Result result, Object data){
        return Response.createResponse(result, result.toString(), data);
    }

    public static Response createResponse(Result result, String message, Object data) {
        return Response.builder().resultCode(result.ordinal()).message(ObjectUtil.isNull(message) ? "system error" : message).data(data).build();
    }

    public static Response createFailResponse(){
        return Response.createResponse(Result.FAIL, null, null);
    }

    public static Response createSuccResponse(){
        return Response.createResponse(Result.SUCCESS, Result.SUCCESS.toString(), null);
    }

    public static Response createFailResponse(String message){
        return Response.createResponse(Result.FAIL, message, null);
    }

    public static Response createSuccResponse(String message){
        return Response.createResponse(Result.SUCCESS, message, null);
    }

    public static Response createFailResponse(Object data){
        return Response.createResponse(Result.FAIL, Result.FAIL.toString(), data);
    }

    public static Response createSuccResponse(Object data){
        return Response.createResponse(Result.SUCCESS, Result.SUCCESS.toString(), data);
    }

    public static Response createFailResponse(String message, Object data){
        return Response.createResponse(Result.FAIL, message, data);
    }

    public static Response createSuccResponse(String message, Object data){
        return Response.createResponse(Result.SUCCESS, message, data);
    }

    public boolean succ(){
        return resultCode == Result.SUCCESS.ordinal();
    }
}
