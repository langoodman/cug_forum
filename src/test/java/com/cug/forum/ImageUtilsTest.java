package com.cug.forum;

import com.cug.forum.base.utils.ImageUtils;

public class ImageUtilsTest {
    public static void main(String[] args) throws Exception {
        byte[] bytes = ImageUtils.download("http://image.uc.cn/s/wemedia/s/upload/2020/7001fdb40c003add3b8d04fb2ebf9a45.png");
        System.out.println(bytes);
    }

}
