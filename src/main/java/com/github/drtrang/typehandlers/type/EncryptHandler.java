package com.github.drtrang.typehandlers.type;

import com.github.drtrang.typehandlers.alias.Encrypt;
import com.github.drtrang.typehandlers.util.EncryptUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 拦截 JavaType 为 encrypt 的 SQL
 * 注意：
 *   1. 加/解密的字段只过滤 null 值，空字符串依然会被加/解密
 *   2. fail fast 模式，当加/解密失败时，立即抛出异常
 *
 * @author trang
 */
@MappedTypes(Encrypt.class)
public class EncryptHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
            throws SQLException {
        // 只要 parameter 非空都进行加密
        ps.setString(i, EncryptUtil.encrypt(parameter));
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String r = rs.getString(columnName);
        return r == null ? null : EncryptUtil.decrypt(r);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String r = rs.getString(columnIndex);
        return r == null ? null : EncryptUtil.decrypt(r);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String r = cs.getString(columnIndex);
        return r == null ? null : EncryptUtil.decrypt(r);
    }

}
