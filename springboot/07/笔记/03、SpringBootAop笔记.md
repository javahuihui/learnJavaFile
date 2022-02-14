# SpringAop的认识



## 01、课程目标

- 认识SpringAop
- 了解SpringAop的底层实现的原理
  - JDK动态代理
  - CGLIB代理
- SpringAop的增强通知的类型
- SpringAop的切点的定义
- Spring中默认的Aop代理机制是什么？
- SpringBoot中默认的Aop代理机制是什么？
- SpringAop的实战开发，日志管理，限流处理，权限拦截。
- SpringMvc源码分析后置通知是如何和Aop产生管理的。
- 然后在回归学习SpringAop和动态代理的关系。
- 为什么springaop是构建在ioc基础上的呢？



## 02、准备工作

- 创建一个springboot-aop-07的项目
- 在项目的pom.xml中依赖aop如下：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```



## 03、认识SpringAop

java学习曲线，面向对象，创建类，创建属性，创建方式，创建对象，执行方法，给属性赋值，比如：

用户pojo

```java
package com.kuangstudy.first;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author 飞哥
 * @Title: 学相伴出品
 * @Description: 飞哥B站地址：https://space.bilibili.com/490711252
 * 记得关注和三连哦！
 * @Description: 我们有一个学习网站：https://www.kuangstudy.com
 * @date 2021/12/21 22:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private Integer userId;
    private String nickname;
    private String password;
}

```

订单pojo

```java
package com.kuangstudy.first;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author 飞哥
 * @Title: 学相伴出品
 * @Description: 飞哥B站地址：https://space.bilibili.com/490711252
 * 记得关注和三连哦！
 * @Description: 我们有一个学习网站：https://www.kuangstudy.com
 * @date 2021/12/21 22:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {
    private Integer userId;
    private Integer orderId;
    private String price;
    private String orderTradeNo;
}

```

用户service

```java
package com.kuangstudy.first;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 飞哥
 * @Title: 学相伴出品
 * @Description: 飞哥B站地址：https://space.bilibili.com/490711252
 * 记得关注和三连哦！
 * @Description: 我们有一个学习网站：https://www.kuangstudy.com
 * @date 2021/12/21 22:05
 */
@Slf4j
@Service
public class UserService {

    /**
     * 用户注册
     *
     * @param user
     */
    public void saveUser(User user) {
        log.info("用户注册....");
    }

    /**
     * 修改用户
     *
     * @param user
     */
    public void updateUser(User user) {
        log.info("修改用户....");
    }


    /**
     * 删除用户
     *
     * @param userId
     */
    public void delUser(Integer userId) {
        log.info("删除用户....{}", userId);
    }

}

```

订单service

```java
package com.kuangstudy.first;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 飞哥
 * @Title: 学相伴出品
 * @Description: 飞哥B站地址：https://space.bilibili.com/490711252
 * 记得关注和三连哦！
 * @Description: 我们有一个学习网站：https://www.kuangstudy.com
 * @date 2021/12/21 22:05
 */
@Slf4j
@Service
public class OrderService {

    /**
     * 订单注册
     *
     * @param order
     */
    public void saveOrder(Order order) {
        log.info("订单注册....");
    }

    /**
     * 修改订单
     *
     * @param order
     */
    public void updateOrder(Order order) {
        log.info("修改订单....");
    }


    /**
     * 删除订单
     *
     * @param orderId
     */
    public void delOrder(Integer orderId) {
        log.info("删除订单....{}", orderId);
    }

}

```



测试代码

```java
package com.kuangstudy;

import com.kuangstudy.first.User;
import com.kuangstudy.first.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootAopApplicationTests {

    @Autowired
    private UserService userService;


    @Test
    void contextLoads() {
        // 1：用户注册
        userService.saveUser(new User());
        // 2: 用户修改
        userService.updateUser(new User());
        // 3: 用户的删除
        userService.delUser(1);
    }

}

