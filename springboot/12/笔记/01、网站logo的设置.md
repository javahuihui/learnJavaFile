# 01、网站logo的设置

##  logo设置(默认方式)

一个网站肯定会有logo，在springboot，默认会从静态资源存放的路径或者定义静态资源的路径去查找logo图标文件。默认是：favicon.ico。如果这个文件存在就自定进行渲染。

![image-20211227202239826](01%E3%80%81%E7%BD%91%E7%AB%99logo%E7%9A%84%E8%AE%BE%E7%BD%AE.assets/image-20211227202239826.png)

如何制作ico的图标呢？

- 找到你自己网站的logo图片，格式可以是：png、jpg

- 打开网址：https://www.bitbug.net/ 在线制作：ico图标
- 然后把制作好的图标放入到 static目录下即可。



## logo设置的手动配置

```xml
<link rel="shortcut icon" href="/img/favicon.ico">
```

可以随便取任何名字，格式不限定ico你可以是png也可以是jpg。





