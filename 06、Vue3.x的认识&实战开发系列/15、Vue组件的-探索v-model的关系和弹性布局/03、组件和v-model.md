# 关于v-model的和组件的关系



##  01、使用契机

- 未来你有机会去封装form表单组件
- 学习这个目的：为帮助大家以后能够看懂一些第三放的组件实现原理



## 02、分析为什么要单独学习v-model和组件的关系

### 02-01、v-model双向数据绑定

我们在指定在表单上使用v-model指令可以实现数据的双向数据绑定。比如：

```html
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>01、Vue的组件和form元素的问题-01.html</title>
</head>
<body>


    <div id="app">
        <input type="text" v-model="message">
        <p>你输入的内容是：{{message}}</p>
    </div>

    <script src="js/vue.global.js"></script>
    <script>
        var app = Vue.createApp({
            data(){
                return {
                    message:"你们都是一群小可爱!!!!"
                }
            }
        });
        app.mount("#app");
    </script>

</body>
</html>
```

- 上面的的message会自动的同步到input框中，同时如果去修改输入框的内容，也会同步给数据。



### v-model的偏见

> v-model同步是数据的时候，它原理是一个js加载的机制，它默认会把form元素的默认的value,checked,selected，disbaled全部忽略。

==偏见：99%的小伙伴刚刚接触v-model的时候都觉得，我只要给input的value赋值你就可以给v-model的数据模型自动赋值==

- 通过分析你要明白一个道理，freemarker它们替换的过程在服务器完成替换，然后返回给浏览器进行渲染。
- 浏览器等所以的dom节点加载完毕之后，开始加载css js 比如：vue.js
- 触发vue生命周期，开始把vue的数据模型data的数据同步给视图。此时data的message = "" 这个会页面中原本的input的内容清空。



##  $nextTick

- 如果你未来要vue的生命周期中，其实一般就是指：==created或者mounted去操作操作原生js的dom的时候==，建议使用$nextTick的函数进行包裹。它作用：vue加载dom完毕以后会触发的回调函数。





## form元素封装组件的问题

分析如下代码：

```html
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>01、Vue的组件和form元素的问题-01.html</title>
</head>
<body>


<div id="app">
    <ksd-input  v-model="user"></ksd-input>
</div>

<script src="js/vue.global.js"></script>
<script>
    var app = Vue.createApp({
        data(){
            return {
                user:""
            }
        }
    });

    app.component("KsdInput",{
        template:"<input type=\"text\"> <p>你输入的内容是：{{message}}</p>",
        data(){
        }
    })

    app.mount("#app");
</script>

</body>
</html>
```

- 封装以个input输入框组件KsdInput
- 问题：你把v-model写组件里面。是不耦合的，比如老季同学用ksd-input组件去开发用户注册。这种form封装的组件v-model是不可能写到组件内部去的，因为这样不灵活。
- 如果要灵活性的，可能v-model是写在标签使用上，而不是定义上。





## form元素的v-model组件的实现

```html
<input v-model="message">
```

上面的原理就是通过 @input输入触发而同步，如下：

```html
<input :value="modelValue" @input="$emit('update:modelValue',$event.target.value)"/>
```

完整代码如下：

```html
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>05、Vue的组件和form元素的问题-01.html</title>
</head>
<body>


<div id="app">
    <ksd-input v-model="message"></ksd-input>
    <p>你输入的内容是：{{message}}</p>
</div>

<script src="js/vue.global.js"></script>
<script>
    var app = Vue.createApp({
        data(){
            return {
                message:"你们是一群小可爱!!!"
            }
        }
    });

    app.component("KsdInput",{
        props:["modelValue"],
        template:`<input :value="modelValue" @input="$emit('update:modelValue',$event.target.value)"/>`
    })

    app.mount("#app");
</script>

</body>
</html>
```

- props中的名字必须叫：modelValue
- this.$emit('update:modelValue',$$event.target.value) 这段，update:modelValue也是固定的。



### $event是什么东西

$event是每个触发时间的默认参数，你不传递默认就是它，它代表的是：当前你点击事件类型的对象。这个事件对象，可以拿到你触发的事件类型，事件的触发元素。

```html
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>05、Vue的组件和form元素的问题-01.html</title>
</head>
<body>


<div id="app">
    <button @click="clickme">点我</button>
    <button @click="clickme($event)">点我</button>
    <button @click="clickme2(100,$event)">点我</button>
</div>

<script src="js/vue.global.js"></script>
<script>
    var app = Vue.createApp({
        data(){
            return {
                message:"你们是一群小可爱!!!"
            }
        },
        methods:{
            clickme($event){
                console.log("okokok",$event.type,$event.target.innerText)
            },
            clickme2(value,$event){
                console.log("okokok22222222222222",value,$event.type,$event.target.innerText)
            }
        }
    }).mount("#app");
</script>

</body>
</html>
```







##  v-model的修饰符的自定义

```html
<input  v-model.trim.lazy="message">
```

修饰符在开发中，有时候非常有价值和赋值表单处理的一种机制。比如上面：.trim .lazy，.num

自定义修饰符：比如首字母大写：

```html
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>07、Vue的组件和form元素v-model修饰符-07.html</title>
</head>
<body>


<div id="app">
    <ksd-input v-model.trim2="message"></ksd-input>
    <p>你输入的内容是：{{message.length}}</p>
</div>

<script src="js/vue.global.js"></script>
<script>
    var app = Vue.createApp({
        data() {
            return {
                message: "azzzzz"
            }
        }
    });

    app.component("KsdInput", {
        props: {
            modelValue:String,
            // 自定义修饰符的接受对象
            modelModifiers: {
                default: () => ({})
            }
        },

        created(){
            var value  = this.modelValue.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
            this.$emit('update:modelValue', value)
        },

        methods: {
            emitValue(e) {
                // 1: 获取你输入的值
                var value = e.target.value;
                // 2: 获取你的修饰符是否存在，存在就进行对应的处理
                if (this.modelModifiers.trim2) {
                    // 3: 把你的值进行一个处理
                    value = value.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
                }
                // 4: 同步到父作用域中去，然后改变视图
                e.target.value = value;
                this.$emit('update:modelValue', value)
            }
        },
        template: `<input :value="modelValue" @input="emitValue"/>`
    })

    app.mount("#app");
</script>

</body>
</html>
```



































