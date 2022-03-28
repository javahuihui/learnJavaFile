# 01、参考官网：
https://v3.cn.vuejs.org/guide/forms.html#%E5%9F%BA%E7%A1%80%E7%94%A8%E6%B3%95
## v-model指令

01：文本指令：v-html / v-text 和插值表达式，名字也必须定义在data中。==
02：事件指令：v-on:click=”事件名”，缩写：[@click](https://github.com/click)=”事件名”，注：事件名定义在：methods中
03：属性指令：v-bind:属性名=”data的key” 缩写 : 属性名=”data的key” .注意动静拼接的问题
04：==控制指令：v-model=”data的key”，用于获取form控制元素的值。如果的多余3个建议使用对象去定义和获取==
05：循环指令：v-for =”(obj,index) in data” 中定义数组的名字” 。
06：条件指令：v-if / v-else-if /v-else 注意中间不能出现标签，否则会出现断层。
07：显示指令：v-show 控制元素的隐藏和显示。（鼠标事件 + v-show /v-if选项卡）
08：渲染指令：v-cloak 
09：其他指令：v-pre  / v-once（== 用的非常的少==）
10、槽指令：v-slot(后续自定义组件使用，自定义代码块的机制)（暂时忽略）



## Vue为什么说是：双向数据绑定

就是体现在这个指令上：v-model
因为v-model是唯一一个可以改变视图----改变数据的指令

## v-model的作用范围

```
v-model指令：只能用在form表元素上( input text,password,checkbox ,radio) ,textarea, select。
```

除了这些元素意外都不能使用v-model。比如：

```html
<div v-model="username"></div>
```


## 01、基础用法

```
你可以用 v-model 指令在表单 <input>、<textarea> 及 <select> 元素上创建双向数据绑定。它会根据控件类型自动选取正确的方法来更新元素。尽管有些神奇，但 v-model 本质上不过是语法糖。它负责监听用户的输入事件来更新数据，并在某种极端场景下进行一些特殊处理。
```

- 获取表单元素的value 
- 同步表单的值--比如回填数据，selected，checked选中



## 02、友情提示
```
> **v-model 会忽略所有表单元素的 value、checked、selected attribute 的初始值。它将始终将当前活动实例的数据作为数据来源。你应该通过 JavaScript 在组件的 data 选项中声明初始值。**

v-model 在内部为不同的输入元素使用不同的 property 并抛出不同的事件：

- text 和 textarea 元素使用 value property 和 input 事件；
- checkbox 和 radio 使用 checked property 和 change 事件；
- select 字段将 value 作为 prop 并将 change 作为事件。
```


案例分析：

```html
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>18、表单属性指令-v-model.html</title>
</head>
<body>

<div id="app">
    **v-model 会忽略所有表单元素的 value、checked、selected attribute 的初始值。它将始终将当前活动实例的数据作为数据来源。你应该通过
    JavaScript 在组件的 data 选项中声明初始值。*
    <input type="text" @input="inputevent" :value="username">
    <input type="text" v-model="username">
    <h1>你输入的值是：{{username}}</h1>

</div>

<script src="js/vue.global.js"></script>
<script>
    var vm = Vue.createApp({
        created(){
        },
        data() {
            return {
                username:"yykk"
            }
        },
        methods:{
            inputevent($event){
                this.username = $event.target.value;
            }
        }

    }).mount("#app");
</script>

</body>
</html>
```



# 02、单输入框--text

## 格式

```html
<input type="text" id="username">
```

## javascipt获取值的方式

```js
var username = document.getElmentById("username").value;
```

## jQuery写法获取值的方式

```js
var username = $("#username").val();
```

## vue获取的的写法--直接获取

```js
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>18、表单属性指令-v-model.html</title>
</head>
<body>

<div id="app">
    <input type="text" v-model="username">
    <h1>你输入的值是：{{username}}</h1>
    <button @click="loginbtn">登录</button>
</div>

<script src="js/vue.global.js"></script>
<script>
    var vm = Vue.createApp({
        created(){
        },
        data() {
            return {
                username:""
            }
        },
        methods:{
            loginbtn(){
                var username = this.username;
                alert("你登录的用户是===>" + username)
            }
        }

    }).mount("#app");
</script>

</body>
</html>
```



## vue获取的的写法--增强处理

### lazy

默认情况下，vue对输入框textarea,text，默认是采用@input事件，输入就进行替换和渲染。这样造成性能开销和损耗。所以vue就用lazy来解决这个同步实时的问题，讲@input事件转换成@change事件，当用户输入完毕以后，释放焦点时候触发同步：

```html
<div id="app">
    <input type="text" v-model.lazy="username">
    <h1>你输入的值是：{{username}}</h1>
    <button @click="loginbtn">登录</button>
</div>
```

不建议，建议实时同步触发。

### trim（==推荐必须掌握==）

```html
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>18、表单属性指令-v-model.html</title>
</head>
<body>

<div id="app">
    <input type="text" v-model.trim="username">
    <h1>你输入的值是：{{username.length}}</h1>
    <input type="text" v-model.trim="username2">
    <h1>你输入的值是：{{username2.length}}</h1>
    <button @click="loginbtn">登录</button>
</div>

<script src="js/vue.global.js"></script>
<script>
    var vm = Vue.createApp({
        created(){
        },
        data() {
            return {
                username:"",
                username2:""
            }
        },
        methods:{
            loginbtn(){
                var username = this.username;
                alert("你登录的用户是===>" + username)
            }
        }

    }).mount("#app");
</script>

</body>
</html>
```

使用.trim会自动剔除左右空格。

### number（==推荐必须掌握==）

提示：不会做校验，

- 如果输入的不是数字，直接输出
- 如果输入的以数字开头+混合其他，就直接输出数字 (parseFloat(number))
- 如果是数字直接输出数字。

```js
var number = "10px";
parseFloat(number)===10
```





# 03、多行输入 - textarea



## 格式

```html
<textarea id="intro"></textarea>
```

## javascipt获取值的方式

```js
var intro = document.getElmentById("intro").value;
```

## jQuery写法获取值的方式

```js
var intro = $("#intro").val();
```

## vue获取的的写法--直接获取

```
<textarea id="intro" v-model="intro"></textarea>
```

````html
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>18、表单属性指令-v-model-多行入框.html</title>
</head>
<body>

<div id="app">
    <textarea  v-model.lazy.trim.number="intro" style="height: 200px;width: 400px;resize:none;"></textarea>
    <h1>你输入的字符长度是：{{intro.length}}</h1>
    <button @click="loginbtn">登录</button>
</div>

<script src="js/vue.global.js"></script>
<script>
    var vm = Vue.createApp({
        created(){
        },
        data() {
            return {
                intro:"你好"
            }
        },
        methods:{
            loginbtn(){
                var intro = this.intro;
                alert("你获取的内容是:" + intro)
            }
        }

    }).mount("#app");
</script>

</body>
</html>
````



# 04、关于label妙用

##  label和form元素的化学反应

```html
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>18、表单属性指令-v-model-多行入框.html</title>
</head>
<body>

<div id="app">
    <p><label for="username">用户名：</label><input type="text" id="username"></p>
    <p><label>用户名：<input type="text" id="username2"></label></p>

    <hr>
    <p><label><input type="checkbox" name="hobbys" value="1">篮球</label></p>
    <p><label><input type="checkbox" name="hobbys" value="2">足球</label></p>
    <p><label><input type="checkbox" name="hobbys" value="3">羽毛球</label></p>

    <p><input type="checkbox" name="hobbys" id="hobbys1" value="1"><label for="hobbys1">篮球</label></p>
    <p><input type="checkbox" name="hobbys" id="hobbys2" value="2"><label for="hobbys2">足球</label></p>
    <p><input type="checkbox" name="hobbys" id="hobbys3" value="3"><label for="hobbys3">羽毛球</label></p>


    <hr>
    <p><label><input type="radio" name="gender" value="0">女</label></p>
    <p><label><input type="radio" name="gender" value="1">男</label></p>
    <p><label><input type="radio" name="gender" value="2">保密</label></p>
    <hr>
    <p><input type="radio" name="gender" id="gender0" value="0"><label  for="gender0">女</label></p>
    <p><input type="radio" name="gender" id="gender1" value="1"><label  for="gender1">男</label></p>
    <p><input type="radio" name="gender" id="gender2" value="2"><label  for="gender2">保密</label></p>



</div>

</body>
</html>
```

被label包裹或者用for指定元素，可以直接选中文件进行焦点的获取。

- 所以在开发中你指定为什么前端都喜欢用label标签去包裹form元素，就是因为有这样的化学反应。
- 同时也告诉你一个道理，如果要增强用户体验，你未来的开发中，尽量用label去包裹元素。



#  05、复选框

## 格式

```html
<form action="" id="form">
    <p><input type="checkbox" value="篮球" name="hobbys"><label>篮球</label></p>
    <p><input type="checkbox" value="足球" name="hobbys" checked><label>足球</label></p>
    <p><input type="checkbox" value="羽毛球" name="hobbys" ><label>羽毛球</label></p>
</form>
```

### javascipt获取值的方式

```js
# 获取所有
var formdom = document.getElementById("form");
var hobbysarr = formdom.hobbys;
var arr = [];
for(var i=0;i<hobbysarr.length;i++){
    arr.push(hobbysarr[i].value);
}
alert(arr)

```

```js
#获取选中的
var formdom = document.getElementById("form");
var hobbysarr = formdom.hobbys;
var arr = [];
for(var i=0;i<hobbysarr.length;i++){
    if(hobbysarr[i].checked) {
        arr.push(hobbysarr[i].value);
    }
}
alert(arr)

```

## jQuery写法获取值的方式

```js
# 所有，不管选中不选中
var hobbys = $("input[type='checkbox'][name='hobbys']").val();
# 获取选中的
var hobbys = $("input[type='checkbox'][name='hobbys']:checked").val();
```

## vue获取的的写法--直接获取

```html
<div id="app">
    <p><input type="checkbox" v-model="hobbys" value="篮球" id="hobbys1"><label for="hobbys1">篮球</label></p>
    <p><input type="checkbox" v-model="hobbys" value="足球" id="hobbys2"><label for="hobbys2">足球</label></p>
    <p><input type="checkbox" v-model="hobbys" value="羽毛球" id="hobbys3" ><label for="hobbys3">羽毛球</label></p>
    <p>你选择的元素是：{{hobbys}}</p>
</div>
```



```html
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>18、表单属性指令-v-model-checkbox复选框.html</title>
</head>
<body>

<div id="app">
    <p><input type="checkbox" v-model="hobbys" value="篮球" id="hobbys1"><label for="hobbys1">篮球</label></p>
    <p><input type="checkbox" v-model="hobbys" value="足球" id="hobbys2"><label for="hobbys2">足球</label></p>
    <p><input type="checkbox" v-model="hobbys" value="羽毛球" id="hobbys3"><label for="hobbys3">羽毛球</label></p>
    <p>你选择的元素是：{{hobbys}}</p>
</div>

<script src="js/vue.global.js"></script>
<script>
    var vm = Vue.createApp({
        created(){
        },
        data() {
            return {
                hobbys:[]
            }
        },
        methods:{
            loginbtn(){
                var intro = this.intro;
                alert("你获取的内容是:" + intro)
            }
        }

    }).mount("#app");
</script>

</body>
</html>
```

### 回填数据

如果你要回调数据的话，直接改变hobbys数组即可

```js
 data() {
     return {
     	hobbys:["篮球","羽毛球"]
     }
 },
```

![](https://itbooking.oss-cn-guangzhou.aliyuncs.com/notes/2021/12/01/6fb94f6e-e03c-42d0-a243-8405e5d40087.png)

### 获取value和文本

```html
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>18、表单属性指令-v-model-checkbox复选框.html</title>
</head>
<body>

<div id="app">
    <p v-for="(item,index) in hobbysArr" :key="item.id"><input type="checkbox" v-model="hobbys" v-bind:value="item" :id="'hobbys'+index"><label :for="'hobbys'+index">{{item.name}}</label></p>
    <p>你选择的元素是：{{hobbys}}</p>
</div>

<script src="js/vue.global.js"></script>
<script>
    var vm = Vue.createApp({
        created(){
        },
        data() {
            return {
                hobbys:[],
                hobbysArr:[
                    {id:1,name:"篮球1"},
                    {id:2,name:"篮球2"},
                    {id:3,name:"篮球3"}
                ]
            }
        },
        methods:{
            loginbtn(){
                var intro = this.intro;
                alert("你获取的内容是:" + intro)
            }
        }

    }).mount("#app");
</script>

</body>
</html>
```

### 默认规则

如果你data模型中数据值和value一致，checked会自动选中。





# 06、单选框

## 格式

```html
<form action="" id="form">
    <p><input type="radio" value="男" name="gender"><label>男</label></p>
    <p><input type="radio" value="女" name="gender" checked><label>女</label></p>
    <p><input type="radio" value="保密" name="gender" ><label>保密</label></p>
</form>
```

## javascipt获取值的方式

```js
# 获取所有
var formdom = document.getElementById("form");
var genderArr = formdom.gender;
var arr = [];
for(var i=0;i<genderArr.length;i++){
    arr.push(genderArr[i].value);
}
alert(arr)

# 选中的取出来
var formdom = document.getElementById("form");
var genderArr = formdom.gender;
var arr = [];
for(var i=0;i<genderArr.length;i++){
    if(genderArr[i].checked){
    	arr.push(genderArr[i].value);
    }
}
alert(arr[0])

```

## jQuery写法获取值的方式

```js
# 所有，不管选中不选中
var gender = $("input[type='radio'][name='gender']").val();
# 获取选中的
var gender = $("input[type='radio'][name='gender']:checked").val();
var gendertext = $("input[type='radio'][name='gender']:checked").next().text();
```

## vue获取的的写法--直接获取

```html
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>18、表单属性指令-v-model-radio单选框.html</title>
</head>
<body>

<div id="app">
    <p><input type="radio" v-model="gender" value="男" id="male"><label for="male">男</label></p>
    <p><input type="radio" v-model="gender" value="女" id="female"><label for="female">女</label></p>
    <p><input type="radio" v-model="gender" value="保密" id="baomi" ><label for="baomi">保密</label></p>
    <p>你选择的元素是：{{gender}}</p>
</div>

<script src="js/vue.global.js"></script>
<script>



    var vm = Vue.createApp({
        created(){
        },
        data() {
            return {
                gender:""
            }
        },
        methods:{
            loginbtn(){
                var intro = this.gender;
                alert("性别是:" + gender)
            }
        }

    }).mount("#app");
</script>

</body>
</html>
```

### 获取value和文本

```html
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>18、表单属性指令-v-model-radio单选框-02.html</title>
</head>
<body>

<div id="app">
    <p><input type="radio" v-model="gender" :value="genderArr[0]" id="male"><label for="male">男</label></p>
    <p><input type="radio" v-model="gender" :value="genderArr[1]" id="female"><label for="female">女</label></p>
    <p><input type="radio" v-model="gender" :value="genderArr[2]" id="baomi" ><label for="baomi">保密</label></p>
    <p>你选择的元素是：{{gender.gender}}，{{gender.name}}</p>
</div>

<script src="js/vue.global.js"></script>
<script>
    var vm = Vue.createApp({
        created(){
        },
        data() {
            return {
                gender:"",
                genderArr:[{id:1,gender:1,name:"男"},{id:2,gender:0,name:"女"},{id:3,gender:2,name:"保密"}]
            }
        },
        methods:{
            loginbtn(){
                var intro = this.gender;
                alert("性别是:" + gender)
            }
        }

    }).mount("#app");
</script>

</body>
</html>
```

### 默认规则

如果你data模型中数据的值和value一致，checked会自动选中。



# 07、select框

## 注意事项

- 如果select的option的value不指定，那么value等text。建议大家不要搞迷惑行为。
- value和text不一致的情况下



## 格式

```html
<select id="education" onchange="changeEducation(this)">
        <option value="">--请选择学历--</option>
        <option value="1">小学</option>
        <option value="2">初中</option>
        <option value="3">高中</option>
        <option value="4">本科</option>
        <option value="5">硕士</option>
        <option value="6">博士</option>
    </select>
```

## javascipt获取值的方式

```js
function changeEducation(obj){
    // 1:获取 select元素本身
    var selectDom = obj; // document.getElementById("education");
    // 2: 获取选中的options的索引
    var selectIndex = selectDom.selectedIndex;
    // 3: 根据索引找到select中索引为selectedIndex的option
    var optionDom = selectDom.children[selectIndex]; // selectDom.options[selectIndex];
    alert(optionDom.value +"==="+optionDom.text)
}

```

## jQuery写法获取值的方式

```js
## 获取value
var education = $("#education").val();
var education = $("#education").find("option:selected").val();
## 获取文本
var educationtext = $("#education").find("option:selected").text();
```

## vue获取的的写法--直接获取

```html
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>18、表单属性指令-v-model-select元素.html</title>
</head>
<body>

<div id="app">
    <h3>单选选择框</h3>
    <select v-model="education">
        <option value="">---请选择学历---</option>
        <option v-for="(option,index) in options" v-bind:value="option">{{option.atxt}}</option>
    </select>
    <p>你获取的学历是：{{education.aval}} == {{education.atxt}}</p>
</div>

<script src="js/vue.global.js"></script>
<script>
    var vm = Vue.createApp({
        created() {
        },
        data() {
            return {
                education: "",
                options:[
                    {aval:1,atxt:"小学"},
                    {aval:2,atxt:"初中"},
                    {aval:3,atxt:"高中"},
                    {aval:4,atxt:"本科"},
                    {aval:5,atxt:"硕士"},
                    {aval:6,atxt:"博士"}
                ]
            }
        },
        methods: {

        }


    }).mount("#app");
</script>

</body>
</html>
```






















