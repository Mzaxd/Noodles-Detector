package com.mzaxd.noodles.service.impl;

import com.mzaxd.noodles.constant.RabbitMqConstant;
import com.mzaxd.noodles.domain.message.DynamicData;
import com.mzaxd.noodles.domain.message.Server;
import com.mzaxd.noodles.service.RabbitMqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Mzaxd
 * @since 2023-02-06 13:10
 */
@Slf4j
@Service
public class RabbitMqServiceImpl implements RabbitMqService {

    @Value("${detector.id}")
    private String detectorId;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public void sendDynamicData() {
        Server server = new Server();
        try {
            server.copyToDynamicData();
            DynamicData dynamicData = new DynamicData()
                    .setCpuLoad(server.getCpu().getCpuLoad())
                    .setCpuTotal(server.getCpu().getTotal())
                    .setCpuSys(server.getCpu().getSys())
                    .setCpuUser(server.getCpu().getUsed())
                    .setCpuWait(server.getCpu().getWait())
                    .setCpuFree(server.getCpu().getFree())
                    .setMemUsed(server.getMem().getUsed())
                    .setMemFree(server.getMem().getFree())
                    .setTxPercent(server.getNetWork().getTxPercent())
                    .setRxPercent(server.getNetWork().getRxPercent())
                    .setDetectorId(detectorId);
            rabbitTemplate.convertAndSend(RabbitMqConstant.DYNAMIC_DATA_EXCHANGE, RabbitMqConstant.DYNAMIC_DATA_ROUTING, dynamicData);
            log.info("发送消息DynamicData：{}", dynamicData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
