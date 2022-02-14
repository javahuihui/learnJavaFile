package com.kuangstudy;

import java.util.Date;

/**
 * 课程信息
 *
 * @author 飞哥
 * @Title: 学相伴出品
 * @Description: 我们有一个学习网站：https://www.kuangstudy.com
 * @Description: 飞哥B站地址：https://space.bilibili.com/490711252
 * @date 2021/10/11 13:47
 */
public class Course extends Item{

    //  课程目录
    private Integer folderId;


    @Override
    public void print(){
        System.out.println(this);
    }

    public Course(String title, String description, String coursesize, String coursetime, Double price, Date createTime, Integer folderId) {
        super(title,description,coursesize,coursetime,price,createTime);
        this.folderId = folderId;
    }

    public Integer getFolderId() {
        return folderId;
    }

    public void setFolderId(Integer folderId) {
        this.folderId = folderId;
    }
}
