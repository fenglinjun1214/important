ShardingSphere版本是用的目前最新的4.1.1版本。当前5.x版本还在孵化中。  
1、使用最开始的application01.properties运行时，会将course数据按奇数、偶数分开放到两个表里。
但是，注意在pom文件中，sharding-jdbc-spring-boot-starter依赖不能搭配druid-spring-boot-starter使用，会报mybatis无法创建数据库连接的错。  
```
Invocation of init method failed; nested exception is java.lang.IllegalArgumentException: Property 'sqlSessionFactory' or 'sqlSessionTemplate' are required org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext.refresh[557]
```

这版本依赖还是挺麻烦的。  
网上查了下，这是shardingsphere和druid数据源配合的问题。一个挺麻烦的bug，有很多人已经提到了。  
后来确定了，官网上也特意提到了这个问题，是因为druid-spring-boot-starter和ShardingSphere都会创建数据库连接池，会有冲突。  
解决的思路就两个，一是降sharding-jdbc-spring-boot-starter的版本，用4.0.0-RC1去搭配MyBatis和druid-spring-boot-starter  
另一个是不降sharding-jdbc-spring-boot-starter的版本，那就需要搭配MyBatis和com.alibaba.druid(非Spring-boot版本)
```xml
<!-- 数据源 -->
<!--<dependency>-->
<!--<groupId>com.alibaba</groupId>-->
<!--<artifactId>druid-spring-boot-starter</artifactId>-->
<!--</dependency>-->  

<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.1.21</version>
</dependency>

<!--mybatis-plus-->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
</dependency>
<!--sharding jdbc 开始-->
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
    <version>4.0.0-RC2</version>
<!-- <version>4.0.0-RC3</version>
<version>4.0.1</version> -->
</dependency>    
```

2、application02.properties: 水平分库

3、application03.properties: user表垂直分库

4、application04.properties: 增加绑定表示例

5、application05.properties: 分库分表  


另外 shardingsphere_docs_cn.pdf 是官方的文档。