package com.sakura.common.domain;

import lombok.Data;

/**
 * 滑动验证码
 * @auther YangFan
 * @Date 2023/6/11 0:09
 */
@Data
public class SlideImage {

    /**
     * x轴宽度
     */
    private Integer xWidth;

    /**
     * y轴高度
     */
    private Integer yHeight;

    /**
     * 大图的BASE64字符串
     */
    private String bigImage;

    /**
     * 小图的BASE64字符串
     */
    private String smallImage;

    /**
     * uuid用来获取存入redis，校验验证码时需要
     */
    private String uuid;
}
