package com.kuangstudy;

import java.util.Date;

/**
 * 视频信息
 *
 * @author 飞哥
 * @Title: 学相伴出品
 * @Description: 我们有一个学习网站：https://www.kuangstudy.com
 * @Description: 飞哥B站地址：https://space.bilibili.com/490711252
 * @date 2021/10/11 13:47
 */
public class Video extends Item{
    // 视频播放地址
    private String playlink;


    public Video(String title, String description, String coursesize, String coursetime, Double price, String playlink, Date createTime) {
        super(title,description,coursesize,coursetime,price,createTime);
        this.playlink = playlink;
    }

    @Override
    public void print(){
        System.out.println(this);
    }
}
