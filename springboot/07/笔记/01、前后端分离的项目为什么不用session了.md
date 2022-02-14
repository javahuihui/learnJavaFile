# 01、前后端分离的项目为什么不用session了。

1：单体架构，整体的运行是以jar或者war进行部署运行，运行过程中是以进程运行，在程序执行是数据存储的过程都是在这个进程中。比如：你运行一个程序就会你的系统的产生一个java.exe进程。

2：后端部分，还运行java进程陈中。前端部分：vue+nodejs架构进行运行，在部署阶段发布一个静态文件部署nginx进程。

3；JWT





## 02、找工作我该如何准备(实习，工作一年)

- 简历一定准备好

  

- 基础中：list map linkedlist hashmap concorrenthashmap

- spring springmvc mybatis springboot

- 数据库（sql优化能力，设计表要注意那些，有没有优化索引，索引的认识什么？）90分



- 不每天都是围绕：增删改查，数据库 

- redis

- mq

- kafka

- 设计模式

  

  







![image-20211221202434968](C:/Users/86150/AppData/Roaming/Typora/typora-user-images/image-20211221202434968.png)





redis远程连接一定修改几个配置：

1：protected-mode : no

2: bind: 注释掉

3：最好设置一个密码 requiredpass:xxxx

4：一定要把你之前的redis服务器关停掉，

```properties
ps -ef | grep redis
kill redispid
```

5：指定配置文件启动redis (如果你怕修改坏redis.conf文件，在修改之前可以考虑备份一份)

然后在用 src/redis-server   ./redis.conf

6：在阿里云的安全组中，去开放redis的6379端口

![img](file:///C:\Users\86150\Documents\Tencent Files\1707550541\Image\C2C\14NCJQ9TMYB]XSUMYX_LXBK.png)



