# Java1.8新特性 - Stream流

授课老师：学相伴飞哥



## 01、课程大纲

01、Stream流概述
02、Stream流的应用



## 02、Stream流概述

概念：Stream 是Java8 提出的一个新概念，不是输入输出的 Stream 流，而是一种用函数式编程方式在集合类上进行复杂操作的工具。简而言之，是以内部迭代的方式处理集合数据的操作，内部迭代可以将更多的控制权交给集合类。Stream 和 Iterator 的功能类似，只是 Iterator 是以外部迭代的形式处理集合数据的操作。

在Java8以前，对集合的操作需要写出处理的过程，如在集合中筛选出满足条件的数据，需要一 一遍历集合中的每个元素，再把每个元素逐一判断是否满足条件，最后将满足条件的元素保存返回。而Stream 对集合筛选的操作提供了一种更为便捷的操作，只需将实现函数接口的筛选条件作为参数传递进来，Stream会自行操作并将合适的元素同样以stream 的方式返回，最后进行接收即可。

- 集合和数组在遍历元素的时候十分冗余，受到函数式编程以及流水线思想的启发，我们可以将常见的操作封装成比较简单的方法，比如遍历的时候直接调用一个方法即可，而不用写冗余的循环程序。这就是stream流对象的由来。
- Stream是一个接口，有两种方式来进行创建流对象。
  一是调用 Stream.接口中的of方法。
  二是调用集合或者数组中的strain方法来获取 Stream.流对象
- Stream对象中常用的方法有：遍历元素，筛选元素，跳过元素，截取元素，计数，把流对象拼接
  对流对象中的数据元素进行转换。



### Stream两种操作

- 中间操作

  intermediate  operation 中间操作：中间操作的结果是刻画、描述了一个Stream，并没有产生一个新集合，这种操作也叫做==惰性求值方法。==

  对应的方法如下：

  ```
  这是所有Stream中间操作的列表：
  过滤()==>filter()
  地图()==>map()
  转换()==>flatMap()
  不同()==>distinct() 
  排序()==>sorted()
  窥视()==>peek（）
  限制()==>limit()
  跳跃()==>skip()
  叠加()==>reduce()
  ```

  

- 终止操作

  terminal operation 终止操作：最终会从Stream中得到值。说白了：就是可以直接得到结果

  ```
  循环()==>foreach()
  总计()==>count()
  收集()==>collect()
  andMatch()
  noneMatch()
  allMatch()
  findAny()
  findFirst()
  min()
  max()
  ```
  
- 总结如图

  ![img](04%E3%80%81Java%E5%9F%BA%E7%A1%80_stream%E6%B5%81.assets/70)

如何区分这2种操作呢？可以根据操作的返回值类型判断，如果返回值是Stream，则该操作是中间操作，如果返回值是其他值或者为空，则该操作是终止操作。

如下图的前2个操作是中间操作，只有最后一个操作是终止操作。

