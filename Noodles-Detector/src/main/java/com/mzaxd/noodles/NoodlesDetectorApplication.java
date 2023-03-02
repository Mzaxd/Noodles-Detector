package com.mzaxd.noodles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Mzaxd
 */
@SpringBootApplication
@EnableScheduling
public class NoodlesDetectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoodlesDetectorApplication.class, args);
    }

}