```



打印测试结果如下：

```properties
【KSD - CONSOLE】 2021-12-21 22:15:13:275 [main] [INFO ] com.kuangstudy.first.UserService saveUser 28 - 用户注册....
【KSD - CONSOLE】 2021-12-21 22:15:13:275 [main] [INFO ] com.kuangstudy.first.UserService updateUser 37 - 修改用户....
【KSD - CONSOLE】 2021-12-21 22:15:13:275 [main] [INFO ] com.kuangstudy.first.UserService delUser 47 - 删除用户....1
```

上面代码：其实就一个标准的java面向对象，创建对象执行方法的过程。如果在开发过程中，我们需要给对象执行方法方法的时候，去增强方法的执行逻辑的时候，控制权限的时候，进行限流的时候，怎么做呢？

### 重要的信息

> ==`对象执行方法`=== ,aop它其实就是对象执行方法过程中一种横切逻辑增强处理的机制。-- OOP

### 我们写代码追去的境界

- 高内聚低耦合

  - 接口
  - oop
  - 消息队列
  - 设计模式
  - SPI

- 尽量用面向对象的思维

  - 枚举

    



## 04、日志开发需求场景

> 我们现在要给程序中的所有用户接口和订单的接口全部进行订单增强日志处理？

会定义一个 Log日志类：

### 日志场景需求的作用

- 可以协助我们排除执行方法耗时的问题
- 这样可以针对性的优化和处理执行方法耗时的问题

### 日志的pojo

```java
package com.kuangstudy.first;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author 飞哥
 * @Title: 学相伴出品
 * @Description: 飞哥B站地址：https://space.bilibili.com/490711252
 * 记得关注和三连哦！
 * @Description: 我们有一个学习网站：https://www.kuangstudy.com
 * @date 2021/12/21 22:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Logs {
    private Integer id;
    private String classname;
    private String method;
    private String time;
    private String params;
}

```

### 日志处理service

```java
package com.kuangstudy.first;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 飞哥
 * @Title: 学相伴出品
 * @Description: 飞哥B站地址：https://space.bilibili.com/490711252
 * 记得关注和三连哦！
 * @Description: 我们有一个学习网站：https://www.kuangstudy.com
 * @date 2021/12/21 22:05
 */
@Slf4j
@Service
public class LogService {

    public void saveLog(Logs logs) {
        log.info("你保存日志是：{}", logs);
    }
}

```



### 开发日志保存的记录功能

我们一般的正常思维和开发流程是上面样子，如下：

#### 用户服务需要记录日志如下：

```java
package com.kuangstudy.first;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 飞哥
 * @Title: 学相伴出品
 * @Description: 飞哥B站地址：https://space.bilibili.com/490711252
 * 记得关注和三连哦！
 * @Description: 我们有一个学习网站：https://www.kuangstudy.com
 * @date 2021/12/21 22:05
 */
@Slf4j
@Service
public class UserService {


    @Autowired
    private LogService logService;

    /**
     * 用户注册
     *
     * @param user
     */
    public void saveUser(User user) {
        log.info("用户注册....");

        // 记录日志
        Logs logs = new Logs();
        logs.setClassname("com.kuangstudy.service.UserService");
        logs.setMethod("saveUser");
        logs.setId(1);
        logs.setParams(user.toString());
        logs.setTime("100ms");
        logService.saveLog(logs);
    }

    /**
     * 修改用户
     *
     * @param user
     */
    public void updateUser(User user) {
        log.info("修改用户....");

        // 记录日志
        Logs logs = new Logs();
        logs.setClassname("com.kuangstudy.service.UserService");
        logs.setMethod("updateUser");
        logs.setId(1);
        logs.setParams(user.toString());
        logs.setTime("100ms");
        logService.saveLog(logs);
    }


    /**
     * 删除用户
     *
     * @param userId
     */
    public void delUser(Integer userId) {
        log.info("删除用户....{}", userId);

        // 记录日志
        Logs logs = new Logs();
        logs.setClassname("com.kuangstudy.service.UserService");
        logs.setMethod("delUser");
        logs.setId(1);
        logs.setParams(userId + "");
        logs.setTime("100ms");
        logService.saveLog(logs);
    }

}

