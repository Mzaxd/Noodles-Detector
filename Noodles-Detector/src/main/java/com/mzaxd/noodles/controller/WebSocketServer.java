package com.mzaxd.noodles.controller;

import com.alibaba.fastjson.JSON;
import com.mzaxd.noodles.domain.message.DynamicData;
import com.mzaxd.noodles.domain.message.Server;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Mzaxd
 * @since 2023-02-05 15:37
 */
@Component
@ServerEndpoint("/ws")
@Slf4j
@Data
public class WebSocketServer {

    /**
     * 实例一个session，这个session是websocket的session
     */
    private Session session;

    /**
     * 存放websocket的集合（本次demo不会用到，聊天室的demo会用到）
     *
     * @author mzaxd
     * @date 2023/2/5 15:39
     * @param null
     */
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();

    /**
     * 前端请求时一个websocket时
     *
     * @param session
     * @author mzaxd
     * @date 2023/2/5 16:11
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        log.info("【websocket消息】有新的连接, 总数:{}", webSocketSet.size());

        // 执行逻辑
        long initialDelay = 0;
        long period = 2L;

        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());
        executorService.scheduleAtFixedRate(() -> {
            Server server = new Server();
            try {
                server.copyTo();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            DynamicData dynamicData = new DynamicData()
                    .setCpuLoad(server.getCpu().getCpuLoad())
                    .setCpuTotal(server.getCpu().getTotal())
                    .setCpuSys(server.getCpu().getSys())
                    .setCpuUser(server.getCpu().getUsed())
                    .setCpuWait(server.getCpu().getFree())
                    .setCpuFree(server.getCpu().getFree())
                    .setMemUsed(server.getMem().getUsed())
                    .setMemFree(server.getMem().getFree())
                    .setTxPercent(server.getNetWork().getTxPercent())
                    .setRxPercent(server.getNetWork().getRxPercent());
            WebSocketServer.sendMessage(JSON.toJSONString(dynamicData));
        }, initialDelay, period, TimeUnit.SECONDS);
    }

    /**
     * 前端关闭时一个websocket时
     *
     * @author mzaxd
     * @date 2023/2/5 16:11
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        log.info("【websocket消息】连接断开, 总数:{}", webSocketSet.size());
    }

    /**
     * 前端向后端发送消息
     *
     * @param message
     * @author mzaxd
     * @date 2023/2/5 16:12
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("【websocket消息】收到客户端发来的消息:{}", message);
    }

    /**
     * 新增一个方法用于主动向客户端发送消息
     *
     * @param message
     * @author mzaxd
     * @date 2023/2/5 16:12
     */
    public static void sendMessage(String message) {
        for (WebSocketServer webSocket : webSocketSet) {
            log.info("【websocket消息】广播消息, message={}", message);
            try {
                webSocket.session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
