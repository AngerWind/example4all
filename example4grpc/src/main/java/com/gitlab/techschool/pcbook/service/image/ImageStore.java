package com.gitlab.techschool.pcbook.service.image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public interface ImageStore {

    // 保存一张照片
    String save(String laptopID, String imageType, ByteArrayOutputStream imageData) throws IOException;
}