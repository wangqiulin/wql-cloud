package com.wql.cloud.basic.kaptcha.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;

/**
 * 验证码生成工具类
 * @author wangqiulin
 *
 */
public class VcodeUtil {

    private final String JPG = "jpg";

    /**
     * 验证码实现
     */
    private Producer producer;

    public VcodeUtil() {
        producer = getDefaultKaptcha();
    }

    public VcodeUtil(Producer producer) {
        this.producer = producer;
    }

    /**
     * 生成验证码方法
     *
     * @return
     */
    public VcodeInfo generatorVCode(String text) {
        try {
            VcodeInfo info = new VcodeInfo();
            info.setText(text);
            info.setImgContent(generatorImgContent(info.getText()));
            return info;
        } catch (Exception e) {
            throw new RuntimeException("生成验证码图片失败2", e);
        }
    }


    /**
     * 生成验证码方法
     *
     * @return
     */
    public VcodeInfo generatorVCode() {
        try {
            VcodeInfo info = new VcodeInfo();
            info.setText(producer.createText());
            info.setImgContent(generatorImgContent(info.getText()));
            return info;
        } catch (Exception e) {
            throw new RuntimeException("生成验证码图片失败2", e);
        }
    }

    /**
     * 由输入内容生成验证码
     *
     * @param text
     * @return
     */
    public byte[] generatorImgContent(String text) {
        BufferedImage image = producer.createImage(text);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(image, JPG, out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("生成验证码图片失败1", e);
        }
    }

    /**
     * 默认验证码配置
     * 数字验证码
     *
     * @return
     */
    private Producer getDefaultKaptcha() {
        Properties properties = new Properties();
        // <!-- 是否有边框 可选yes 或者 no -->
        properties.setProperty(Constants.KAPTCHA_BORDER, "no");
        // <!-- 边框颜色 -->
        properties.setProperty(Constants.KAPTCHA_BORDER_COLOR, "105,179,90");
        // <!-- 验证码文本字符颜色 -->
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, "blue");
        // <!-- 验证码文本字符大小 -->
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, "30");
        // <!-- 验证码图片的宽度 默认200 -->
        properties.setProperty(Constants.KAPTCHA_IMAGE_WIDTH, "110");
        // <!-- 验证码图片的高度 默认50 -->
        properties.setProperty(Constants.KAPTCHA_IMAGE_HEIGHT, "40");
        // <!-- 验证码文本字符长度  默认为5 -->
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
        // <!-- 验证码文本字体样式  默认为new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize)  -->
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial, Courier");
        // 文字间隔
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "5");
        // 文字实现类
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_IMPL, "com.google.code.kaptcha.text.impl.DefaultTextCreator");
        // 背景渐变
        properties.setProperty(Constants.KAPTCHA_BACKGROUND_CLR_FROM, "white");
        properties.setProperty(Constants.KAPTCHA_BACKGROUND_CLR_TO, "white");
        // 干扰实现类
        properties.setProperty(Constants.KAPTCHA_NOISE_IMPL, "com.google.code.kaptcha.impl.DefaultNoise");
        // 文字渲染器
        properties.setProperty(Constants.KAPTCHA_WORDRENDERER_IMPL, "com.google.code.kaptcha.text.impl.DefaultWordRenderer");
        // 干扰样式
        // 图片样式：
        // 水纹com.google.code.kaptcha.impl.WaterRipple 默认
        // 鱼眼com.google.code.kaptcha.impl.FishEyeGimpy
        // 阴影com.google.code.kaptcha.impl.ShadowGimpy
        properties.setProperty(Constants.KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.WaterRipple");
        Config config = new Config(properties);
        return config.getProducerImpl();
    }


}
