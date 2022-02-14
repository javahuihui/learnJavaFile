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

        // 两个集合或者多个集合进行累加年龄
        int sum = Stream.of(userList,userList2).flatMapToInt(user -> user.stream().mapToInt(u->u.getAge())).sum();
        System.out.println(sum);

    }
}
