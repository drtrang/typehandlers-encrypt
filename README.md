# MyBatis Type Handlers for JSR 310: Date and Time API

[![Build Status](https://travis-ci.org/mybatis/typehandlers-jsr310.svg?branch=master)](https://travis-ci.org/mybatis/typehandlers-jsr310)
[![Coverage Status](https://coveralls.io/repos/github/mybatis/typehandlers-jsr310/badge.svg?branch=master)](https://coveralls.io/github/mybatis/typehandlers-jsr310?branch=master)
[![Dependency Status](https://www.versioneye.com/user/projects/56ef43cb35630e00388897bb/badge.svg?style=flat)](https://www.versioneye.com/user/projects/56ef43cb35630e00388897bb)
[![Maven central](https://maven-badges.herokuapp.com/maven-central/org.mybatis/mybatis-typehandlers-jsr310/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.mybatis/mybatis-typehandlers-jsr310)
[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

![mybatis](http://mybatis.github.io/images/mybatis-logo.png)

The MyBatis type handlers supporting types introduced in JSR 310: Date and Time API.


## Installation

If you are using Maven add the following dependency to your `pom.xml`:

```xml
<dependency>
  <groupId>org.mybatis</groupId>
  <artifactId>mybatis-typehandlers-jsr310</artifactId>
  <version>1.0.2</version>
</dependency>
```

If you are using Gradle add the following dependency to your `build.gradle`:

```groovy
compile("org.mybatis:mybatis-typehandlers-jsr310:1.0.2")
```

## Configuration

If you are using MyBatis 3.4 or later, you can simply add this artifact on your classpath and MyBatis will automatically register the provided type handlers.
If you are using an older version, you need to add the type handlers to your `mybatis-config.xml` as follow:

```xml
<typeHandlers>
  <!-- ... -->
  <typeHandler handler="org.apache.ibatis.type.InstantTypeHandler" />
  <typeHandler handler="org.apache.ibatis.type.LocalDateTimeTypeHandler" />
  <typeHandler handler="org.apache.ibatis.type.LocalDateTypeHandler" />
  <typeHandler handler="org.apache.ibatis.type.LocalTimeTypeHandler" />
  <typeHandler handler="org.apache.ibatis.type.OffsetDateTimeTypeHandler" />
  <typeHandler handler="org.apache.ibatis.type.OffsetTimeTypeHandler" />
  <typeHandler handler="org.apache.ibatis.type.ZonedDateTimeTypeHandler" />
  <typeHandler handler="org.apache.ibatis.type.YearTypeHandler" />
  <typeHandler handler="org.apache.ibatis.type.MonthTypeHandler" />
  <typeHandler handler="org.apache.ibatis.type.YearMonthTypeHandler" />
  <typeHandler handler="org.apache.ibatis.type.JapaneseDateTypeHandler" />
</typeHandlers>
```

## Supported types

The following type handlers are supported:

| Type handler |  Date and Time API type | JDBC types | Available<br>version |
| ------------ | ----------------------- | ---------- | :------------------: | 
| `InstantTypeHandler` | `java.time.Instant` | `TIMESTAMP` |  |
| `LocalDateTimeTypeHandler` | `java.time.LocalDateTime` | `TIMESTAMP` |  |
| `LocalDateTypeHandler` | `java.time.LocalDate` | `DATE` |  |
| `LocalTimeTypeHandler` | `java.time.LocalTime` | `TIME` |  |
| `OffsetDateTimeTypeHandler` | `java.time.OffsetDateTime` | `TIMESTAMP` |  |
| `OffsetTimeTypeHandler` | `java.time.OffsetTime` | `TIME` |  |
| `ZonedDateTimeTypeHandler` | `java.time.ZonedDateTime` | `TIMESTAMP` |  |
| `YearTypeHandler` | `java.time.Year` | `INTEGER` | 1.0.1 |
| `MonthTypeHandler` | `java.time.Month` | `INTEGER` | 1.0.1 |
| `YearMonthTypeHandler` | `java.time.YearMonth` | `VARCHAR` or `LONGVARCHAR` | 1.0.2  |
| `JapaneseDateTypeHandler` | `java.time.chrono.JapaneseDate` | `DATE` | 1.0.2 |

> **Note:**
>
> For more details of type handler, please refer to "[MyBatis 3 REFERENCE DOCUMENTATION](http://www.mybatis.org/mybatis-3/configuration.html#typeHandlers)".
