package com.mzaxd.noodles.util;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.fasterxml.jackson.core.io.UTF8Writer;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.Charset;
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
        InputStream inputStream = loader.getResource("classpath:id.txt").getInputStream();
        return IoUtil.read(inputStream, "utf8");
    }
}