```

#### 订单服务记录日志

```java
package com.kuangstudy.first;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 飞哥
 * @Title: 学相伴出品
 * @Description: 飞哥B站地址：https://space.bilibili.com/490711252
 * 记得关注和三连哦！
 * @Description: 我们有一个学习网站：https://www.kuangstudy.com
 * @date 2021/12/21 22:05
 */
@Slf4j
@Service
public class OrderService {

    @Autowired
    private LogService logService;

    /**
     * 订单注册
     *
     * @param order
     */
    public void saveOrder(Order order) {
        log.info("订单注册....");

        // 记录日志
        Logs logs = new Logs();
        logs.setClassname("com.kuangstudy.service.OrderService");
        logs.setMethod("saveOrder");
        logs.setId(1);
        logs.setParams(order.toString());
        logs.setTime("100ms");
        logService.saveLog(logs);
    }

    /**
     * 修改订单
     *
     * @param order
     */
    public void updateOrder(Order order) {
        log.info("修改订单....");

        // 记录日志
        Logs logs = new Logs();
        logs.setClassname("com.kuangstudy.service.OrderService");
        logs.setMethod("updateOrder");
        logs.setId(1);
        logs.setParams(order.toString());
        logs.setTime("100ms");
        logService.saveLog(logs);
    }


    /**
     * 删除订单
     *
     * @param orderId
     */
    public void delOrder(Integer orderId) {
        log.info("删除订单....{}", orderId);

        // 记录日志
        Logs logs = new Logs();
        logs.setClassname("com.kuangstudy.service.OrderService");
        logs.setMethod("delOrder");
        logs.setId(1);
        logs.setParams(orderId.toString());
        logs.setTime("100ms");
        logService.saveLog(logs);
    }

}

```

#### 执行测试

```java
package com.kuangstudy;

import com.kuangstudy.first.Order;
import com.kuangstudy.first.OrderService;
import com.kuangstudy.first.User;
import com.kuangstudy.first.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringbootAopApplicationTests {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;


    @Test
    void contextLoads() {
        // 1：用户注册
        userService.saveUser(new User());
        // 2: 用户修改
        userService.updateUser(new User());
        // 3: 用户的删除
        userService.delUser(1);
    }

    @Test
    public void testOrder() {
        // 1：用户注册
        orderService.saveOrder(new Order());
        // 2: 用户修改
        orderService.updateOrder(new Order());
        // 3: 用户的删除
        orderService.delOrder(1);
    }

}

```

#### 执行结果

```properties
【KSD - CONSOLE】 2021-12-21 22:33:38:208 [main] [INFO ] com.kuangstudy.first.UserService saveUser 33 - 用户注册....
【KSD - CONSOLE】 2021-12-21 22:33:38:208 [main] [INFO ] com.kuangstudy.first.LogService saveLog 19 - 你保存日志是：Logs(id=1, classname=com.kuangstudy.service.UserService, method=saveUser, time=100ms, params=User(userId=null, nickname=null, password=null))
【KSD - CONSOLE】 2021-12-21 22:33:38:210 [main] [INFO ] com.kuangstudy.first.UserService updateUser 51 - 修改用户....
【KSD - CONSOLE】 2021-12-21 22:33:38:211 [main] [INFO ] com.kuangstudy.first.LogService saveLog 19 - 你保存日志是：Logs(id=1, classname=com.kuangstudy.service.UserService, method=updateUser, time=100ms, params=User(userId=null, nickname=null, password=null))
【KSD - CONSOLE】 2021-12-21 22:33:38:211 [main] [INFO ] com.kuangstudy.first.UserService delUser 70 - 删除用户....1
【KSD - CONSOLE】 2021-12-21 22:33:38:211 [main] [INFO ] com.kuangstudy.first.LogService saveLog 19 - 你保存日志是：Logs(id=1, classname=com.kuangstudy.service.UserService, method=delUser, time=100ms, params=1)


