package com.mzaxd.noodles;

import cn.hutool.core.io.IoUtil;
import com.fasterxml.jackson.core.io.UTF8Writer;
import com.mzaxd.noodles.util.SystemInfoUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import oshi.SystemInfo;
import oshi.hardware.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NoodleDetectorApplicationTests {

    @Test
    public void initDetectorId() throws IOException {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println(uuid);
    }
}
