# Swagger3生成API在线文档工具



## 01、出现背景

在前后端分离的大趋势下，无论是前端开发人员还是后端开发人员，或多或少都被接口文档折磨过。而且由于开发任务重，时间紧迫，经常陷入版本迭代而接口文档缺没有及时更新的窘境，为了解决这个问题，就有了Swagger生成接口文档工具。

Swagger出现的背景：

- 接口文档对于前后端开发人员都十分重要。
- 尤其近几年流行前后端分离后接口文档又变成重中之重。
- 接口文档固然重要，但是由于项目周期等原因后端人员经常出现无法及时更新，
- 导致前端人员抱怨接口文档和实际情况不一致。
- 很多人员会抱怨别人写的接口文档不规范，不及时更新。
- 当时自己写的时候确实最烦去写接口文档。这种痛苦只有亲身经历才会牢记于心。
- 如果接口文档可以实时动态生成就不会出现上面问题。
- Swagger 可以完美的解决上面的问题。
  

Swagger 是一款RESTFUL接口的、基于YAML、JSON语言的文档在线自动生成、代码自动生成的工具。

官网：https://swagger.io/

- Swagger 代码级别的在线帮助文档。



## 02、Swagger3注解说明

```properties
@Api：用在请求的类上，表示对类的说明
    tags="说明该类的作用，可以在UI界面上看到的注解"
    value="该参数没什么意义，在UI界面上也看到，所以不需要配置"

@ApiOperation：用在请求的方法上，说明方法的用途、作用
    value="说明方法的用途、作用"
    notes="方法的备注说明"

@ApiImplicitParams：用在请求的方法上，表示一组参数说明
    @ApiImplicitParam：用在@ApiImplicitParams注解中，指定一个请求参数的各个方面
        name：参数名
        value：参数的汉字说明、解释
        required：参数是否必须传
        paramType：参数放在哪个地方
            · header --> 请求参数的获取：@RequestHeader
            · query --> 请求参数的获取：@RequestParam
            · path（用于restful接口）--> 请求参数的获取：@PathVariable
            · div（不常用）
            · form（不常用）    
        dataType：参数类型，默认String，其它值dataType="Integer"       
        defaultValue：参数的默认值

@ApiResponses：用在请求的方法上，表示一组响应
    @ApiResponse：用在@ApiResponses中，一般用于表达一个错误的响应信息
        code：数字，例如400
        message：信息，例如"请求参数没填好"
        response：抛出异常的类

@ApiIgnore: 忽略文档或者忽略方法接口生成

@ApiModel：用于响应类上，表示一个返回响应数据的信息
            （这种一般用在post创建的时候，使用@RequestBody这样的场景，
            请求参数无法使用@ApiImplicitParam注解进行描述的时候）
    @ApiModelProperty：用在属性上，描述响应类的属性
```





## 03、引入步骤

### 1.编写SpringBoot 项目

编写SpringBoot 项目，项目中controller 中包含一个Handler，
测试项目，保证程序可以正确运行。

可以使用自己经编写好的可以返回json的controller进行测试

```java
package com.kuangstudy.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "用户信息管理")
@RestController
@RequestMapping("userRecord")
public class UserRecordController  {

    /**
     * 分页查询所有数据
     * @return 所有数据
     */
    @ApiOperation("分页查询所有数据")
    @GetMapping("page")
    public void selectAll() {
    }

}

```
### 2：导入pom.xml依赖

互联网看到的很多swagger集成都是Swagger2的版本，Swagger2的springboot的依赖必须是：2.2.6.RELEASE。如果你用最新的springboot整合Swagger2就会出问题。

```xml
<!-- 接口文档可以实时动态生成工具Swagger -->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-boot-starter</artifactId>
    <version>3.0.0</version>
</dependency>
```

### 3：定义配置类和开启swagger

```java
package com.kuangstudy.config;//package com.kuangstudy.config;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


/**
 * @author 飞哥
 * @Title: 学相伴出品
 * @Description: 飞哥B站地址：https://space.bilibili.com/490711252
 * 记得关注和三连哦！
 * @Description: 我们有一个学习网站：https://www.kuangstudy.com
 * @date 2021/12/24 16:14
 */
@Configuration
@EnableOpenApi // 开启swagger3的支持
@EnableWebMvc
public class Swagger3Configuration {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                // 扫包的方式 不论加不加Swagger的注解，默认代表全部要生成文档接口
                //.apis(RequestHandlerSelectors.basePackage("com.kuangstudy.web"))
                // 推荐下面这个  加了Swagger的注解@Api，就会生成文档接口，否则不会
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("学相伴旅游项目实战接口文档")
                .description("学相伴旅游项目实战接口文档。")
                .contact(new Contact("yykk、xiaowen, xiaoli,xiaoliu", "https://www.itbooking.net", "xuchengfeifei@163.com"))
                .version("1.0")
                .build();
    }

}

```



### 4.访问UI页面`入 http://ip:port/swagger-ui/index.html

在页面中可以通过可视化的进行操作项目中所有接口。

![image-20211224163029203](01%E3%80%81Swagger%E7%94%9F%E6%88%90API%E6%96%87%E6%A1%A3%E5%B7%A5%E5%85%B7.assets/image-20211224163029203.png)

