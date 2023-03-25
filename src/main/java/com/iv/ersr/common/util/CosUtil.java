package com.iv.ersr.common.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.iv.ersr.common.entity.InnerFile;
import com.iv.ersr.common.entity.TencentCosKey;
import com.iv.ersr.common.entity.enums.FileBusinessTypeEnum;
import com.iv.ersr.common.entity.enums.FileExtensionEnum;
import com.iv.ersr.common.entity.enums.FileTypeEnum;
import com.iv.ersr.common.exception.HandlerException;
import com.iv.ersr.system.entity.Constant;
import com.iv.ersr.system.entity.SystemSetting;
import com.iv.ersr.system.service.ISystemSettingService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
* <p>
* 腾讯云对象存储工具类
* </p>
*
* @author IVI04
* @since 2022-03-28
*/
@Slf4j
@Component
public class CosUtil {

    @Autowired
    private ISystemSettingService systemSettingService;

    private static String bucket;

    private static String baseUrl;

    private static String secretId;

    private static String secretKey;

    private static String region;

    private static COSClient cosClient;

    private static final String NO_CACHE = "no-cache";

    private CosUtil() {}

    private static final String MSG_1 = "COS客户端错误，可能原因：未连接上COS客户端！";
    private static final String MSG_2 = "COS其他错误，可能原因：参数错误/身份验证不通过等，请排查错误！";
    private static final String MSG_3 = "上传文件传输IO错误！";

    /**
     * 生成cos客户端
     */
    @PostConstruct
    public void init() {
        // 获取配置表中的参数
        SystemSetting tencentSetting = systemSettingService.getSettingByKey(Constant.TENCENT_COS_KEY, false);
        if (BeanUtil.isEmpty(tencentSetting)) {
            throw new HandlerException("腾讯COS配置参数不存在！");
        }
        TencentCosKey tencentCosKey = JSON.parseObject(tencentSetting.getSettingValue(), TencentCosKey.class);
        if (BeanUtil.hasNullField(tencentCosKey)) {
            throw new HandlerException("腾讯COS配置参数不完整！");
        }

        // 1 密钥 SecretId
        secretId = tencentCosKey.getSecretId();

        // 2 密钥 SecretKey
        secretKey = tencentCosKey.getSecretKey();

        // 3 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);

        // 4 设置地域
        region = tencentCosKey.getRegion();

        // 5 设置桶
        bucket = tencentCosKey.getBucketName();

        // 6 设置默认域名访问url
        baseUrl = tencentCosKey.getBaseUrl();

        // 7 设置bucket的区域, COS地域的简称请参照 https://www.qcloud.com/document/product/436/6224
        ClientConfig clientconfig = new ClientConfig(new Region(region));

        // 8 生成cos客户端
        cosClient = new COSClient(cred, clientconfig);
    }

    /**
     * 获取COS对象访问 URL
     * @return String – URL
     */
    public static String getCosUrl() {
        return baseUrl;
    }

    /**
     * 文件上传
     * @param file – 上传的文件
     * @param fileBusinessType – 文件业务类型
     * @param fileType – 文件类型
     * @return InnerFile – 自定义文件实体
     */
    public static InnerFile uploadFile(MultipartFile file, FileBusinessTypeEnum fileBusinessType, FileTypeEnum fileType) {
        InnerFile innerFile;
        if (ObjectUtil.isNull(file)) {
            return new InnerFile();
        }
        // 校验文件名
        FileUtil.checkFileName(file, fileType);
        try {
            // 校验文件内容
            FileUtil.checkFileContent(file, fileType);
            innerFile = FileUtil.createNewFile(file.getOriginalFilename(), fileBusinessType, false);
            putObject(file, innerFile);
        } catch (IOException e) {
            log.error(MSG_3);
            log.error(ExceptionUtil.getSimpleMessage(e));
            throw new HandlerException(MSG_3);
        } catch (CosServiceException e) { // 如果是其他错误, 比如参数错误， 身份验证不过等会抛出CosServiceException
            log.error(MSG_2);
            log.error(ExceptionUtil.getSimpleMessage(e));
            throw new HandlerException(MSG_2);
        } catch (CosClientException e) { // 如果是客户端错误，比如连接不上COS
            log.error(MSG_1);
            log.error(ExceptionUtil.getSimpleMessage(e));
            throw new HandlerException(MSG_1);
        } catch (Exception e) {
            log.error(ExceptionUtil.getSimpleMessage(e));
            throw new HandlerException(ExceptionUtil.getSimpleMessage(e));
        }
        return innerFile;
    }

