# Stream 实战开发

技术栈：springBoot + mybatis-plus + vue + stream

具体的呈现：实现vue的Tree结构

![image-20211020221515523](C:/Users/Administrator/AppData/Roaming/Typora/typora-user-images/image-20211020221515523.png)

## 01、搭建后台的框架

### 01-01、新建一个springboot项目

![image-20211020221932888](C:/Users/Administrator/AppData/Roaming/Typora/typora-user-images/image-20211020221932888.png)

![image-20211020222011554](C:/Users/Administrator/AppData/Roaming/Typora/typora-user-images/image-20211020222011554.png)





![image-20211020222135687](C:/Users/Administrator/AppData/Roaming/Typora/typora-user-images/image-20211020222135687.png)



### 01-02、整合SSM

打开pom.xml去整合ssm相关的依赖

```java
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-freemarker</artifactId>
</dependency>

<!--mysql数据库驱动-->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.26</version>
</dependency>

<!--mybatis-plus依赖-->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.4.2</version>
</dependency>

<!--web依赖-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!--lombok依赖-->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
    <version>1.18.20</version>
</dependency>

<!--测试test依赖-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
    <version>3.12.0</version>
</dependency>
```



### 01-03、在application.yml配置数据连接信息

```java
server:
  port: 9100

spring:
  freemarker:
    suffix: .html
    cache: false
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8
    locale: zh_CN
    generator:
      write-numbers-as-strings: true
      write-bigdecimal-as-plain: true
    serialization:
      write-dates-as-timestamps: false
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/ksd-user-cours-db?serverTimezone=GMT%2b8&useUnicode=true&characterEncoding=utf-8&useSSL=false
    username: root
    password: mkxiaoer
    hikari:
      connection-timeout: 60000
      validation-timeout: 3000
      idle-timeout: 60000
      login-timeout: 5
      max-lifetime: 60000
      maximum-pool-size: 30
      minimum-idle: 10
      read-only: false

# mybatis-plus配置
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  mapper-locations: classpath*:/mapper/*.xml


logging:
  level:
    root: info
```



### 01-04、编写Category相关bean、mapper、service和controller



#### sql脚本

```sql
/*
 Navicat MySQL Data Transfer

 Source Server         : 我本地数据库
 Source Server Type    : MySQL
 Source Server Version : 50734
 Source Host           : localhost:3306
 Source Schema         : ksd-user-cours-db

 Target Server Type    : MySQL
 Target Server Version : 50734
 File Encoding         : 65001

 Date: 20/10/2021 22:42:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for kss_course_category
-- ----------------------------
DROP TABLE IF EXISTS `kss_course_category`;
CREATE TABLE `kss_course_category`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT ' 主键',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '分类名称',
  `descrciption` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '分类描述',
  `mark` int(1) NULL DEFAULT NULL COMMENT '是否更新',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `type` int(1) NULL DEFAULT NULL COMMENT '类型1文件夹 2文件',
  `pid` int(11) NULL DEFAULT NULL COMMENT '父ID',
  `status` int(1) NULL DEFAULT NULL COMMENT '发布状态1发布 0未发布',
  `isexpand` bit(1) NULL DEFAULT NULL COMMENT '0收起 1展开',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of kss_course_category
-- ----------------------------
INSERT INTO `kss_course_category` VALUES (1, 'Java', 'Java', 1, '2021-10-18 14:19:05', '2021-10-18 14:19:05', 1, 0, 1, b'1');
INSERT INTO `kss_course_category` VALUES (2, 'Js', 'Js', 1, '2021-10-18 14:19:05', '2021-10-18 14:19:05', 1, 0, 1, b'0');
INSERT INTO `kss_course_category` VALUES (3, 'Go', 'Go', 1, '2021-10-18 14:19:05', '2021-10-18 14:19:05', 1, 0, 1, b'0');
INSERT INTO `kss_course_category` VALUES (4, 'Python', 'Python', 1, '2021-10-18 14:19:05', '2021-10-18 14:19:05', 1, 0, 1, b'0');
INSERT INTO `kss_course_category` VALUES (5, 'Java面向对象', 'Java面向对象', 1, '2021-10-18 14:19:05', '2021-10-18 14:19:05', 1, 1, 1, b'0');
INSERT INTO `kss_course_category` VALUES (6, 'Spring', 'Spring', 1, '2021-10-18 14:19:05', '2021-10-18 14:19:05', 1, 1, 1, b'0');
INSERT INTO `kss_course_category` VALUES (7, 'SpringMvc', 'SpringMvc', 1, '2021-10-18 14:19:05', '2021-10-18 14:19:05', 2, 6, 1, b'0');
INSERT INTO `kss_course_category` VALUES (8, 'SpringBoot', 'SpringBoot', 1, '2021-10-18 14:19:05', '2021-10-18 14:19:05', 2, 6, 1, b'0');
INSERT INTO `kss_course_category` VALUES (9, 'Mybatis', 'Mybatis', 1, '2021-10-18 14:19:05', '2021-10-18 14:19:05', 1, 1, 1, b'0');
INSERT INTO `kss_course_category` VALUES (10, 'MybatisPlus', 'MybatisPlus', 1, '2021-10-18 14:19:05', '2021-10-18 14:19:05', 1, 9, 1, b'0');
INSERT INTO `kss_course_category` VALUES (11, 'nodejs', 'MybatisPlus', 1, '2021-10-18 14:19:05', '2021-10-18 14:19:05', 1, 2, 1, b'0');

SET FOREIGN_KEY_CHECKS = 1;

```



