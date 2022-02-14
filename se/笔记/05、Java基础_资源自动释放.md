# Java7新特性 - AutoCloseable

授课老师：学相伴飞哥



## 01、课程大纲

01、概述

02、实战应用



## 02、概述

JDK在1.7之后出现了自动关闭类的功能，该功能的出现为各种关闭资源提供了相当大的帮助，这里我们谈一谈自动关闭类。
JDK1.7之后出现了一个重要的接口，以及改造了一个重要的方法结构：
1、AutoCloseable自动关闭接口
2、try(){}--catch{}--finally{}
相应的 一些资源也实现了该接口，如preparedStatement、Connection、InputStream、outputStream等等资源接口。

接口的实现类要重写close（）方法，将要关闭的资源定义在try()中，这样当程序执行完毕之后，资源将会自动关闭。自定义类如果要进行自动关闭，只需要实现AutoCloseable接口重写close（）方法即可，

同时也只有实现了AutoCloseable接口才能将，自定义类放入到try()块中，否则编译不能通过，举例说明

```java
public class ReadTxt extends AutoClassable {
	@Override
	public void close() throws Exception {
		System.out.println("ReadTxt close");
	}
 
	public String readTextValue(String path){
		StringBuffer sb = new StringBuffer();
		try(BufferedReader br = new BufferedReader(new FileReader(path))){
			int line;
			while((line = br.read())!=-1){
				sb.append(br.readLine()+"\n")
			}
		}
		return sb.toString();
	}
}
 
class MainTest {
	public static void main(String[] args) {
		try (ReadTxt rt = new ReadTxt()) {
			String line = rt.readTextValue("G:\\学习文档\\test.txt");
			System.out.println(line);
		}
	}
}
```





## 03、案例分析

比如：未来的Redis的分布式锁等

分布式锁类：

```java
package com.kuangstudy.lock;

/*
 * @Author yykk
 * @Description 分布式锁案例
 * @Date 19:44 2021/10/12
 * @Param
 * @return
 **/
public class RedisLock implements AutoCloseable {

    // 加锁
    public void lock(){
        System.out.println("加锁了....");
    }

    // 释放锁
    public void unlock(){
        System.out.println("释放锁了....");
    }

    // 自动释放资源
    @Override
    public void close() throws Exception {
        this.unlock();
    }
}

```

测试类:

```java
package com.kuangstudy.lock;

/*
 * @Author yykk
 * @Description 分布式锁案例
 * @Date 19:44 2021/10/12
 * @Param
 * @return
 **/
public class RedisLockTest {

    public static void main(String[] args) {
        try (RedisLock redisLock = new RedisLock()) {
            redisLock.lock();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        finally {
//            // 不需要定义了.因为会自动去释放资源
//            redisLock.unlock();
//        }
    }

}

```

执行结果如下：

```java
加锁了....
释放锁了...
```















