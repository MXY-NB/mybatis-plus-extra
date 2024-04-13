package com.qingyu.mo.util;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.crypto.SecureUtil;
import com.qingyu.mo.constant.Constant;
import com.qingyu.mo.entity.InnerCheckSign;
import com.qingyu.mo.entity.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;

/**
 * <p>
 * md5签名
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
@Slf4j
public class SignUtil {

    private SignUtil(){}

    /**
     * 验签
     * @param key key
     * @param timestamp 时间戳
     * @param version 版本
     * @param oldSign 前端传值
     * @return Response
     */
    public static Response checkSign(String key, String timestamp, String version, String oldSign, InnerCheckSign innerCheckSign) {
        log.debug("请求签名数据: {},{},{},{}", key, timestamp, version, oldSign);

        if (CharSequenceUtil.isEmpty(key)) {
            return Response.createFailResponse("key is empty!");
        }
        if (CharSequenceUtil.isEmpty(timestamp)) {
            return Response.createFailResponse("timestamp is empty!");
        }
        if (CharSequenceUtil.isEmpty(version)) {
            return Response.createFailResponse("version is empty!");
        }


        if (!innerCheckSign.getVersion().equals(version)) {
            return Response.createFailResponse("wrong version!");
        }

        Long now = System.currentTimeMillis();
        Long req = Long.parseLong(timestamp);

        if ((now - req) / 1000 > innerCheckSign.getSecond()) {
            return Response.createFailResponse("wrong timestamp!");
        }

        String newSign = getSign(innerCheckSign.getKey(), timestamp, innerCheckSign.getVersion(), innerCheckSign.getSecret());
        if(CharSequenceUtil.isEmpty(newSign)){
            return Response.createFailResponse("sign is empty!");
        }
        if (!newSign.equalsIgnoreCase(oldSign)) {
            return Response.createFailResponse("wrong sign");
        }

        return Response.createSuccResponse();
    }

    /**
     * 获取MD5加密签名
     * @param key key
     * @param timestamp 时间戳
     * @param version 版本
     * @param secret 密钥
     * @return String MD5加密签名
     */
    public static String getSign(String key, String timestamp, String version, String secret) {
        StrBuilder signStr = new StrBuilder();
        signStr.append(Constant.INNER_KEY_NAME).append("=").append(key).append("&");
        signStr.append(Constant.INNER_TIMESTAMP_NAME).append("=").append(timestamp).append("&");
        signStr.append(Constant.INNER_VERSION_NAME).append("=").append(version).append("&");
        signStr.append(Constant.INNER_SECRET_NAME).append("=").append(secret);

        log.debug("signStr:{}", signStr);
        String md5Str = SecureUtil.md5(signStr.toString());
        if (md5Str != null) {
            md5Str = md5Str.toLowerCase(Locale.ROOT);
        }
        return md5Str;
    }
}