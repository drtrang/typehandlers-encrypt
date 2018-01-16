package com.github.trang.typehandlers.type;

import com.github.trang.typehandlers.alias.Encrypt;
import com.github.trang.typehandlers.util.EncryptUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 拦截 JavaType 为 #{@link Encrypt} 的 SQL
 * 注意：
 *   1. 加密时字段只过滤 `null` 值，明文不做任何处理直接加密
 *   2. 解密时会判断字段是否是加密数据，如果是才会解密否则直接返回原始数据
 *   3. fail fast 模式，当加/解密失败时，立即抛出异常
 *
 * @author trang
 */
@MappedTypes(Encrypt.class)
public class EncryptTypeHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
            throws SQLException {
        // 只要 parameter 非空都进行加密
        ps.setString(i, EncryptUtil.encrypt(parameter));
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String r = rs.getString(columnName);
        // 兼容待修复的数据
        return r == null ? null : (EncryptUtil.isEncrypted(r) ? EncryptUtil.decrypt(r) : r);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String r = rs.getString(columnIndex);
        // 兼容待修复的数据
        return r == null ? null : (EncryptUtil.isEncrypted(r) ? EncryptUtil.decrypt(r) : r);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String r = cs.getString(columnIndex);
        // 兼容待修复的数据
        return r == null ? null : (EncryptUtil.isEncrypted(r) ? EncryptUtil.decrypt(r) : r);
    }

}