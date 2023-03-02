package com.mzaxd.noodles.util;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Mzaxd
 * @since 2023-02-06 17:43
 */
@Slf4j
@Component
public class IdUtil {

    @Autowired
    private ResourceLoader resourceLoader;

    private static ResourceLoader loader;

    @PostConstruct
    public void init() {
        loader = resourceLoader;
    }

    public static String getDetectorId() throws IOException {
        return FileUtil.readUtf8String(getIdFile());
    }

    public static boolean isFirstInit() throws IOException {
        if (FileUtil.isEmpty(getIdFile())) {
            return true;
        } else {
            getDetectorId();
            return false;
        }
    }

    public static boolean initDetectorId() throws IOException {
        File file = getIdFile();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        FileUtil.writeUtf8String(uuid, file);
        log.info("首次使用此探测器，自动生成识别码为：{}", uuid);
        if (FileUtil.isEmpty(file)) {
            log.info("探测器UUID初始化失败");
        }
        log.info("探测器UUID初始化成功");
        return true;
    }

    public static File getIdFile() throws IOException {
        Resource resource = loader.getResource("classpath:id.txt");
        String path = resource.getFile().getPath();
        return (FileUtil.file(path));
    }
}
