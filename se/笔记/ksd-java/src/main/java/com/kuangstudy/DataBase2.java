package com.kuangstudy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 资料库
 *
 * @author 飞哥
 * @Title: 学相伴出品
 * @Description: 我们有一个学习网站：https://www.kuangstudy.com
 * @date 2021/10/11 13:47
 */
public class DataBase2 {

    // 1：课程资料容器
    private List<Item> itemTable = new ArrayList<Item>();


    // 2: 添加课程到课程资料容器
    public void add(Item course) {
        itemTable.add(course);
    }

    // 4: 罗列出所有的资源库的课程信息
    public void list() {
        itemTable.forEach(res -> res.print());
    }

    // 5: 找个应用程序运行程序
    public static void main(String[] args) {

        String str= "jl";
        str = "23";

        // 1: 创建资料库
        DataBase2 dataBase = new DataBase2();

        // 2: 创建课程
        Item course1 = new Course("学相伴旅游项目实战课程","学相伴旅游项目实战课程java基础","12M","300小时",1499d,new Date(),1);
        Item course2 = new Course("学相伴秋招课程","学相伴秋招课程","12M","300小时",1499d,new Date(),1);
        //创建视频资源
        Video video1 = new Video("学相伴秋招课程","学相伴秋招课程","12M","300小时",1499d,"MP4",new Date());
        Video video2 = new Video("学相伴秋招课程","学相伴秋招课程","12M","300小时",1499d,"MP4",new Date());


        // 3: 添加课程到资料库
        dataBase.add(course1);
        dataBase.add(course2);
        // 4：添加视频资源到资料库
        dataBase.add(video1);

        // 4:打印罗列课程
        dataBase.list();
    }

}
