package com.wql.cloud.basic.kaptcha.util;

import java.util.Base64;

/**
 * 验证码对象
 * @author wangqiulin
 *
 */
public class VcodeInfo {

    public static final String BASE64_IMG_PREFIX = "data:image/jpeg;base64,";

    /**
     * 验证码
     */
    private String text;

    private byte[] imgContent;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public byte[] getImgContent() {
        return imgContent;
    }

    public void setImgContent(byte[] imgContent) {
        this.imgContent = imgContent;
    }

    /**
     * 获取加工后图片二进制编码base64内容
     * @return
     */
    public String getBase46ImgContent() {
        return BASE64_IMG_PREFIX.concat(Base64.getEncoder().encodeToString(this.imgContent));
    }

    /**
     * 获取原生图片二进制编码base64内容
     * @return
     */
    public String getNativeBase46ImgContent() {
        return Base64.getEncoder().encodeToString(this.imgContent);
    }
}
