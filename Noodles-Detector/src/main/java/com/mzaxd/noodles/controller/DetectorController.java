package com.mzaxd.noodles.controller;

import com.mzaxd.noodles.constant.SystemVariable;
import com.mzaxd.noodles.domain.ResponseResult;
import com.mzaxd.noodles.enums.AppHttpCodeEnum;
import com.mzaxd.noodles.domain.message.Server;
import com.mzaxd.noodles.service.RabbitMqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author Mzaxd
 * @since 2023-02-05 10:58
 */
@Slf4j
@RestController
public class DetectorController {

    @Value("${detector.id}")
    private String detectorId;

    @Resource
    private RabbitMqService rabbitMqService;

    @GetMapping("/getInfo")
    public ResponseResult getInfo() {
        Server server = new Server();
        try {
            server.copyTo();
        } catch (Exception e) {
            ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            throw new RuntimeException(e);
        }
        return ResponseResult.okResult(server);
    }

    @GetMapping("/getDiskInfo")
    public ResponseResult getDiskInfo() {
        Server server = new Server();
        try {
            server.copyToDiskInfo();
        } catch (Exception e) {
            ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            throw new RuntimeException(e);
        }
        return ResponseResult.okResult(server.getSysFiles());
    }

    @GetMapping("/getNetworkIfInfo")
    public ResponseResult getNetworkIfInfo() {
        Server server = new Server();
        try {
            server.copyToNetworkIfInfo();
        } catch (Exception e) {
            ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            throw new RuntimeException(e);
        }
        return ResponseResult.okResult(server.getNetWorkIf());
    }


    @GetMapping("/startSendDynamicDataFromMq")
    public ResponseResult startSendDynamicDataFromMq() {
        SystemVariable.SEND_DYNAMIC_DATA_SWITCH = true;
        log.info("开始发送动态数据");
        return ResponseResult.okResult();
    }

    @GetMapping("/stopSendDynamicDataFromMq")
    public ResponseResult stopSendDynamicDataFromMq() {
        SystemVariable.SEND_DYNAMIC_DATA_SWITCH = false;
        log.info("停止发送动态数据");
        return ResponseResult.okResult();
    }

    @GetMapping("/isTrueUrl")
    public ResponseResult isTrueUrl() {
        return ResponseResult.okResult("连接成功");
    }

    @GetMapping("/getDetectorID")
    public ResponseResult getDetectorID() {
        log.info("探测器Id为：{}", detectorId);
        return ResponseResult.okResult(detectorId);
    }
}
