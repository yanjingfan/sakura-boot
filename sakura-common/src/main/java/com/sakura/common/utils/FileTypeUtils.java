package com.sakura.common.utils;

import cn.hutool.core.io.FileTypeUtil;
import com.sakura.common.exception.YWarmingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @auther yangfan
 * @date 2022/8/24
 * @describle 文件类型工具类
 */
public class FileTypeUtils {
    private final static String IMAGE_TYPE = "image/";
    private final static String AUDIO_TYPE = "audio/";
    private final static String VIDEO_TYPE = "video/";
    private final static String FILE_TYPE = "file/";
    private final static String TXT_TYPE = "text/";

    public static String getFileType(MultipartFile multipartFile) {
        InputStream inputStream = null;
        String type = null;
        try {
            inputStream = multipartFile.getInputStream();
            type = FileTypeUtil.getType(inputStream);
            if (null == type) {
                // 未成功识别类型，扩展名辅助识别
                type = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1);
            }
            System.out.println(type);
            if (type.equalsIgnoreCase("JPG") || type.equalsIgnoreCase("JPEG")
                    || type.equalsIgnoreCase("GIF") || type.equalsIgnoreCase("PNG")
                    || type.equalsIgnoreCase("BMP") || type.equalsIgnoreCase("PCX")
                    || type.equalsIgnoreCase("TGA") || type.equalsIgnoreCase("PSD")
                    || type.equalsIgnoreCase("TIFF")) {
                return IMAGE_TYPE;
            }
            if (type.equalsIgnoreCase("mp3") || type.equalsIgnoreCase("OGG")
                    || type.equalsIgnoreCase("WAV") || type.equalsIgnoreCase("REAL")
                    || type.equalsIgnoreCase("APE") || type.equalsIgnoreCase("MODULE")
                    || type.equalsIgnoreCase("MIDI") || type.equalsIgnoreCase("VQF")
                    || type.equalsIgnoreCase("CD")) {
                return AUDIO_TYPE;
            }
            if (type.equalsIgnoreCase("mp4") || type.equalsIgnoreCase("avi")
                    || type.equalsIgnoreCase("MPEG-1") || type.equalsIgnoreCase("RM")
                    || type.equalsIgnoreCase("ASF") || type.equalsIgnoreCase("WMV")
                    || type.equalsIgnoreCase("qlv") || type.equalsIgnoreCase("MPEG-2")
                    || type.equalsIgnoreCase("MPEG4") || type.equalsIgnoreCase("mov")
                    || type.equalsIgnoreCase("3gp")) {
                return VIDEO_TYPE;
            }
            if (type.equalsIgnoreCase("doc") || type.equalsIgnoreCase("docx")
                    || type.equalsIgnoreCase("pdf")
                    || type.equalsIgnoreCase("ppt") || type.equalsIgnoreCase("pptx")
                    || type.equalsIgnoreCase("xls") || type.equalsIgnoreCase("xlsx")
                    || type.equalsIgnoreCase("zip")||type.equalsIgnoreCase("jar")) {
                return FILE_TYPE;
            }
            if (type.equalsIgnoreCase("txt")) {
                return TXT_TYPE;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new YWarmingException("存在危险文件，不允许上传！");
    }
}
