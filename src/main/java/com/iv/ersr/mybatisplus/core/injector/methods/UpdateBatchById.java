package com.iv.ersr.mybatisplus.core.injector.methods;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.iv.ersr.mybatisplus.core.entity.enums.SqlMethod;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Objects;
import java.util.function.Predicate;

import static java.util.stream.Collectors.joining;

/**
 * <p>
 * 批量更新数据
 * </p>
 *
 * @author moxiaoyu
 * @since 2023-03-22
 */
public class UpdateBatchById extends AbstractMethod {

    /**
     * 字段筛选条件
     */
    @Setter
    @Accessors(chain = true)
    private Predicate<TableFieldInfo> predicate;

    public UpdateBatchById() {
        super("myUpdateByIdBatchSomeColumn");
    }

    /**
     * 默认方法名
     * @param predicate 字段筛选条件
     */
    public UpdateBatchById(Predicate<TableFieldInfo> predicate) {
        this(SqlMethod.UPDATE_BATCH_BY_ID.getMethod(), predicate);
    }

    /**
     * 默认方法名
     * @param name 方法名
     * @param predicate 字段筛选条件
     */
    public UpdateBatchById(String name, Predicate<TableFieldInfo> predicate) {
        super(name);
        this.predicate = predicate;
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = "<script>\n<foreach collection=\"list\" item=\"item\" separator=\";\">\nupdate %s %s where %s=#{%s} %s\n</foreach>\n</script>";
        String additional = tableInfo.isWithVersion() ? tableInfo.getVersionFieldInfo().getVersionOli("item", "item.") : "" + tableInfo.getLogicDeleteSql(true, true);
        String setSql = sqlSet(tableInfo.isWithLogicDelete(), false, tableInfo, false, "item", "item.");
        String sqlResult = String.format(sql, tableInfo.getTableName(), setSql, tableInfo.getKeyColumn(), "item." + tableInfo.getKeyProperty(), additional);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sqlResult, modelClass);
        // 第三个参数必须和RootMapper的自定义方法名一致
        return this.addUpdateMappedStatement(mapperClass, modelClass, methodName, sqlSource);
    }

    protected String sqlSet(boolean logic, TableInfo table, boolean judgeAliasNull, final String alias,
                            final String prefix) {
        String sqlScript = getAllSqlSet(table, logic, prefix);
        if (judgeAliasNull) {
            sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", alias), true);
        }
        sqlScript = SqlScriptUtils.convertSet(sqlScript);
        return sqlScript;
    }

    public String getAllSqlSet(TableInfo table, boolean ignoreLogicDelFiled, final String prefix) {
        final String newPrefix = prefix == null ? EMPTY : prefix;
        return table.getFieldList().stream()
                .filter(predicate)
                .filter(i -> {
                    if (ignoreLogicDelFiled) {
                        return !(table.isWithLogicDelete() && i.isLogicDelete());
                    }
                    return true;
                }).map(i -> i.getSqlSet(newPrefix)).filter(Objects::nonNull).collect(joining(NEWLINE));
    }
}
