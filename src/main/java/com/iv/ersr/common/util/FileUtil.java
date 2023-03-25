package com.iv.ersr.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.text.StrPool;
import com.iv.ersr.common.entity.InnerFile;
import com.iv.ersr.common.entity.enums.FileBusinessTypeEnum;
import com.iv.ersr.common.entity.enums.FileExtensionEnum;
import com.iv.ersr.common.entity.enums.FileTypeEnum;
import com.iv.ersr.common.exception.HandlerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

/**
 * <p>
 * 文件工具类
 * </p>
 *
 * @author liweimin
 * @since 2021-10-12
 */
@Component
@Slf4j
public class FileUtil {

    private FileUtil(){}

    /**
     * 创建新文件
     * @param originalFileName – 上传的文件名
     * @param fileBusinessType – 文件业务类型
     * @param flag – 是否添加年/月/日分类
     * @return InnerFile – 自定义文件实体
     */
    public static InnerFile createNewFile(String originalFileName, FileBusinessTypeEnum fileBusinessType, boolean flag) {
        StrBuilder strBuilder = StrBuilder.create(fileBusinessType.toString()).append(StrPool.SLASH);
        LocalDate now = LocalDate.now();
        String path;
        // 完整存储路径 -> 文件业务类型/年/月/日
        if (flag) {
            path = strBuilder.append(String.valueOf(now.getYear())).append(StrPool.SLASH).append(String.valueOf(now.getMonthValue())).append(StrPool.SLASH).append(String.valueOf(now.getDayOfMonth())).append(StrPool.SLASH).toString();
        } else {
            // 完整存储路径 -> 文件业务类型/
            path = strBuilder.toString();
        }
        // 文件的扩展名
        String type = getSuffix(originalFileName);
        // 原文件名 + 随机数
        String fileName = StrBuilder.create(getPrefix(originalFileName), StrPool.DASHED, Sequence.generateSequenceIdStr(), StrPool.DOT, type).toString();
        // 构建自定义文件对象
        return InnerFile.builder().originalName(originalFileName).type(type).name(fileName).path(path + fileName).build();
    }

    /**
     * 将内容写入文件
     * @param filePath – 文件路径
     * @param content – 内容
     * @return File – 文件对象
     */
    public static File write(String filePath, String content) {
        FileWriter mailFile = new FileWriter(filePath);
        return mailFile.write(content);
    }

    /**
     * 校验文件数据
     * @param file – 上传文件
     * @param fileType – 上传的文件类型
     */
    public static void checkFileData(MultipartFile file, FileTypeEnum fileType) throws IOException {
        // 校验文件名
        checkFileName(file, fileType, null);
        // 校验文件内容
        checkFileContent(file, fileType, null);
    }

    /**
     * 校验文件名
     * @param file – 上传文件
     * @param fileType – 上传的文件类型
     */
    public static void checkFileName(MultipartFile file, FileTypeEnum fileType) {
        checkFileName(file, fileType, null);
    }

    /**
     * 校验文件名
     * @param file – 上传文件
     * @param fileType – 上传的文件类型
     * @param exTypes – 额外的校验格式
     */
    public static void checkFileName(MultipartFile file, FileTypeEnum fileType, List<FileExtensionEnum> exTypes) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename==null){
            throw new HandlerException("文件名不存在！");
        }
        boolean flag = true;
        List<FileExtensionEnum> types = fileType.getTypes();
        if (CollUtil.isNotEmpty(exTypes)) {
            types.addAll(exTypes);
        }
        for (FileExtensionEnum type : types) {
            if (originalFilename.toLowerCase(Locale.ROOT).endsWith(type.getValue())) {
                flag = false;
                break;
            }
        }
        if (flag){
            throw new HandlerException("文件格式不正确！");
        }
    }

    /**
     * 校验文件内容
     * @param file – 上传文件
     * @param fileType – 上传的文件类型
     */
    public static void checkFileContent(MultipartFile file, FileTypeEnum fileType) throws IOException {
        checkFileContent(file, fileType, null);
    }

    /**
     * 校验文件内容
     * @param file – 上传文件
     * @param fileType – 上传的文件类型
     * @param exTypes – 额外的校验格式
     */
    public static void checkFileContent(MultipartFile file, FileTypeEnum fileType, List<FileExtensionEnum> exTypes) throws IOException {
        boolean contentFlag = true;
        List<FileExtensionEnum> types = fileType.getTypes();
        // 服务端通过读取文件的首部几个二进制位来判断常用的文件类型
        String typeStr = FileTypeUtil.getType(file.getInputStream());
        if (typeStr == null){
            throw new HandlerException("暂不支持此格式！");
        }
        if (CollUtil.isNotEmpty(exTypes)) {
            types.addAll(exTypes);
        }
        for (FileExtensionEnum type : types) {
            if (type.getValue().contains(typeStr)) {
                contentFlag = false;
                break;
            }
        }
        if (contentFlag){
            throw new HandlerException("文件内容有误！");
        }
    }

    /**
     * 获得文件的扩展名（后缀名），扩展名不带“.”
     * @param fileName – 文件名
     * @return String – 文件的扩展名
     */
    public static String getSuffix(String fileName) { return FileNameUtil.getSuffix(fileName); }

    /**
     * 返回主文件名
     * @param fileName 完整文件名
     * @return String – 文件的主文件名
     */
    public static String getPrefix(String fileName) { return FileNameUtil.getPrefix(fileName); }
}