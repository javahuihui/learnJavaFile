 # 自定义starter



## 01、starter作用

- 依赖聚合
- 自定义配置类 + @Bean
- 属性配置类

## 02、starter的命名规范

- 官方：spring-boot-starter -xxxx
- 自定义：xxx-boot-starter

## 03、实现步骤

标准的starter工程分为两个：

- xxx-boot-autoconfiguration
  - META-INF/spring.factories
  - 配置类
  - 属性配置类
  - 聚合依赖
- xxx-boot-starter
  - 引入xxx-boot-autoconfiguration

当然在很多场景和开发中，我们也只要一个工程即可。也把上面的xxx-boot-autoconfiguration去掉，用xxx-boot-starter一个即可。



##04、自定starter工程

### 01、kuangstudy-kaptcha-boot-starter工程

就是一个普通的maven工程而已，只不过需要遵循springboot内部的一些机制，所以你这个工程必须要导入sprignboot的基础核心包。

#### 第一步：新建两个maven项目

- kuangstudy-kaptcha-boot-starter 
- kuangstudy-kaptcha-boot-autoconfiguration

#### 第二步：pom.xml进行依赖聚合

在kuangstudy-kaptcha-boot-autoconfiguration的pom.xml依赖聚合：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.5.4</version>
    </parent>

    <groupId>com.kuangstudy.kpcode</groupId>
    <artifactId>kuangstudy-kaptcha-boot-autoconfiguration</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <!--自定义starter的核心配置类-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-autoconfigure</artifactId>
        </dependency>

        <!--属性配置类自动提示问题-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
        </dependency>

        <!--google kaptcha 验证码的核心依赖-->
        <dependency>
            <groupId>com.github.penggle</groupId>
            <artifactId>kaptcha</artifactId>
            <version>2.3.2</version>
        </dependency>

        <!--业务包lombok-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.20</version>
        </dependency>

    </dependencies>

</project>
```

#### 第三步：定义配置类

在kuangstudy-kaptcha-boot-autoconfiguration定义配置类：

```java
	package com.kuangstudy.kpcode;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author 飞哥
 * @Title: 学相伴出品
 * @Description: 飞哥B站地址：https://space.bilibili.com/490711252
 * 记得关注和三连哦！
 * @Description: 我们有一个学习网站：https://www.kuangstudy.com
 * @date 2021/12/26 21:29
 */
@Configuration
public class KaptchaConfiguration {


    @Bean
    public Producer producer() {
        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width", "100");
        properties.setProperty("kaptcha.image.height", "100");
        properties.setProperty("kaptcha.textproducer.font.color", "0,0,0");
        properties.setProperty("kaptcha.border.color", "105,179,90");
        properties.setProperty("kaptcha.textproducer.font.size", "32");
        properties.setProperty("kaptcha.session.key", "code");
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789ABCEFGHIJKLMNOPQRSTUVWXYZ");
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}

```

#### 第四步：定义META-INF/spring.facoties进行注册配置类

在kuangstudy-kaptcha-boot-autoconfiguration进行注册配置类



```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=com.kuangstudy.kpcode.KaptchaConfiguration
```

#### 第五步：在kuangstudy-kaptcha-boot-starter进行依赖注册

pom.xml如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.kuangstudy.kpcode</groupId>
    <artifactId>kuangstudy-kaptcha-boot-starter</artifactId>
    <version>1.0-SNAPSHOT</version>


    <dependencies>
        <dependency>
            <groupId>com.kuangstudy.kpcode</groupId>
            <artifactId>kuangstudy-kaptcha-boot-autoconfiguration</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

        <!--google kaptcha 验证码的核心依赖-->
        <dependency>
            <groupId>com.github.penggle</groupId>
            <artifactId>kaptcha</artifactId>
            <version>2.3.2</version>
        </dependency>

    </dependencies>
</project>
```



第六步：一定要把每个工程进行本地的install。

目的是：让两个工程放到本地仓库中，然后其他的项目如果也和本地仓库是关联，都可以使用。

![image-20211226213712563](%E8%87%AA%E5%AE%9A%E4%B9%89starter.assets/image-20211226213712563.png)







