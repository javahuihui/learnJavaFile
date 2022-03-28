# v-model的问题 



## v-model和value的问题

```html
 <input type="hidden" value="${(blogId)!}" v-model="blog.id">
```

**v-model 会忽略所有表单元素的 value、checked、selected attribute 的初始值。它将始终将当前活动实例的数据作为数据来源。你应该通过 JavaScript 在组件的 data 选项中声明初始值。**

固定的想法，value明明有值，为什么v-model拿不到？飞哥你为什么还要在生命周期的created通过js获取在赋予data中。



页面加载js初始化完整的过程如下：

- 1、http请求路径进入服务器端。

- 2、服务器接收请求，通过freemarker获取模型中数据blogId，把页面${}或者freemarker的进行替换渲染。

- 3、然后把渲染的内容，通过response流进行输出。这个blogId在页面上其实就已经替换掉了.

  ```html
   <input type="hidden" ref="blogid" value="1" v-model="blog.id">
  ```

- 4、浏览器接收页面数据，开始呈现给用户,dom节点开始初始化。初始化完毕之后，浏览呈现出页面给用户

- 5、但是浏览器同时在dom初始化之后，会解析css，同时也加载js（vuejs/jquery）等，

- 6、当一旦加载到vue的js的时候，==发现页面上有指令v-model 它会忽略所有表单元素的 value、checked、selected attribute 的初始值。==

## 解决方案

1：在vue生命周期created或者mounted中，通过ref或者js的dom获取dom元素值value, 在重新赋值给data的blog.id。这个blog.id有值了，就直接同步给视图。==你应该通过 JavaScript 在组件的 data 选项中声明初始值。==

2：代码如下：

```html
<input type="hidden" ref="blogid" value="${blogId}">
<input type="hidden" v-model="blog.id">
<input type="text"  v-model="blog.title">
<input type="text"  v-model="blog.img">
<input type="text"  v-model="blog.categoryId">
```

```js
data(){
	return {
		blog:{id:,title:"",img:"",categoryId:""}
	}
}, 
created(){
   this.blog.id = this.$refs.blogid.value;
   //this.blog.id = this.$route.params.id;
   this.getBlog();
},
methods :{
   async getBlog(){
     var blog =  await axios.get("/api/blog/get/"+this.blog.id) ;
     this.blog = blog;  
   }
}
```

3：未来在前后端分离的项目中，压根就不存在这种开发混合的开发模式。freemarker + jquery