    /**
     * 文件上传 【不校验数据】
     * @param file – 上传的文件
     * @param fileBusinessType – 文件业务类型
     * @return InnerFile – 自定义文件实体
     */
    public static InnerFile uploadFileNoCheck(MultipartFile file, FileBusinessTypeEnum fileBusinessType) {
        InnerFile innerFile;
        if (ObjectUtil.isNull(file)) {
            return new InnerFile();
        }
        try {
            innerFile = FileUtil.createNewFile(file.getOriginalFilename(), fileBusinessType, false);
            putObject(file, innerFile);
        } catch (IOException e) {
            log.error(MSG_3);
            log.error(ExceptionUtil.getSimpleMessage(e));
            throw new HandlerException(MSG_3);
        } catch (CosServiceException e) { // 如果是其他错误, 比如参数错误， 身份验证不过等会抛出CosServiceException
            log.error(MSG_2);
            log.error(ExceptionUtil.getSimpleMessage(e));
            throw new HandlerException(MSG_2);
        } catch (CosClientException e) { // 如果是客户端错误，比如连接不上COS
            log.error(MSG_1);
            log.error(ExceptionUtil.getSimpleMessage(e));
            throw new HandlerException(MSG_1);
        } catch (Exception e) {
            log.error(ExceptionUtil.getSimpleMessage(e));
            throw new HandlerException(ExceptionUtil.getSimpleMessage(e));
        }
        return innerFile;
    }

    /**
     * 文件批量上传
     * @param files – 上传的文件数组
     * @param fileBusinessType – 文件业务类型
     * @param fileType – 文件类型
     * @return List<InnerFile>
     */
    public static List<InnerFile> batchUploadFile(MultipartFile[] files, FileBusinessTypeEnum fileBusinessType, FileTypeEnum fileType) {
        List<InnerFile> innerFiles = new ArrayList<>();
        if (files != null && files.length != 0) {
            InnerFile innerFile;
            try {
                for (MultipartFile file : files) {
                    // 校验文件数据
                    FileUtil.checkFileData(file, fileType);
                    innerFile = FileUtil.createNewFile(file.getOriginalFilename(), fileBusinessType, false);
                    putObject(file, innerFile);
                    innerFiles.add(innerFile);
                }
            } catch (IOException e) {
                log.error(MSG_3);
                log.error(ExceptionUtil.getSimpleMessage(e));
                throw new HandlerException(MSG_3);
            } catch (CosServiceException e) { // 如果是其他错误, 比如参数错误， 身份验证不过等会抛出CosServiceException
                log.error(MSG_2);
                log.error(ExceptionUtil.getSimpleMessage(e));
                throw new HandlerException(MSG_2);
            } catch (CosClientException e) { // 如果是客户端错误，比如连接不上COS
                log.error(MSG_1);
                log.error(ExceptionUtil.getSimpleMessage(e));
                throw new HandlerException(MSG_1);
            } catch (Exception e) {
                log.error(ExceptionUtil.getSimpleMessage(e));
                throw new HandlerException(ExceptionUtil.getSimpleMessage(e));
            }
        }
        return innerFiles;
    }

    /**
     * 创建文件及其父目录
     * @param innerFile – 文件名和文件路径
     */
    public static void touch(InnerFile innerFile) {
        write(null, innerFile);
    }

    /**
     * 写文件
     * @param content – 内容
     * @param innerFile – 文件名和文件路径
     */
    public static void write(String content, InnerFile innerFile) {
        byte[] data;
        try {
            if (CharSequenceUtil.isBlank(content)) {
                data = new byte[0];
            } else {
                data = CharSequenceUtil.utf8Bytes(content);
            }
            @Cleanup
            InputStream inputStream = new ByteArrayInputStream(data);
            putObject(inputStream, innerFile);
        } catch (IOException e) {
            log.error(MSG_3);
            log.error(ExceptionUtil.getSimpleMessage(e));
            throw new HandlerException(MSG_3);
        } catch (CosServiceException e) { // 如果是其他错误, 比如参数错误， 身份验证不过等会抛出CosServiceException
            log.error(MSG_2);
            log.error(ExceptionUtil.getSimpleMessage(e));
            throw new HandlerException(MSG_2);
        } catch (CosClientException e) { // 如果是客户端错误，比如连接不上COS
            log.error(MSG_1);
            log.error(ExceptionUtil.getSimpleMessage(e));
            throw new HandlerException(MSG_1);
        } catch (Exception e) {
            log.error(ExceptionUtil.getSimpleMessage(e));
            throw new HandlerException(ExceptionUtil.getSimpleMessage(e));
        }
    }

