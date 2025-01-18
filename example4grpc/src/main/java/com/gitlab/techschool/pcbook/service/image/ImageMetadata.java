package com.gitlab.techschool.pcbook.service.image;

public class ImageMetadata {

    // 图片所属的笔记本的id
    private String laptopID;

    // 图片的类型
    private String type;

    // 保存图片的路径
    private String path;

    public ImageMetadata(String laptopID, String type, String path) {
        this.laptopID = laptopID;
        this.type = type;
        this.path = path;
    }

    public String getLaptopID() {
        return laptopID;
    }

    public String getType() {
        return type;
    }

    public String getPath() {
        return path;
    }
}