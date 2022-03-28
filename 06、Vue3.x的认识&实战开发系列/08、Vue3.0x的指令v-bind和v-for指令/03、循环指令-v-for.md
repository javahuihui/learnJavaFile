# 循环指令-v-for

## 官网地址

https://v3.cn.vuejs.org/guide/list.html#%E5%9C%A8-v-for-%E9%87%8C%E4%BD%BF%E7%94%A8%E5%AF%B9%E8%B1%A1



## v-for

列表循环指令：数组和对象

## 分析

其实在vue的数据模型中，数据结构很多中种：字符串String, 数字Number。布尔Boolean，对象 Object (Date，正则，数组)，函数Function。

- 字符串，数字，boolean 都是通过：{{}},v-text/html/v-bind/v-model直接获取。
- 对象。都是通过：{{}},v-text/html/v-bind/v-model直接获取只不过：{{对象.key}}

## 语法

### 简单语法:

```html
<div v-for="obj in dataArrKey"></div>
```

### 索引语法 (把这种写法当成默认)

```html
<div v-for="(obj,index) in dataArrKey"></div>
```

因为在开发过程中，用索引开发和处理逻辑是非常频繁操作。也是后续开发中经常使用的方式。所以一定要写成这种。

### 标识索引语法

```html
<div v-for="(obj,index) in dataArrKey" :key="obj.id"></div>
```



##  数组循环

```html
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>11、属性指令-v-bind.html</title>
</head>
<body>

    <div id="app">
        <p>{{title}}</p>
        <p>{{price}}</p>
        <p>{{mark}}</p>
        <p>{{date}}</p>
        <hr>
        <p>{{user.id}} ，{{user.name}}{{user.age}}</p>
        <hr>
        
        <div v-for="(obj,index) in friends">{{index}}==={{friends[index].id}}，{{friends[index].name}}，{{friends[index].age}}</div>
        <hr>
        <div v-for="(obj,index) in friends">{{index}}==={{obj.id}}，{{obj.name}}，{{obj.age}}</div>

    </div>

    <script src="js/vue.global.js"></script>
    <script>
        var vue = Vue.createApp({
            data(){
               return {
                  title:"学习V-for",
                  price:1499,
                  mark:false,
                  date:new Date(),
                  user:{
                      id:1,
                      name:"小文文",
                      age:20
                  },
                  friends:[
                      {id:1,name:"飞哥1",age:35},
                      {id:2,name:"飞哥2",age:15},
                      {id:3,name:"飞哥3",age:13},
                      {id:4,name:"飞哥4",age:5},
                  ]
               }
            },
            methods:{

            }
        }).mount("#app");

    </script>


</body>
</html>
```

## 对象循环 

> 1： 对象 也能循环吗？
>
> 2：java中的map能循环吗?

- 一般情况下：对象其实不需要循环的。但是为什么还要学习对象的循环呢？

```html
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>11、属性指令-v-bind.html</title>
</head>
<body>

    <div id="app">
        <p>{{user.id}} ，{{user.name}}{{user.age}}</p>
        <hr>
        <div v-for="(value,key,index) in user">{{index}}==={{key}}==={{value}}</div>
    </div>

    <script src="js/vue.global.js"></script>
    <script>
        var vue = Vue.createApp({
            data(){
               return {
                  user:{
                      id:1,
                      name:"小文文",
                      age:20
                  }
               }
            },
            methods:{
            }
        }).mount("#app");
    </script>

</body>
</html>
```



## 实际应用

- 归档
- 统计
- 分组

```html
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>11、属性指令-v-bind.html</title>
</head>
<body>

<div id="app">
    <div v-for="(value,key,index) in yingshous">
        {{key}}：
        <div style="margin-left: 20px;">
            <div v-for="(data,index) in value">
                <span>{{data.month}}==={{data.money}}</span>
            </div>
        </div>
    </div>
    <hr>
    <div v-for="(value,key,index) in yingshous">{{key}}：
        <div style="margin-left: 20px;">
            <div v-for="(data,index) in value">
                <span>{{data.month}}==={{data.money}}</span>
            </div>
        </div>
    </div>
</div>

<script src="js/vue.global.js"></script>
<script>
    var vue = Vue.createApp({
        data() {
            return {
                yingshous: {
                    "2019": [{month: 1, money: 54545}, {month: 2, money: 54545.23}],
                    "2020": [{month: 1, money: 54545}, {month: 2, money: 54545.23}],
                    "2021": [{month: 1, money: 54545}, {month: 2, money: 54545.23}],
                    "2022": [{month: 1, money: 54545}, {month: 2, money: 54545.23}],
                }
            }
        },
        methods: {}
    }).mount("#app");
</script>

</body>
</html>
```





### bean

### mapper

### service

### serviceImpl



### 定义收藏夹的查询接口















