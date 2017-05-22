package com.github.drtrang.typehandlers.type;

import com.github.drtrang.typehandlers.alias.Encrypt;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.github.drtrang.typehandlers.util.EncryptUtil.*;

/**
 * 拦截 JavaType 为 #{@link Encrypt} 的 SQL
 * 注意：
 *   1. 加密时字段只过滤 null 值，明文不做任何处理直接加密
 *   2. 解密时判断数据库中的值是否不是数字且长度是否 >= 32，若不满足条件认为是未修复数据
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
        ps.setString(i, encrypt(parameter));
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String r = rs.getString(columnName);
        // 兼容待修复的数据
        return r == null ? null : isEncrypted(r) ? decrypt(r) : r;
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String r = rs.getString(columnIndex);
        return r == null ? null : isEncrypted(r) ? decrypt(r) : r;
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String r = cs.getString(columnIndex);
        return r == null ? null : isEncrypted(r) ? decrypt(r) : r;
    }

}