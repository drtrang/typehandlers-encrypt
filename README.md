# MyBatis Type Handlers for Encrypt

## 介绍
应公司安全部门要求，需要对数据库敏感字段进行加密处理，我结合实际情况后开发出此插件，希望减小此需求对各业务方的影响，避免重复工作。

该插件尽量保证对业务方透明，仅需少量配置即可实现指定字段的加解密操作。

## 依赖
### Maven
```xml
<dependency>
    <groupId>com.github.drtrang</groupId>
    <artifactId>typehandlers-encrypt</artifactId>
    <version>1.0.1</version>
</dependency>
```

### Gradle
```groovy
compile("com.github.drtrang:typehandlers-encrypt:1.0.1")
```

## 使用方式
### 声明 EncryptTypeHandler
#### mybatis-config.xml
```xml
<typeHandlers>
    <package name="com.github.drtrang.typehandlers.type" />
</typeHandlers>

<typeAliases>
    <package name="com.github.drtrang.typehandlers.alias" />
</typeAliases>
```

#### Spring 配置
```java
@Bean
public SqlSessionFactory sqlSessionFactory(Configuration config) {
    SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
    ...
    factory.setTypeAliasesPackage("com.github.drtrang.typehandlers.alias;xxx.domain");
    factory.setTypeHandlersPackage("com.github.drtrang.typehandlers.type");
    return factory.getObject();
}
```

#### SpringBoot 配置
```yaml
##application.yml
mybatis:
  type-aliases-package: xxx.domain;com.github.drtrang.typehandlers.alias
  type-handlers-package: com.github.drtrang.typehandlers.type
```

以上配置任选其一即可，请根据实际情况选择。

### 使用 EncryptTypeHandler
```xml
<!-- select -->
<resultMap id="BaseResultMap" type="user">
    <id column="id" property="id" jdbcType="BIGINT" />
    <result column="username" javaType="string" property="username" jdbcType="VARCHAR" />
    <result column="password" javaType="encrypt" property="password" jdbcType="VARCHAR" />
</resultMap>

<!-- insert -->
<insert id="insert" parameterType="user">
    insert into user (id, username, password)
    values (#{id,jdbcType=BIGINT}, #{username,jdbcType=VARCHAR}, #{password, javaType=encrypt, jdbcType=VARCHAR})
</insert>
```

### 加密算法
加密方式统一为 AES 加密，私钥可自定义，查找逻辑如下：
1. 在 classpath 中依次查找名称为 `encrypt`、`properties/config-common`、`properties/config`、`config`、`application` 的 Properties 文件，直到文件存在且文件中包含名称为 `encrypt.private.key` 的属性时停止
2. 若不存在上述文件，或文件中均不存在 `encrypt.private.key` 属性，则使用默认值（与 iprd-common 中的私钥相同）
3. 若以上内置文件不满足需求，业务方可自定义文件名称 ```BundleUtil.bundleNames("common")```，此种方式只从给定的文件中查找，若文件中不包含 `encrypt.private.key` 属性，则使用默认值（与 iprd-common 中的私钥相同）

## 注意
目前 `EncryptTypeHandler` 只支持 javaType 为 String 的情况，如有其它需求，请及时联系我。
