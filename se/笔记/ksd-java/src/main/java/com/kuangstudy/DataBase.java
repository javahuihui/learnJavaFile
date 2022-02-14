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
public class DataBase {

    // 1：课程资料容器
    private List<Course> courseTable = new ArrayList<Course>();
    // 2：视频资源容器
    private List<Video> videoTable = new ArrayList<Video>();


    // 2: 添加课程到课程资料容器
    public void add(Course course) {
        courseTable.add(course);
    }

    // 3: 添加视频资源
    public void add(Video video) {
        videoTable.add(video);
    }

    // 4: 罗列出所有的资源库的课程信息
    public void listcourse() {
        courseTable.forEach(res->res.print());
    }

    // 5: 罗列出所有的资源库的视频信息
    public void listvideo() {
        videoTable.forEach(res->res.print());
    }

    // 5: 找个应用程序运行程序
    public static void main(String[] args) {

        // 1: 创建资料库
        DataBase dataBase = new DataBase();

        // 2: 创建课程
        Course course1 = new Course("学相伴旅游项目实战课程","学相伴旅游项目实战课程java基础","12M","300小时",1499d,new Date(),1);
        Course course2 = new Course("学相伴秋招课程","学相伴秋招课程","12M","300小时",1499d,new Date(),1);

        //创建视频资源
        Video video1 = new Video("学相伴秋招课程","学相伴秋招课程","12M","300小时",1499d,"MP4",new Date());

        // 3: 添加课程到资料库
        dataBase.add(course1);
        dataBase.add(course2);
        // 4：添加视频资源到资料库
        dataBase.add(video1);

        // 4:打印罗列课程
        dataBase.listcourse();
        // 4:打印罗列视频
        dataBase.listvideo();
    }

}
