package com.qingyu.mo.controller;

import com.qingyu.mo.entity.Response;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liweimin
 * @since 2019-07-09
 */
@Controller
public class BaseController {

    @Resource
    public HttpServletRequest request;

    @Resource
    public HttpServletResponse response;

    public Response returnFailMsg(String msg){
        return Response.createFailResponse(msg);
    }
}