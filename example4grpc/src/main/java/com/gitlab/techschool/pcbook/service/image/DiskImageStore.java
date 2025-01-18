package com.gitlab.techschool.pcbook.service.image;

import com.gitlab.techschool.pcbook.service.image.ImageMetadata;
import com.gitlab.techschool.pcbook.service.image.ImageStore;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DiskImageStore implements ImageStore {
    // 保存照片的文件夹
    private String imageFolder;

    // key是laptop的id, value是image的元数据信息
    private ConcurrentMap<String, ImageMetadata> data;

    public DiskImageStore(String imageFolder) {
        this.imageFolder = imageFolder;
        this.data = new ConcurrentHashMap<>(0);
    }

    /**
     * 保存照片到disk
     */
    @Override
    public String save(String laptopID, String imageType, ByteArrayOutputStream imageData) throws IOException {
        // 随机一个image id
        String imageID = UUID.randomUUID().toString();
        // 创建image保存的path
        String imagePath = String.format("%s/%s%s", imageFolder, imageID, imageType);

        // 保存到disk
        FileOutputStream fileOutputStream = new FileOutputStream(imagePath);
        imageData.writeTo(fileOutputStream);
        fileOutputStream.close();

        // 保存image的元数据信息
        ImageMetadata metadata = new ImageMetadata(laptopID, imageType, imagePath);
        data.put(imageID, metadata);

        return imageID;
    }
}