package com.qingyu.mo.excel.handler;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.constant.OrderConstant;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

/**
 * <p>
 * 内容居中
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
public class DefaultStyle extends HorizontalCellStyleStrategy {

    @Override
    public int order() {
        return OrderConstant.DEFAULT_DEFINE_STYLE;
    }

    public DefaultStyle() {
        super();
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        setContentWriteCellStyleList(CollUtil.newArrayList(contentWriteCellStyle));
    }
}