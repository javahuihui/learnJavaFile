package com.streamdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author 飞哥
 * @Title: 学相伴出品
 * @Description: 飞哥B站地址：https://space.bilibili.com/490711252
 * 记得关注和三连哦！
 * @Description: 我们有一个学习网站：https://www.kuangstudy.com
 * @date 2021/10/12 14:41
 */
public class StreamDemo {

    public static void main(String[] args) {
        List<User> userList = new ArrayList<>();
        userList.add(new User(1, "yykk", "111111", 34, 1, 34600d));
        userList.add(new User(2, "祈福", "2222222", 24, 0, 883600d));
        userList.add(new User(3, "小王", "3333333", 24, 1, 734090d));
        userList.add(new User(4, "小楠", "4444444", 14, 0, 33400d));
        userList.add(new User(5, "小张", "55555", 29, 1, 140000d));

        // 1： 使用map获取集合中username列
        List<String> userNameList = userList.stream().map(res -> res.getUsername()).collect(Collectors.toList());
        userNameList.forEach(System.out::println);

        // 2: 使用map获取集合中的id,username,age,sex,shenjia，排除password
        List<Map<String, Object>> collect = userList.stream().map(user -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id",user.getId());
            map.put("age",user.getAge());
            map.put("sex",user.getSex());
            map.put("shenjia",user.getShenjia());
            return map;
        }).collect(Collectors.toList());
        collect.forEach(System.out::println);

        // 3: 快速清空password
        List<User> userList1 = userList.stream().map(user -> {
            user.setPassword("");
            return user;
        }).collect(Collectors.toList());
        userList1.forEach(System.out::println);


        Integer maxage = userList.stream().map(user -> user.getAge()).max((a,b)->a-b).get();
        Integer minage = userList.stream().map(user -> user.getAge()).min((a,b)->a-b).get();
        long count = userList.stream().map(user -> user.getAge()).count();
        long sumcount1 = userList.stream().map(user -> user.getAge()).reduce((a,b)->a+b).get();
        long sumcount2 = userList.stream().mapToInt(user -> user.getAge()).sum();
        System.out.println(maxage);
        System.out.println(minage);
        System.out.println(count);
        System.out.println(sumcount1);
        System.out.println(sumcount2);
        System.out.println(sumcount1/count);
    }
}
