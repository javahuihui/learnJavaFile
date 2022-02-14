package com.kuangstudy;

import java.util.Date;

/**
 * @author 飞哥
 * @Title: 学相伴出品
 * @Description: 飞哥B站地址：https://space.bilibili.com/490711252
 * 记得关注和三连哦！
 * @Description: 我们有一个学习网站：https://www.kuangstudy.com
 * @date 2021/10/11 14:41
 */
public class Item {
    // 视频标题
    private String title;
    // 视频描述
    private String description;
    // 视频大小
    private String coursesize;
    // 视频时长
    private String coursetime;
    // 视频价格
    private Double price;
    // 视频上传时间
    private Date createTime;

    public Item(){

    }

    public Item(String title, String description, String coursesize, String coursetime, Double price, Date createTime) {
        this.title = title;
        this.description = description;
        this.coursesize = coursesize;
        this.coursetime = coursetime;
        this.price = price;
        this.createTime = createTime;
    }

    public void print(){

    }

    public static void main(String[] args) {
        Item item = new Item();
        item.print();
    }
}
