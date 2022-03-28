# 关于Vue中的数组常见API的操作



##  分析

在未来程序开发中，只要是查询返回多条，在前端vue的data绝对是数组，所以数组的操作变得尤为的重要，你必须要掌握。

## js/vue如何操作数组呢？



火狐官方js标准API网址：https://developer.mozilla.org/zh-CN/docs/Web/JavaScript



vue的核心是数据和视图双向数据绑定，为了检测数组中元素的变化，以便及时的将数组元素的变化反映到视图中，Vue对数组的一些方法做了一些处理和包裹，包裹以后逻辑你只要去调用这些数组的方法，就会渲染和同步视图。比如：



## 前期准备工作

```html
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>19、Vue对数组的操作大全.html</title>
    <style>
        table {width: 100%;border-collapse: collapse}
        table tr td{border:1px solid #ccc;padding:10px;}
        .container{background: #eee;padding:20px;margin-bottom: 10px;}
    </style>
</head>
<body>


<div id="app">
    <div class="container">
        <p>商品注解: <input type="text" v-model="product.id"></p>
        <p>商品标题: <input type="text" v-model="product.title"></p>
        <p>商品价格: <input type="text" v-model="product.price"></p>
        <p>商品数量: <input type="text" v-model="product.num"></p>
        <p>
            <button>push添加商品</button> &nbsp;&nbsp;&nbsp;
            <button>unshift添加商品</button>
        </p>
    </div>
    <table>
        <thead>
        <tr>
            <td><input type="checkbox"></td>
            <td>ID</td>
            <td>产品名称</td>
            <td>产品价格</td>
            <td>产品的数量</td>
            <td>操作</td>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(product,index) in products" :key="product.id">
            <td><input type="checkbox"></td>
            <td>{{product.id}}</td>
            <td>{{product.title}}</td>
            <td>{{product.price}}</td>
            <td>{{product.num}}</td>
            <td><a href="javascript:void(0);">编辑</a> <a href="javascript:void(0);">删除</a></td>
        </tr>
        </tbody>
    </table>
</div>

<script src="js/vue.global.js"></script>
<script>
    var vm = Vue.createApp({
        data() {
            return {
                product:{id:0,title:"",price:0,num:1},
                products: [
                    {id: 1, title: "小米11", price: 125.8, num: 12},
                    {id: 2, title: "小米12", price: 25.8, num: 2},
                    {id: 3, title: "小米13", price: 15.8, num: 1},
                    {id: 4, title: "小米14", price: 5.8, num: 11},
                    {id: 5, title: "小米15", price: 15.8, num: 58}
                ]
            }
        },
        methods: {}
    }).mount("#app");
</script>
</body>
</html>
```



## 分析和了解数组常见操作

### 添加 push(); 

`**push()**` 方法将一个或多个元素添加到数组的末尾，并返回该数组的新长度。 改变数组长度

```js
// push添加商品
pushProduct(){
	this.products.push(this.product);
},

```

### 添加 unshift

**`unshift()`** 方法将一个或多个元素添加到数组的**开头**，并返回该数组的**新长度(该**方法修改原有数组**)**。

```js
// unshift添加商品
unshiftProduct(){
	this.products.unshift(this.product);
}
```

### 拼接 concat();

```js
// 拼接数组
concatProduct(){
    // 这里是从数据库中查询出来的商品的第二页的数组
    var secondProducts = [
    {id: 6, title: "小米16", price: 125.8, num: 12},
    {id: 7, title: "小米17", price: 25.8, num: 2},
    {id: 8, title: "小米18", price: 15.8, num: 1},
    {id: 9, title: "小米20", price: 5.8, num: 11},
    {id: 10, title: "小米21", price: 15.8, num: 58}
    ];

    // 这里是追加
    this.products = this.products.concat(secondProducts);
},
```

如果你想覆盖，如下

```js

loadMore(){
    // 这里是从数据库中查询出来的商品的第二页的数组
    var secondProducts = [
    {id: 6, title: "小米16", price: 125.8, num: 12},
    {id: 7, title: "小米17", price: 25.8, num: 2},
    {id: 8, title: "小米18", price: 15.8, num: 1},
    {id: 9, title: "小米20", price: 5.8, num: 11},
    {id: 10, title: "小米21", price: 15.8, num: 58}
    ];

    // 这里是追加
    this.products = secondProducts;
}
```



### 删除&修改 splice();

假设你的数组是：[1,2,3,4,5,6,7,9,10]

- 删除功能： splice(数组索引,个数) ---比如splice(1,2) 从数组的索引是1的位置上，删除2个元素 ，就把数组的：2,3

```js
 // 删除元素
 delProductByIndex(index){
 	this.products.splice(index,1);
 }
```

- 替换功能：splice(数组索引，个数，替换的元素); ---比如：splice(1,2,22,33,44) 从索引1的位置上，删除两个元素，把删除位置上替换成：22,33 ,4

```js
#  删除一个补一个
this.products.splice(index,1,{id: 6, title: "小米16", price: 125.8, num: 12});
# 删除一个补多个
this.products.splice(index,1,{id: 6, title: "小米16", price: 125.8, num: 12},{id: 6, title: "小米16", price: 125.8, num: 12});
```



### sort();

```js
// 排序
sortProductPrice() {
    this.sortPrice = !this.sortPrice;
        this.products = this.products.sort( (a, b) => {
        return this.sortPrice ? a.price - b.price : b.price - a.price;
    })
},

sortMain(flag,filed) {
    this[flag] = !this[flag];
    this.products = this.products.sort( (a, b) => {
        return this[flag] ? a[filed] - b[filed] : b[filed] - a[filed];
    })
},

```



### 弹出 pop();

`**pop()**`方法从数组中删除最后一个元素，==并返回该元素的值==。此方法更改数组的长度。

```js
popProduct() {
	this.product = this.products.pop();
},
```



### 弹出 shift();

`**shift()**` 方法从数组中删除**第一个**元素，==并返回该元素的值==。此方法更改数组的长度。

```js
shiftProduct() {
	this.product = this.products.shift();
},
```

### reverse();

`**reverse()**` 方法将数组中元素的位置颠倒，并返回该数组。数组的第一个元素会变成最后一个，数组的最后一个元素变成第一个。该方法会改变原数组。

```js	
reverseProduct() {
	this.products.reverse();
},
```

```js
var str = "hello";// olleh
var strs = str.split("").reverse().join("");
console.log(strs)//olleh
```



### join()

`**join()**` 方法将一个数组（或一个[类数组对象](https://developer.mozilla.org/zh-CN_docs/Web/JavaScript/Guide/Indexed_collections#working_with_array-like_objects)）的所有元素连接成一个字符串并返回这个字符串。如果数组只有一个项目，那么将返回该项目而不使用分隔符。

```js
var arr = [1,2,3,4,5,6];
## 默认是逗号
var str =arr.join();
console.log(str);//1,2,3,4,5,6

## 写一个空字符，单引号或者双引号都可以如下
var arr = [1,2,3,4,5,6];
var str =arr.join('');
console.log(str);//123456

## 使用分隔符
var arr = [1,2,3,4,5,6];
var str =arr.join('#');
console.log(str);//1#2#3#4#5#6
```





## es6中对数组扩展的新的API---computed

- reduce
- map
- filter
- indexOf
- lastIndexOf
- forEach



