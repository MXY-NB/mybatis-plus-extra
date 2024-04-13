package com.qingyu.mo.excel.handler;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.qingyu.mo.excel.write.entity.ExcelSelectedResolve;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.Map;

/**
 * <p>
 * excel下拉框写入处理器
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
@Data
@AllArgsConstructor
public class SelectedSheetWriteHandler implements SheetWriteHandler {
    private final Map<Integer, ExcelSelectedResolve> selectedMap;
 
    /**
     * Called before create the sheet
     */
    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        // is empty
    }
 
    /**
     * Called after the sheet is created
     */
    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        // 这里可以对cell进行任何操作
        Sheet sheet = writeSheetHolder.getSheet();
        DataValidationHelper helper = sheet.getDataValidationHelper();
        selectedMap.forEach((k, v) -> {
            // 设置下拉列表的行： 首行，末行，首列，末列
            CellRangeAddressList rangeList = new CellRangeAddressList(v.getFirstRow(), v.getLastRow(), k, k);
            // 设置下拉列表的值
            DataValidationConstraint constraint = helper.createExplicitListConstraint(v.getSource());
            // 设置约束
            DataValidation validation = helper.createValidation(constraint, rangeList);
            if (!v.isInput()) {
                // 阻止输入非下拉选项的值
                validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
                validation.setShowErrorBox(true);
                validation.createErrorBox("提示", "请输入下拉选项中的内容");
            }
            validation.setSuppressDropDownArrow(true);
            sheet.addValidationData(validation);
        });
    }
}