#### pojo

```java
package com.kuangstudy.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@ToString
@Builder
@TableName("kss_course_category")
public class CourseCategory implements java.io.Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    // 上课标题
    private String title;
    // 分类描述
    private String descrciption;
    // 是否更新
    private Integer mark;
    // 发布状态1发布 0未发布
    private Integer status;
    // 类型 1文件夹 2文件
    private Integer type;
    // 父id =0 根集 其它全部都是子集
    private Integer pid;
    // 是否展开和收起
    private Boolean isexpand;
    // 这里一定加exist=false ,代表当前这个字段不在表中，不加就会报错
    @TableField(exist = false)
    private List<CourseCategory> chilrenList;
    // 创建时间
    @TableField(fill = FieldFill.INSERT)//在新增的时候填充
    private Date createTime;
    // 更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)//在新增的时候填充
    private Date updateTime;
}

```

#### mapper

```java
package com.kuangstudy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuangstudy.pojo.CourseCategory;

public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {
}

```

#### service和serviceimpl

```java
package com.kuangstudy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuangstudy.pojo.CourseCategory;

public interface CourseCategoryService extends IService<CourseCategory> {
}

```

```java
package com.kuangstudy.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuangstudy.mapper.CourseCategoryMapper;
import com.kuangstudy.pojo.CourseCategory;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CourseCategoryServiceImpl extends ServiceImpl<CourseCategoryMapper, CourseCategory>
        implements CourseCategoryService {


}

```

#### controller

```java
package com.kuangstudy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseCategoryController {


    @GetMapping("/api/category/tree")
    public String tree(){
        return "tree";
    }

}

```



#### 测试整合ssm

````java
package com.kuangstudy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.kuangstudy.mapper")
public class KsdStreamVueTreeApplication {

    public static void main(String[] args) {
        SpringApplication.run(KsdStreamVueTreeApplication.class, args);
    }

}

````

写一个查询

```java
package com.kuangstudy.controller;

import com.kuangstudy.pojo.CourseCategory;
import com.kuangstudy.service.CourseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CourseCategoryController {


    @Autowired
    private CourseCategoryService courseCategoryService;


    @GetMapping("/api/category/tree")
    public List<CourseCategory> tree(){
        return courseCategoryService.list();
    }

}

```



```java
http://localhost:9100/api/category/tree
```





### 01-05、使用stream流完成分类的递归的相关操作