    /**
     * 文件以流形式下载
     * @param fileUrl – 文件链接
     * @param response – 响应
     */
    public static void downloadFile(String fileUrl, HttpServletResponse response) {
        if (!cosClient.doesObjectExist(bucket, fileUrl)) {
            throw new HandlerException("cos文件不存在!");
        }

        // 获取下载输入流
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, fileUrl);
        // 限流使用的单位是 bit/s, 这里设置下载带宽限制为10MB/s
//      getObjectRequest.setTrafficLimit(80*1024*1024);
        COSObject cosObject = cosClient.getObject(getObjectRequest);
        try {
            @Cleanup
            COSObjectInputStream cosObjectInput = cosObject.getObjectContent();
            @Cleanup
            BufferedInputStream br = new BufferedInputStream(cosObjectInput);
            // – 文件下载设置
            response.reset();
            response.setCharacterEncoding(CharsetUtil.UTF_8);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", cosObject.getObjectMetadata().getContentDisposition().replace("inline", "attachment"));
            // 读取文件内容
            @Cleanup
            OutputStream out = response.getOutputStream();
            byte[] buf = new byte[2048];
            int len = 0;
            while ((len = br.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (IOException e) {
            log.error(MSG_3);
            log.error(ExceptionUtil.getSimpleMessage(e));
            throw new HandlerException(MSG_3);
        } catch (CosServiceException e) { // 如果是其他错误, 比如参数错误， 身份验证不过等会抛出CosServiceException
            log.error(MSG_2);
            log.error(ExceptionUtil.getSimpleMessage(e));
            throw new HandlerException(MSG_2);
        } catch (CosClientException e) { // 如果是客户端错误，比如连接不上COS
            log.error(MSG_1);
            log.error(ExceptionUtil.getSimpleMessage(e));
            throw new HandlerException(MSG_1);
        } catch (Exception e) {
            log.error(ExceptionUtil.getSimpleMessage(e));
            throw new HandlerException(ExceptionUtil.getSimpleMessage(e));
        }
    }

    /**
     * 根据绝对路径读取文件内容
     * @param fileUrl – 文件链接
     * @return String – 内容
     */
    public static String readFileByUrl(String fileUrl) {
        StrBuilder strBuilder = StrBuilder.create();
        if (CharSequenceUtil.isNotBlank(fileUrl) && cosClient.doesObjectExist(bucket, fileUrl)) {
            List<String> strings;
            // 获取下载输入流
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, fileUrl);
            COSObject cosObject = cosClient.getObject(getObjectRequest);
            try (
                    COSObjectInputStream cosObjectInput = cosObject.getObjectContent();
                    // 读取文件内容
                    BufferedInputStream br = new BufferedInputStream(cosObjectInput)
            ) {
                strings = IoUtil.readUtf8Lines(br, new ArrayList<>());
            } catch (IOException e) {
                log.error(MSG_3);
                log.error(ExceptionUtil.getSimpleMessage(e));
                throw new HandlerException(MSG_3);
            }
            if (CollUtil.isNotEmpty(strings)) {
                for (String string : strings) {
                    strBuilder.append(string);
                }
            }
        }
        return strBuilder.toString();
    }

    /**
     * 删除单个文件(不带版本号, 即bucket未开启多版本)
     * @param fileUrl – 文件路径
     */
    public static void delFile(String fileUrl) {
        if (CharSequenceUtil.isNotBlank(fileUrl)) {
            log.info("进入CosUtil的delFile方法，fileUrl为：{}",fileUrl);
            try {
                if (cosClient.doesObjectExist(bucket, fileUrl)) {
                    cosClient.deleteObject(bucket, fileUrl);
                }
                log.info("删除文件成功，fileUrl为：{}",fileUrl);
            } catch (CosServiceException e) { // 如果是其他错误, 比如参数错误， 身份验证不过等会抛出CosServiceException
                log.error(MSG_2);
                log.error(ExceptionUtil.getSimpleMessage(e));
                throw new HandlerException(ExceptionUtil.getSimpleMessage(e));
            } catch (CosClientException e) { // 如果是客户端错误，比如连接不上COS
                log.error(MSG_1);
                log.error(ExceptionUtil.getSimpleMessage(e));
                throw new HandlerException(ExceptionUtil.getSimpleMessage(e));
            }
        }
    }

    /**
     * 批量删除文件(不带版本号, 即bucket未开启多版本)
     * @param fileUrlList – 文件路径集合
     */
    public static void batchDelFile(List<String> fileUrlList) {
        if (CollUtil.isNotEmpty(fileUrlList)) {
            log.info("进入CosUtil的batchDelFile方法，fileUrlList为：{}",fileUrlList);
            DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucket);
            // 设置要删除的key列表, 最多一次删除1000个
            ArrayList<DeleteObjectsRequest.KeyVersion> keyList = new ArrayList<>();
            // 传入要删除的文件名
            for (String fileUrl : fileUrlList) {
                if (cosClient.doesObjectExist(bucket, fileUrl)) {
                    keyList.add(new DeleteObjectsRequest.KeyVersion(fileUrl));
                }
            }
            deleteObjectsRequest.setKeys(keyList);

            // 批量删除文件
            try {
                cosClient.deleteObjects(deleteObjectsRequest);
                log.info("删除文件成功，fileUrlList为：{}",fileUrlList);
            } catch (CosServiceException e) { // 如果是其他错误, 比如参数错误， 身份验证不过等会抛出CosServiceException
                log.error(MSG_2);
                log.error(ExceptionUtil.getSimpleMessage(e));
                throw new HandlerException(ExceptionUtil.getSimpleMessage(e));
            } catch (CosClientException e) { // 如果是客户端错误，比如连接不上COS
                log.error(MSG_1);
                log.error(ExceptionUtil.getSimpleMessage(e));
                throw new HandlerException(ExceptionUtil.getSimpleMessage(e));
            }
        }
    }

