package com.mzaxd.noodles.domain.message;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Mzaxd
 * @since 2023-02-05 18:49
 */
@Data
@Accessors(chain = true)
public class DynamicData {

    /**
     * 探测器id
     */
    private String detectorId;

    /**
     * CPU Load
     */
    private double cpuLoad;

    /**
     * CPU总的使用率
     */
    private double cpuTotal;

    /**
     * CPU系统使用率
     */
    private double cpuSys;

    /**
     * CPU用户使用率
     */
    private double cpuUser;

    /**
     * CPU当前等待率
     */
    private double cpuWait;

    /**
     * CPU当前空闲率
     */
    private double cpuFree;

    /**
     * 已用内存
     */
    private double memUsed;

    /**
     * 剩余内存
     */
    private double memFree;

    /**
     * 上行速度
     */
    private String txPercent;

    /**
     * 下行速度
     */
    private String rxPercent;

}
