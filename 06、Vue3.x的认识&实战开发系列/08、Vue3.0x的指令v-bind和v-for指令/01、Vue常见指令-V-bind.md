# Vue常见指令

01：文本指令：v-html / v-text 和插值表达式，名字也必须定义在data中。==
02：事件指令：v-on:click=”事件名”，缩写：[@click](https://github.com/click)=”事件名”，注：事件名定义在：methods中
03：属性指令：v-bind:属性名=”data的key” 缩写 : 属性名=”data的key” .注意动静拼接的问题
04：控制指令：v-model=”data的key”，用于获取form控制元素的值。如果的多余3个建议使用对象去定义和获取
05：循环指令：v-for =”(obj,index) in data” 中定义数组的名字” 。
06：条件指令：v-if / v-else-if /v-else 注意中间不能出现标签，否则会出现断层。
07：显示指令：v-show 控制元素的隐藏和显示。（鼠标事件 + v-show /v-if选项卡）
08：渲染指令：v-cloak
09：其他指令：v-pre v-once（== 用的非常的少==）
10、槽指令：v-slot(后续自定义组件使用，自定义代码块的机制)（暂时忽略）



# 属性指令 -v-bind

##  作用范围

元素的属性

## 语法

v-bind或者 简写：`:`

## 比如 

```html
<div v-bind:title="title"></div>
#或者
<div :title="title"></div>
```



## 如何认识元素标签

- 在前端学习，我们会学习元素的标签，有：<div> <p>  <a>
- 标签上分为几部分
  - 标签名 div p a from
  - 标签属性  
    - 自有属性：class,id,title
    - 自定属性：用户自己决定 username
  - 行为事件
    - js事件去绑定 onclick
  - 样式控制
    - style
    - class
  - 文本域
- 总结

```html
<div class="container" id="box" title="我是一个div" @click="alert('1')" username="我是飞哥">{{test}}/v-html/v-text</div>
```

错觉：

```
<div class="container" id="box" title="{{title}}" @click="alert('1')" username="我是飞哥">{{test}}/v-html/v-text</div>
```



## 案例 - 初认识

v-bind指令：其实就一种解决属性和data动态渲染的一种机制。

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
        <h1 title="">{{title}}</h1>
        <div class="container" id="box1" v-bind:title="title" onclick="alert('1')" v-bind:username="username">我是文本域</div>
        <div class="container" id="box2" :title="title" onclick="alert('1')" :username="username">我是文本域</div>
    </div>

    <script src="js/vue.global.js"></script>
    <script>
        var vue = Vue.createApp({
            data(){
               return {
                   title:"我是一个div",
                   username:"我是飞哥"
               }
            },
            methods:{

            }
        }).mount("#app");

    </script>


</body>
</html>
```







## 案例 - 动静拼接

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
    <h1 title="">{{title}}</h1>
    <p v-for="(user,index) in users">
        {{user.id}}==={{user.name}} <a :href="'/detail?id='+user.id+'&name='+user.name">点我查看明细</a>
    </p>
</div>

<script src="js/vue.global.js"></script>
<script>
    var vue = Vue.createApp({
        data() {
            return {
                users: [
                    {id: 1, name: "zhangsan"},
                    {id: 2, name: "xiaowen"},
                    {id: 3, name: "xiaofei"},
                    {id: 4, name: "xiaoshi"},
                    {id: 5, name: "xiaocai"},
                ]
            }
        },
        methods: {}
    }).mount("#app");

</script>


</body>
</html>
```



















