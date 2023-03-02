package com.mzaxd.noodles.domain.message;

import com.mzaxd.noodles.util.Arith;
import lombok.Data;

/**
 * @author Mzaxd
 * @since 2023-02-05 10:49
 */
@Data
public class Cpu {

    /**
     * CPU型号
     */
    private String cpuName;

    /**
     * CPU路数
     */
    private int physicalPackageCount;

    /**
     * 核心数
     */
    private int physicalProcessorCount;

    /**
     * 线程数
     */
    private int logicalProcessorCount;

    /**
     * CPU主频
     */
    private long maxFreq;

    /**
     * CPU Load
     */
    private double cpuLoad;

    /**
     * CPU总的使用率
     */
    private double total;

    /**
     * CPU系统使用率
     */
    private double sys;

    /**
     * CPU用户使用率
     */
    private double used;

    /**
     * CPU当前等待率
     */
    private double wait;

    /**
     * CPU当前空闲率
     */
    private double free;

    public double getCpuLoad()
    {
        return Double.parseDouble(String.format("%.1f", cpuLoad));
    }

    public double getTotal()
    {
        return Arith.round(Arith.mul(total, 100), 2);
    }

    public double getSys()
    {
        return Arith.round(Arith.mul(sys / total, 100), 2);
    }

    public double getUsed()
    {
        return Arith.round(Arith.mul(used / total, 100), 2);
    }

    public double getWait()
    {
        return Arith.round(Arith.mul(wait / total, 100), 2);
    }

    public double getFree()
    {
        return Arith.round(Arith.mul(free / total, 100), 2);
    }

}