![img](https://img-blog.csdn.net/20170912223227366?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxNDUxOTQ2MQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)

可以形象地理解Stream的操作是对一组粗糙的工艺品原型（即对应的 Stream 数据源）进行加工成颜色统一的工艺品（即最终得到的结果），第一步筛选出合适的原型（即对应Stream的 filter 的方法），第二步将这些筛选出来的原型工艺品上色（对应Stream的map方法），第三步取下这些上好色的工艺品（即对应Stream的 collect(toList())方法）。在取下工艺品之前进行的操作都是中间操作，可以有多个或者0个中间操作，但每个Stream数据源只能有一次终止操作，否则程序会报错。

### 准备工作

```java
package com.streamdemo;

/**
 * @author 飞哥
 * @Title: 学相伴出品
 * @Description: 飞哥B站地址：https://space.bilibili.com/490711252
 * 记得关注和三连哦！
 * @Description: 我们有一个学习网站：https://www.kuangstudy.com
 * @date 2021/10/12 14:33
 */
public class User {

    private Integer id;
    private Integer age;
    private Integer sex;
    private Double shenjia;
    private String username;
    private String password;

    public User(Integer id, String username, String password, Integer age, Integer sex, Double shenjia) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.age = age;
        this.sex = sex;
        this.shenjia = shenjia;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Double getShenjia() {
        return shenjia;
    }

    public void setShenjia(Double shenjia) {
        this.shenjia = shenjia;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", age=" + age +
                ", sex=" + sex +
                ", shenjia=" + shenjia +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
```

```java
package com.streamdemo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 飞哥
 * @Title: 学相伴出品
 * @Description: 飞哥B站地址：https://space.bilibili.com/490711252
 * 记得关注和三连哦！
 * @Description: 我们有一个学习网站：https://www.kuangstudy.com
 * @date 2021/10/12 14:41
 */
public class StreamDemo {

    public static void main(String[] args) {
        List<User> userList = new ArrayList<>();
        userList.add(new User(1, "yykk", "111111", 34, 1, 34600d));
        userList.add(new User(2, "祈福", "2222222", 24, 0, 883600d));
        userList.add(new User(3, "小王", "3333333", 24, 1, 734090d));
        userList.add(new User(4, "小楠", "4444444", 14, 0, 33400d));
        userList.add(new User(5, "小张", "55555", 29, 1, 140000d));

    }
}

```



## filter方法-中间操作-过滤（重要）

把条件满足的，过滤匹配出来。

### 语法

```java
Stream<T> filter(Predicate<? super T> predicate);
```

这个方法，传入一个Predicate的函数接口，关于Predicate函数接口定义，可以查看《JAVA8 Predicate接口》，这个接口传入一个泛型参数T，做完操作之后，返回一个boolean值；filter方法的作用，是对这个boolean做判断，返回true判断之后的对象，下面一个案例，可以看到怎么使用

### 案例

```java
package com.streamdemo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 飞哥
 * @Title: 学相伴出品
 * @Description: 飞哥B站地址：https://space.bilibili.com/490711252
 * 记得关注和三连哦！
 * @Description: 我们有一个学习网站：https://www.kuangstudy.com
 * @date 2021/10/12 14:41
 */
public class StreamDemo {

    public static void main(String[] args) {
        List<User> userList = new ArrayList<>();
        userList.add(new User(1, "yykk", "111111", 34, 1, 34600d));
        userList.add(new User(2, "祈福", "2222222", 24, 0, 883600d));
        userList.add(new User(3, "小王", "3333333", 24, 1, 734090d));
        userList.add(new User(4, "小楠", "4444444", 14, 0, 33400d));
        userList.add(new User(5, "小张", "55555", 29, 1, 140000d));

        // 1： filter过滤
        List<User> collect = userList.stream().filter(res -> res.getSex()==1).collect(Collectors.toList());
        collect.forEach(System.out::println);

    }
}

```

运行结果

```java
User{id=1, age=34, sex=1, shenjia=34600.0, username='yykk', password='111111'}
User{id=3, age=24, sex=1, shenjia=734090.0, username='小王', password='3333333'}
User{id=5, age=29, sex=1, shenjia=140000.0, username='小张', password='55555'
```



## map方法-中间操作-改造变换（重要）

把条件满足的，过滤匹配出来。

### 语法

```java
<R> Stream<R> map(Function<? super T, ? extends R> mapper);
```

这个方法传入一个Function的函数式接口，接口定义可以查看[《JAVA8 Function接口》](https://blog.csdn.net/qq_28410283/article/details/80615629)，这个接口，接收一个泛型T，返回泛型R，map函数的定义，返回的流，表示的泛型是R对象，这个表示，调用这个函数后，可以改变返回的类型，先看下面的案例

### 案例

```java
package com.streamdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author 飞哥
 * @Title: 学相伴出品
 * @Description: 飞哥B站地址：https://space.bilibili.com/490711252
 * 记得关注和三连哦！
 * @Description: 我们有一个学习网站：https://www.kuangstudy.com
 * @date 2021/10/12 14:41
 */
public class StreamDemo {

    public static void main(String[] args) {
        List<User> userList = new ArrayList<>();
        userList.add(new User(1, "yykk", "111111", 34, 1, 34600d));
        userList.add(new User(2, "祈福", "2222222", 24, 0, 883600d));
        userList.add(new User(3, "小王", "3333333", 24, 1, 734090d));
        userList.add(new User(4, "小楠", "4444444", 14, 0, 33400d));
        userList.add(new User(5, "小张", "55555", 29, 1, 140000d));

        // 1： 使用map获取集合中username列
        List<String> userNameList = userList.stream().map(res -> res.getUsername()).collect(Collectors.toList());
        userNameList.forEach(System.out::println);

        // 2: 使用map获取集合中的id,username,age,sex,shenjia，排除password
        List<Map<String, Object>> collect = userList.stream().map(user -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id",user.getId());
            map.put("age",user.getAge());
            map.put("sex",user.getSex());
            map.put("shenjia",user.getShenjia());
            return map;
        }).collect(Collectors.toList());
        collect.forEach(System.out::println);
        
        /*
         // 4: 使用map + min/max/count/sum快速求所有用户
        List<Map<String, Object>> mapList = userList.stream().map(new Function<User, Map<String, Object>>() {
            @Override
            public Map<String, Object> apply(User user) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", user.getId());
                map.put("age", user.getAge());
                map.put("sex", user.getSex());
                map.put("shenjia", user.getShenjia());
                return map;
            }
        }).collect(Collectors.toList());
        mapList.forEach(System.out::println);
        */
        

        // 3: 快速清空password
        List<User> userList1 = userList.stream().map(user -> {
            user.setPassword("");
            return user;
        }).collect(Collectors.toList());
        userList1.forEach(System.out::println);

        // 4: 使用map + min/max/count/sum快速求所有用户
        Integer maxage = userList.stream().map(user -> user.getAge()).max((a,b)->a-b).get();
        Integer minage = userList.stream().map(user -> user.getAge()).min((a,b)->a-b).get();
        long count = userList.stream().map(user -> user.getAge()).count();
        long sumcount1 = userList.stream().map(user -> user.getAge()).reduce((a,b)->a+b).get();
        long sumcount2 = userList.stream().mapToInt(user -> user.getAge()).sum();
        System.out.println(maxage);
        System.out.println(minage);
        System.out.println(count);
        System.out.println(sumcount1);
        System.out.println(sumcount2);
        System.out.println(sumcount1/count);
    }
}

```

运行结果

```java
User{id=1, age=34, sex=1, shenjia=34600.0, username='yykk', password='111111'}
User{id=3, age=24, sex=1, shenjia=734090.0, username='小王', password='3333333'}
User{id=5, age=29, sex=1, shenjia=140000.0, username='小张', password='55555'
```

可以看到，我们把Integer，变成了String输出，把Emp对象里的name字符串，单独输出；现在，我们只看到了一个forEach的终端操作，后面，我们会看到，更多的终端操作，把map操作后，改变的对象类型，返回各种类型的集合，或者对数字类型的，返回求和，最大，最小等的操作；



## flatMap方法-中间操作-过滤

参考：https://www.cnblogs.com/xfyy-2020/p/13289066.html

### 语法

```java
<R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper);
```

这个接口，跟map一样，接收一个Fucntion的函数式接口，不同的是，Function接收的泛型参数，第二个参数是一个Stream流；方法，返回的也是泛型R，具体的作用是把两个流，变成一个流返回，下面，我们看一个案例，来详细解答，怎么把两个流的内容，变成一个流的内容

```java
package com.streamdemo;

import java.util.Objects;

/**
 * @author 飞哥
 * @Title: 学相伴出品
 * @Description: 飞哥B站地址：https://space.bilibili.com/490711252
 * 记得关注和三连哦！
 * @Description: 我们有一个学习网站：https://www.kuangstudy.com
 * @date 2021/10/12 14:33
 */
public class User {

    private Integer id;
    private Integer age;
    private Integer sex;
    private Double shenjia;
    private String username;
    private String password;

    public User(Integer id, String username, String password, Integer age, Integer sex, Double shenjia) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.age = age;
        this.sex = sex;
        this.shenjia = shenjia;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Double getShenjia() {
        return shenjia;
    }

    public void setShenjia(Double shenjia) {
        this.shenjia = shenjia;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", age=" + age +
                ", sex=" + sex +
                ", shenjia=" + shenjia +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(age, user.age) &&
                Objects.equals(sex, user.sex) &&
                Objects.equals(shenjia, user.shenjia) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, age, sex, shenjia, username, password);
    }
}

```



### 案例

```java
package com.streamdemo;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author 飞哥
 * @Title: 学相伴出品
 * @Description: 飞哥B站地址：https://space.bilibili.com/490711252
 * 记得关注和三连哦！
 * @Description: 我们有一个学习网站：https://www.kuangstudy.com
 * @date 2021/10/12 14:41
 */
public class StreamDemo2 {

    public static void main(String[] args) {
        List<User> userList = new ArrayList<>();
        userList.add(new User(1, "yykk", "111111", 34, 1, 34600d));
        userList.add(new User(2, "祈福", "2222222", 24, 0, 883600d));
        userList.add(new User(3, "小王", "3333333", 24, 1, 734090d));
        userList.add(new User(4, "小楠", "4444444", 14, 0, 33400d));
        userList.add(new User(5, "小张", "55555", 29, 1, 140000d));

        List<User> userList2 = new ArrayList<>();
        userList2.add(new User(1, "yykk", "111111", 34, 1, 34600d));
        userList2.add(new User(7, "祈福", "2222222", 24, 0, 883600d));
        userList2.add(new User(8, "小王", "3333333", 24, 1, 734090d));
        userList2.add(new User(9, "小楠", "4444444", 14, 0, 33400d));
        userList2.add(new User(10, "小张", "55555", 29, 1, 140000d));


        //flatmap一般用于：
        // 使用Java8实现集合的并、交、差操作
        // 具体的作用是把两个流，变成一个流返回

        //案例一：合并两个集合
        System.out.println("============案例一 合并==============");
        List<User> collect = userList.stream().flatMap(user->userList2.stream()).collect(Collectors.toList());
        collect.forEach(System.out::println);

        System.out.println("============案例二取交集==============");
        // 案例二取交集
        List<User> userList1 = userList.stream().filter(userList2::contains).collect(Collectors.toList());
        userList1.forEach(System.out::println);

        System.out.println("===========案例三取并集===============");
        // 案例三取并集
        List<User> userList3= Stream.of(userList,userList2).flatMap(Collection::stream).distinct().collect(Collectors.toList());
        userList3.forEach(System.out::println);

        System.out.println("=========== 案例四取差集===============");
        // 案例四 取差集
        List<User> userList4= userList.stream().filter(user -> !userList2.contains(user)).collect(Collectors.toList());
        userList4.forEach(System.out::println);

        int sum = Stream.of(userList,userList2).flatMapToInt(user -> user.stream().mapToInt(u->u.getAge())).sum();
        System.out.println(sum);

    }
}

```

运行结果

```java

```



## 参考文献

>  参考网址：https://blog.csdn.net/qq_28410283/article/details/80642786