```

#### 订单执行的结果

```properties
【KSD - CONSOLE】 2021-12-21 22:34:38:089 [main] [INFO ] com.kuangstudy.first.OrderService saveOrder 28 - 订单注册....
【KSD - CONSOLE】 2021-12-21 22:34:38:090 [main] [INFO ] com.kuangstudy.first.LogService saveLog 19 - 你保存日志是：Logs(id=1, classname=com.kuangstudy.service.OrderService, method=saveOrder, time=100ms, params=Order(userId=null, orderId=null, price=null, orderTradeNo=null))
【KSD - CONSOLE】 2021-12-21 22:34:38:096 [main] [INFO ] com.kuangstudy.first.OrderService updateOrder 46 - 修改订单....
【KSD - CONSOLE】 2021-12-21 22:34:38:096 [main] [INFO ] com.kuangstudy.first.LogService saveLog 19 - 你保存日志是：Logs(id=1, classname=com.kuangstudy.service.OrderService, method=updateOrder, time=100ms, params=Order(userId=null, orderId=null, price=null, orderTradeNo=null))
【KSD - CONSOLE】 2021-12-21 22:34:38:097 [main] [INFO ] com.kuangstudy.first.OrderService delOrder 65 - 删除订单....1
【KSD - CONSOLE】 2021-12-21 22:34:38:097 [main] [INFO ] com.kuangstudy.first.LogService saveLog 19 - 你保存日志是：Logs(id=1, classname=com.kuangstudy.service.OrderService, method=delOrder, time=100ms, params=1)


```



### 你必须从上面的代码中认知又哪些？

1、通过上面一个非常典型案例就给一些做日志增强处理的时候，我们发现都是 `对象执行方法` 要`额外做一些事情`而这些个事情，你又还得不得不做。

2、日志逻辑的代码是紧耦合，它污染了一些业务逻辑。而且不便于后续的维护。

3、现在上面的整个日志都会拦截，灵活吗？ 如果我要考虑到有些方法要拦截，又方法又不拦截怎么处理？比如后续的限流，权限拦截



## 05、上面的问题的解决方案

- AOP



## 06、上面的代码我们可以通过一些方案去优化呢？

### 01、方法封装

#### 01、日志方法的重载

```java
package com.kuangstudy.first;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 飞哥
 * @Title: 学相伴出品
 * @Description: 飞哥B站地址：https://space.bilibili.com/490711252
 * 记得关注和三连哦！
 * @Description: 我们有一个学习网站：https://www.kuangstudy.com
 * @date 2021/12/21 22:05
 */
@Slf4j
@Service
public class LogService {

    public void saveLog(Logs logs) {
        log.info("你保存日志是：{}", logs);
    }

    public void saveLog(String classname,String method,String time,String params) {
        Logs logs = new Logs();
        logs.setClassname("com.kuangstudy.service.OrderService");
        logs.setMethod("updateOrder");
        logs.setId(1);
        logs.setParams(params);
        logs.setTime("100ms");
        log.info("你保存日志是：{}", logs);
    }
}

```

用户注册方法进行修改

```java

    /**
     * 用户注册
     *
     * @param user
     */
    public void saveUser(User user) {
        log.info("用户注册....");
        // 记录日志
        logService.saveLog("com.kuangstudy.service.UserService","saveUser","100ms",user.toString());
    }

```

其他的以此类推

#### 02：问题

并没用解决根本性的问题，这个肯定抛弃的。





### 拦截器(springmvc的拦截器)

核心思想：jdk动态代理  必须 实现接口：HandlerInterceptor

> 为什么拦截器一定实现一个接口：HandlerInterceptor ，是因为jdk动态代理必须要要有一个接口，才能创建代理对象

#### 定义拦截器

LogInterceptor实现HandlerInterceptor覆盖三个方法。然后把需要处理的日志在handler进行处理。

- 注册拦截器
- 配置拦截去路由

####  拦截器存在问题

- 和容器(servlet)有关的业务(request,response,freemarker)

-  ==粒度太粗了。因为拦截器是根据路由（RequestMapping）来决定，你只能拦截controller层，不能拦截和处理service层,dao层也就是controller以外的都没处理。==

  ```java
  @PostMaiing("/save/userorder")
  public void saveUserOrder(){// --- 3s ~5s
     // 保存订单
     orderServicee.saveOrder(); // 需要记录日志 限流 100ms
     // 用户积分增加
     userService.updateUserjifen();//这个不需要 不做 800ms
  }
  ```

- 逻辑处理不方便。太笨重了。

  - 如果出现异常呢（这个问题呢？）
  - 在方法执行中，
  - 在方法执行后
  - 执行结果之后

- 如果又多个处理，你必须定义多个拦截器，其实粒度太大了。配置和维护都灾难。

```java
addRegisterInterceptor(new LogInterceptor())
.addPatterns("/user/**","/order/**");