````java
package com.kuangstudy.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuangstudy.mapper.CourseCategoryMapper;
import com.kuangstudy.pojo.CourseCategory;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class CourseCategoryServiceImpl extends ServiceImpl<CourseCategoryMapper, CourseCategory>
        implements CourseCategoryService {


    public List<CourseCategory> findCategoiresTree() {
        // 1 :查询表中所有的数据
        List<CourseCategory> allList = this.list(); // 思考空间，为什么查询的是所有
        // 2: 找到所有的根节点 pid = 0
        List<CourseCategory> rootList = allList.stream().filter(category -> category.getPid().equals(0))
                .sorted((a,b)->a.getSorted()-b.getSorted()).collect(Collectors.toList());
        // 3 : 查询所有的非根节点
        List<CourseCategory> subList = allList.stream().filter(category -> !category.getPid().equals(0)).collect(Collectors.toList());
        // 4 : 循环根节点去subList去找对应的子节点
        rootList.forEach(root -> busorts(root,subList));

        return rootList;
    }


    private void busorts(CourseCategory root,List<CourseCategory> subList){
        // 通过根节点去id和子节点的pid是否相等，如果相等的话，代表是当前根的子集
        List<CourseCategory> childrenList = subList.stream().filter(category -> category.getPid().equals(root.getId()))
                .sorted((a,b)->a.getSorted()-b.getSorted())
                .collect(Collectors.toList());
        // 如果你当前没一个子集，初始化一个空数组
        if(!CollectionUtils.isEmpty(childrenList)) {
            // 查询以后放回去
            root.setChildenList(childrenList);
            // 再次递归构建即可
            childrenList.forEach(category -> busorts(category,subList));
        }else{
            root.setChildenList(new ArrayList<>());
        }
    }
}

````

最终的数据结构的效果

```java
- Java
	- Spring
		- SpringMvc
		- SpringBoot
	- Mybatis
		- MybatisPlus
- Phtyon
- Go
- JS
	- nodejs
```

推到成json

```json
[{
	id:1,
	title:"Java",
	pid:0,
	chilrenList:[
		{
			id:5,title:"Spring",
            pid:1,
			childrenList:[{id:12,title:"SpringMvc",pid:5},{id:12,title:"SpringBoot",pid:5}]
		},
		{
			id:6,title:"Mybatis", pid:1,
			childrenList:[{id:12,title:"mybatisplus",pid:6}]
		}
    ]
},
{
	id:2,
	title:"Phtyon",
    pid:0,
    chilrenList:[]
},
{
	id:3,
	title:"Go",
    pid:0,
    chilrenList:[]
},
{
	id:4,
	title:"JS"
    pid:0,
    chilrenList:[
    	{id:11,title:"nodejs",pid:4}
    ]
}]
```







### 01-06、使用axios完成异步接口的调用

- 要么使用现成的tree组件 v-tree/ztree/other tree
- 要么自己开发
  - 第一件事情：查询接口返回数据
    - 使用Axios查询数据接口返回给页面
  - 使用vue来进行tree实现
    - vue组件递归的问题
    - vue语法你必须掌握







### 01-07、使用vue的组件和递归组件完成tree结构

```html
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>tree菜单</title>
    <style>
        [v-cloak]{display: none}
    </style>
</head>
<body>

    <div id="app" v-cloak>
        <h1>{{title}}</h1>
        <ksd-tree v-bind:data="dataList"></ksd-tree>
    </div>


    <script src="/js/vue.min.js"></script>
    <script src="/js/axios.min.js"></script>
    <script>

        Vue.component("ksd-tree",{
            props:{
                data:Array  // private List<?> data;
            },
            template:"<ul>" +
                        "<li v-for='(d,index) in data' @click.stop='expand(d)'>" +
                            "<span><span v-if='d.type==1'>目录</span><span v-if='d.type==2'>文件</span> {{d.title}}</span>" +
                            "<ksd-tree v-show='d.isexpand' v-if='d.childrenList' v-bind:data='d.childrenList'></ksd-tree>"+
                        "</li>" +
                    "</ul>",
            methods:{
                expand:function(obj){
                    obj.isexpand = !obj.isexpand;
                }
            }
        })
        var vue = new Vue({
            el:"#app",
            data:{
                title:"学相伴基于vue的Tree菜单",
                dataList:[]
            },
            created:function(){
                // 1： 页面加载初始化执行获取数组
                this.loadTree();
            },
            methods:{
                // 加载tree的数据信息
                loadTree:function(){
                    axios.get("/api/category/tree").then(res => {
                        this.dataList = res.data;
                    })
                }
            }
        })


    </script>


</body>
</html>
```



















