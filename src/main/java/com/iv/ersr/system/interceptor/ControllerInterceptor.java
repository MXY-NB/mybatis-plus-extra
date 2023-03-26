package com.iv.ersr.system.interceptor;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.iv.ersr.common.entity.Response;
import com.iv.ersr.common.exception.HandlerException;
import com.iv.ersr.system.entity.HandleRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

/**
 * <p>
 * Controller AOP 日志
 * </p>
 *
 * @author moxiaoyu
 * @since 2022-02-28
 */
@Slf4j
@Aspect
@Component
public class ControllerInterceptor {

    private static final String USER_AGENT = "User-Agent";
    private static final String ORIGIN = "Origin";
    private static final String UNKNOWN = "unknown";

    /**
     * 日志输出
     * @param pjp 切点
     * @return Object
     */
    @Around("execution(* com.iv.ersr.*.controller.*.*(..)))and @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public Object interceptor(ProceedingJoinPoint pjp) {
        long beginTime = System.currentTimeMillis();
        HandleRequest handleRequest;
        Object result = null;
        StrBuilder sb = new StrBuilder();
        try {
            ServletRequestAttributes sa = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
            assert sa != null;
            HttpServletRequest request = sa.getRequest();
            handleRequest = handleRequest(request);

            sb.append("request start:").append(handleRequest.getUri());

            // 处理访问ip
            handleIpAddress(request, sb, handleRequest);

            // 获取请求参数
            Object[] args = pjp.getArgs();
            requestParamsHandle(args, sb, handleRequest);

            //获取返回结果
            result = pjp.proceed();
            return result;
        } catch (HandlerException e) {
            return Response.createFailResponse(e.getMessage());
        } catch (InvocationTargetException e) {
            String errMsg = e.getTargetException() != null ? e.getTargetException().getMessage() : e.getMessage();
            if(!(e.getTargetException() instanceof HandlerException)){
                log.error(ExceptionUtil.stacktraceToString(e.getTargetException()));
            }
            return Response.createFailResponse(errMsg);
        } catch(Throwable e) {
            log.error(ExceptionUtil.stacktraceToString(e));
            return Response.createFailResponse();
        } finally {
            long endTime = System.currentTimeMillis();
            sb.append("\nresponseParams:");
            try {
                sb.append(ObjectUtil.defaultIfNull(JSONUtil.toJsonStr(result), ""));
            } catch (Exception e){
                e.printStackTrace();
            }
            sb.append("\n").append("waste:").append(endTime - beginTime);
            log.info(sb.toString());
        }
    }

    /**
     * 处理请求数据
     * @param request 请求
     * @return HandleRequest 返回对象
     */
    private HandleRequest handleRequest(HttpServletRequest request){
        String uri = request.getRequestURI().substring(request.getRequestURI().indexOf(request.getContextPath()) + request.getContextPath().length());
        UserAgent ua = UserAgentUtil.parse(request.getHeader(USER_AGENT));
        return HandleRequest.builder()
                .uri(uri)
                .operateTime(LocalDateTime.now())
                .origin(request.getHeader(ORIGIN))
                .browser(ua.getBrowser().toString())
                .platform(ua.getPlatform().toString())
                .system(ua.getOs().toString())
                .build();
    }

    /**
     * 获取访问ip
     * @param request 请求数据
     * @param sb 拼接参数
     * @param handleRequest 请求数据
     */
    private void handleIpAddress(HttpServletRequest request, StrBuilder sb, HandleRequest handleRequest) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 获取访问ip
        handleRequest.setSourceIp(ip);
        sb.append(",").append(handleRequest.getSourceIp());
    }

    /**
     * 获取请求参数
     * @param args 传入参数1
     * @param sb 拼接参数
     * @param handleRequest 请求数据
     * @author MXY
     * @date 2022/04/19
     */
    private void requestParamsHandle(Object[] args, StrBuilder sb, HandleRequest handleRequest) {
        StrBuilder requestParams = new StrBuilder("\n","requestParams:");
        if (args != null && args.length >= 1) {
            if ((args[0] instanceof MultipartFile)) {
                MultipartFile mf = (MultipartFile) args[0];
                requestParams.append(mf.getOriginalFilename()).append("--").append(mf.getSize());
            } else if (args[0] instanceof MultipartFile[]) {
                MultipartFile[] mfs = (MultipartFile[]) args[0];
                for (MultipartFile multipartFile : mfs) {
                    requestParams.append(multipartFile.getOriginalFilename()).append("--").append(multipartFile.getSize());
                }
            } else {
                requestParams.append(JSON.toJSONString(args[0]));
            }
        }
        handleRequest.setRequestParams(requestParams.toString());
        sb.append(requestParams).append("\n");
    }
}

