package com.qingyu.mo.typehandler;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>
 * AES数据库数据加密解密
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
public interface DataAesHandler extends TypeHandler<String> {

    @Override
    default void setParameter(PreparedStatement preparedStatement, int i, String s, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, aes().encryptHex(s, CharsetUtil.CHARSET_UTF_8));
    }

    @Override
    default String getResult(ResultSet resultSet, String columnName) throws SQLException {
        if (ObjectUtil.isNull(resultSet)) {
            return StringPool.EMPTY;
        }
        if (CharSequenceUtil.isEmpty(resultSet.getString(columnName))) {
            return StringPool.EMPTY;
        }
        String columnValue = resultSet.getString(columnName);
        return aes().decryptStr(columnValue, CharsetUtil.CHARSET_UTF_8);
    }

    @Override
    default String getResult(ResultSet resultSet, int columnIndex) throws SQLException {
        if (ObjectUtil.isNull(resultSet)) {
            return StringPool.EMPTY;
        }
        if (CharSequenceUtil.isEmpty(resultSet.getString(columnIndex))) {
            return StringPool.EMPTY;
        }
        String columnValue = resultSet.getString(columnIndex);
        return aes().decryptStr(columnValue, CharsetUtil.CHARSET_UTF_8);
    }

    @Override
    default String getResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        if (ObjectUtil.isNull(callableStatement)) {
            return StringPool.EMPTY;
        }
        if (CharSequenceUtil.isEmpty(callableStatement.getString(columnIndex))) {
            return StringPool.EMPTY;
        }
        String columnValue = callableStatement.getString(columnIndex);
        return aes().decryptStr(columnValue, CharsetUtil.CHARSET_UTF_8);
    }

    SymmetricCrypto aes();
}
