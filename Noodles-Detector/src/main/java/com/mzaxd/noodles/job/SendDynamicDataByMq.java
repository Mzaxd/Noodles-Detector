package com.mzaxd.noodles.job;

import com.mzaxd.noodles.constant.SystemVariable;
import com.mzaxd.noodles.service.RabbitMqService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Mzaxd
 * @since 2023-02-07 17:00
 */
@Component
public class SendDynamicDataByMq {

    @Resource
    private RabbitMqService rabbitMqService;

    @Scheduled(cron = "0/3 * * * * ?")
    public void sendDynamicDataByMQ() {
        if (SystemVariable.SEND_DYNAMIC_DATA_SWITCH) {
            rabbitMqService.sendDynamicData();
        }
    }
}
