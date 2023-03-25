package com.iv.ersr.common.controller;

import com.iv.ersr.common.entity.Response;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liweimin
 * @since 2019-07-09
 */
@Controller
public abstract class BaseController {

    @Resource
    public HttpServletRequest request;

    @Resource
    public HttpServletResponse response;

    public Response returnFailMsg(String msg){
        return Response.createFailResponse(msg);
    }
}