addRegisterInterceptor(new PersmissionInterceptor())
.addPatterns("/user/save","/user/update","/order/makeorder");

```





### jdk动态代理、cglib代码

使用aop的底层机制比如：jdk动态代理去实现或者cglib代理去实现。是没问题。但是面临如下问题：

#### 面临的问题：

- 精细的控制必须自己去编写，
  - 比如：条件判断，哪些方法要进，哪些方法不进。必须自己控制
  - 如果多个代理 代理对象的，代理类创建也必须自己定义（切面，连接点，切点，织入，通知增强，全部要自己去封装）
  - 多代理类怎么执行顺序是如何保证。你也需要自己控制
- ==你必须对springioc的底层机制，和生命周期要非常的熟悉。因为你一定动态代理无论你是用jdk动态代理实现还是cglib代理实现，它们目标都是要把目标对象转化成：代理对象。==
- 因为只有代理对象执行方法，才会进行到代理类（切面）去做逻辑增强处理。(我们处理)





## 07、AOP的重要性

- aop底层是jdk动态代理和cglib代理结合实现。
- 它可以解决上面所有的问题。
- aop它会自动把springioc中的增强对象全部自动转换成代理对象。





## 08、Aop的底层的实现机制

- jdk动态实现
- cglib代理实现

==不论是用那种实现：它的目标都是一致的把springioc的对象转化代理对象。==

这是一个正常springioc的对象

![image-20211221231840577](01%E3%80%81SpringBootAop%E7%AC%94%E8%AE%B0.assets/image-20211221231840577.png)

![image-20211221231923900](01%E3%80%81SpringBootAop%E7%AC%94%E8%AE%B0.assets/image-20211221231923900.png)



## 09、Springioc对象转化代理对象？

为什么用做日志处理，你日志拦截处理和核心点是什么？

- 查看当前类的执行时长
- 根据时长可以判断当前是那个方法耗时，可以根据这个耗时的方法针对的性排查和优化。

### 01、定义日志注解

```java
package com.kuangstudy.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 飞哥
 * @Title: 学相伴出品
 * @Description: 我们有一个学习网站：https://www.kuangstudy.com
 * @date 2021/12/21 23:21
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface KsdLog {

    String value() default "";
}

```

为什么要定义一个注解：

- 它就相当于一个小红旗，那个方法需要被AOP拦截就写上注解，红旗就出现在方法上。
- 因为springaop的切点表达式做了注解的条件处理，可以控制加了注解的方法菜会进行到通知（增强逻辑的位置）
- 不用注解可以吗？当然可以，但是不灵活，看场景
  - execution 表达式 （事务控制）
  - within
  - target （针对性的）
  - args （针对的处理）
  - this
  - annatation(注解)--细粒度



### 02、定义切面

- @Component
  - 让springioc容器管理这个切面，变成一家人。
- @Aspect
  - 标记为切面
- 连接点
  - 切点表达式
- 通知
  - 前置Advice  --- @Before(切点或者切点表达式)
  - 后置Advice --- @After(切点或者切点表达式)
  - 异常Adivce -- @AfterThrowing(切点或者切点表达式)
  - 环绕Advice ---- @Around(切点或者切点表达式)
  - 后置返回Advice ----@AfterRetuning(切点或者切点表达式)

```java
package com.kuangstudy.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @author 飞哥
 * @Title: 学相伴出品
 * @Description: 飞哥B站地址：https://space.bilibili.com/490711252
 * 记得关注和三连哦！
 * @Description: 我们有一个学习网站：https://www.kuangstudy.com
 * @date 2021/12/21 23:31
 */
@Component
@Aspect
@Slf4j
public class LogAspect {

    // 1: 定义切入点，切入点标注有注解@KsdLog的的所有函数，通过 @Pointcut 判断才可以进入到具体增强的通知
    @Pointcut("@annotation(com.kuangstudy.aop.KsdLog)")
    public void logpointcut() {
    }


