package com.mzaxd.noodles.runner;

import com.mzaxd.noodles.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author Mzaxd
 * @since 2023-02-06 17:14
 */
@Slf4j
@Component
public class GenerateUUID implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("探测器Id：{}", IdUtil.getDetectorId());
    }
}