    /**
     * COS上传文件
     * @param file – 文件
     * @param innerFile – 参数
     * @throws IOException – 异常
     */
    private static void putObject(MultipartFile file, InnerFile innerFile) throws IOException {
        @Cleanup
        InputStream inputStream = file.getInputStream();
        putObject(inputStream, innerFile);
    }

    /**
     * COS上传文件
     * @param inputStream – 文件流
     * @param innerFile – 参数
     * @throws IOException – 异常
     */
    private static void putObject(InputStream inputStream, InnerFile innerFile) throws IOException {
        // 从输入流上传(需提前告知输入流的长度, 否则可能导致 oom)
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 设置输入流长度
        objectMetadata.setContentLength(inputStream.available());
        // 设置 Content type, 默认是 application/octet-stream
        objectMetadata.setContentType(getContentType(FileUtil.getSuffix(innerFile.getName())));
        // 设置不缓存
//        objectMetadata.setCacheControl(NO_CACHE);
//        objectMetadata.setHeader("Pragma", NO_CACHE);
        // inline在线预览,中文乱码已处理,下载文件的时候可以用原来上传的名字
        objectMetadata.setContentDisposition("inline;filename=" + URLEncoder.encode(innerFile.getName(), CharsetUtil.UTF_8));
        cosClient.putObject(bucket, innerFile.getPath(), inputStream, objectMetadata);
    }

    /**
     * 判断COS服务文件上传时文件的contentType
     * @param fileNameExtension – 文件后缀
     * @return String – ContentType
     */
    private static String getContentType(String fileNameExtension) {
        //image/jpg 可以在线预览
        if (fileNameExtension.equalsIgnoreCase(FileExtensionEnum.GIF.getValue())
                || fileNameExtension.equalsIgnoreCase(FileExtensionEnum.JPEG.getValue())
                || fileNameExtension.equalsIgnoreCase(FileExtensionEnum.JPG.getValue())
                || fileNameExtension.equalsIgnoreCase(FileExtensionEnum.PNG.getValue())) {
            return "image/jpg";
        } else if (fileNameExtension.equalsIgnoreCase(FileExtensionEnum.BMP.getValue())) {
            return "image/bmp";
        } else if (fileNameExtension.equalsIgnoreCase(FileExtensionEnum.HTML.getValue())) {
            return "text/html";
        } else if (fileNameExtension.equalsIgnoreCase(FileExtensionEnum.TXT.getValue())) {
            return "text/plain";
        } else if (fileNameExtension.equalsIgnoreCase(FileExtensionEnum.VSD.getValue())) {
            return "application/vnd.visio";
        } else if (fileNameExtension.equalsIgnoreCase(FileExtensionEnum.PPTX.getValue())) {
            return "application/vnd.ms-powerpoint";
        } else if (fileNameExtension.equalsIgnoreCase(FileExtensionEnum.DOC.getValue()) || fileNameExtension.equalsIgnoreCase(FileExtensionEnum.DOCX.getValue())) {
            return "application/msword";
        } else if (fileNameExtension.equalsIgnoreCase(FileExtensionEnum.PDF.getValue())) {
            return "application/pdf";
        } else if (fileNameExtension.equalsIgnoreCase(FileExtensionEnum.PPT.getValue())) {
            return "application/x-ppt";
        } else if (fileNameExtension.equalsIgnoreCase(FileExtensionEnum.XML.getValue())) {
            return "text/xml";
        } else if (fileNameExtension.equalsIgnoreCase(FileExtensionEnum.MP3.getValue())) {
            return "audio/mp3";
        } else if (fileNameExtension.equalsIgnoreCase(FileExtensionEnum.MP4.getValue())) {
            return "video/mp4";
        } else if (fileNameExtension.equalsIgnoreCase(FileExtensionEnum.AVI.getValue())) {
            return "video/avi";
        } else if (fileNameExtension.equalsIgnoreCase(FileExtensionEnum.WMV.getValue())) {
            return "video/x-ms-wmv";
        }
        return "image/jpg";
    }
}