    @Around("logpointcut()")
    public void beforeAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            // 1:方法执行的开始时间
            long starttime = System.currentTimeMillis();
            // 2:执行真实方法
            MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            System.out.println(signature.getMethod().getName());
            System.out.println(signature.getReturnType());
            System.out.println(signature.getParameterNames());
            System.out.println(signature.getParameterTypes());
            Object methodReturnValue = proceedingJoinPoint.proceed();
            log.info("当前执行方法的返回值是：{}", methodReturnValue);
            // 3:方法执行的结束时间
            long endtime = System.currentTimeMillis();
            // 4：方法的总耗时
            long total = endtime - starttime;
            log.info("当前方法:{}，执行的时间是：{} ms", signature.getMethod().getName(), total);

        } catch (Throwable ex) {
            log.info("执行方法出错.....,{}", ex);
            throw ex;
        }
    }

}

```

在需要拦截的方法上增加@KsdLog注解

```java
package com.kuangstudy.first;

import com.kuangstudy.aop.KsdLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 飞哥
 * @Title: 学相伴出品
 * @Description: 飞哥B站地址：https://space.bilibili.com/490711252
 * 记得关注和三连哦！
 * @Description: 我们有一个学习网站：https://www.kuangstudy.com
 * @date 2021/12/21 22:05
 */
@Slf4j
@Service
public class OrderService {

    @Autowired
    private LogService logService;

    /**
     * 订单注册
     *
     * @param order
     */
    @KsdLog
    public void saveOrder(Order order) {
        log.info("订单注册....");

        // 记录日志
        Logs logs = new Logs();
        logs.setClassname("com.kuangstudy.service.OrderService");
        logs.setMethod("saveOrder");
        logs.setId(1);
        logs.setParams(order.toString());
        logs.setTime("100ms");
        logService.saveLog(logs);
    }

    /**
     * 修改订单
     *
     * @param order
     */
    @KsdLog
    public void updateOrder(Order order) {
        log.info("修改订单....");

        // 记录日志
        Logs logs = new Logs();
        logs.setClassname("com.kuangstudy.service.OrderService");
        logs.setMethod("updateOrder");
        logs.setId(1);
        logs.setParams(order.toString());
        logs.setTime("100ms");
        logService.saveLog(logs);
    }


    /**
     * 删除订单
     *
     * @param orderId
     */
    @KsdLog
    public void delOrder(Integer orderId) {
        log.info("删除订单....{}", orderId);

        // 记录日志
        Logs logs = new Logs();
        logs.setClassname("com.kuangstudy.service.OrderService");
        logs.setMethod("delOrder");
        logs.setId(1);
        logs.setParams(orderId.toString());
        logs.setTime("100ms");
        logService.saveLog(logs);
    }

}

```

运行测试代码如下：

这个时候我们很清晰的看orderService变成了代理对象

![image-20211221231923900](01%E3%80%81SpringBootAop%E7%AC%94%E8%AE%B0.assets/image-20211221231923900.png)

![image-20211221235218587](01%E3%80%81SpringBootAop%E7%AC%94%E8%AE%B0.assets/image-20211221235218587.png)



## 10、结论

- ==在spring底层框架中，它是ioc做核心基础，ioc做的管理项目所有的bean对象。如果这些管理的bean中的任何一个方法只要和aop的切面有管理。就会把这些bean全部转换成代理对象。==
- 因为只有代理对象执行方法，才可以进入到代理类（切面）中去做逻辑增强处理
- 而哪些如果没用加满足条件的方法，全部会自然抛弃和正常执行，而满足条件的就进入对应通知中进行逻辑增强。
- springboot默认情况下代码类型是：cglib代理，而spring的框架默认代理：jdk动态代理。
  - 因为jdk动态代理必须要接口。
  - 但是在有些情况下，开发不需要接口，依然要能够支持代理，现如用jdk动态代理就无法上西安
  - 为解决这种众口难调的情况，有些喜欢面向类变成，有人喜欢面向接口编程spring都做了兼容和支持。











