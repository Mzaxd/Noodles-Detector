package com.mzaxd.noodles.domain.message;

/**
 * @author Mzaxd
 * @since 2023-02-05 10:48
 */

import com.mzaxd.noodles.constant.OsConstant;
import com.mzaxd.noodles.util.Arith;
import com.mzaxd.noodles.util.FormatUtil;
import com.mzaxd.noodles.util.IdUtil;
import lombok.Data;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.FileSystem;
import oshi.software.os.NetworkParams;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.*;

/**
 * 服务器相关信息
 *
 * @author huasheng
 */
@Data
public class Server {

    private static final int OSHI_WAIT_SECOND = 1000;
    /**
     * 网速测速时间2s
     */
    private static final int SLEEP_TIME = 2 * 1000;

    /**
     * 探测器id
     */
    private String detectorId;

    /**
     * Os相关信息
     */
    private Os os = new Os();

    /**
     * CPU相关信息
     */
    private Cpu cpu = new Cpu();

    /**
     * 內存相关信息
     */
    private Mem mem = new Mem();

    /**
     * 服务器相关信息
     */
    private Sys sys = new Sys();

    /**
     * 磁盘相关信息
     */
    private List<SysFile> sysFiles = new LinkedList<SysFile>();

    /**
     * 网络相关信息
     */
    private NetWork netWork = new NetWork();

    /**
     * 网络接口相关信息
     */
    private List<NetworkIF> netWorkIf = new ArrayList<>();

    /**
     * 获取服务器主机相关信息
     *
     * @throws Exception
     */
    public void copyTo() throws Exception {
        // 获取系统信息
        SystemInfo si = new SystemInfo();
        OperatingSystem operatingSystem = si.getOperatingSystem();

        // 根据SystemInfo获取硬件实例
        HardwareAbstractionLayer hal = si.getHardware();

        // 获取Os信息
        setOsInfo(operatingSystem);
        // 获取硬件CPU信息
        setCpuInfo(hal.getProcessor());
        // 获取硬件内存信息
        setMemInfo(hal.getMemory());
        // 设置服务器信息
        setSysInfo(hal.getComputerSystem());
        // 设置磁盘信息
        setSysFiles(si.getOperatingSystem());
        //设置探测器id
        setDetectorId(IdUtil.getDetectorId());

    }

    public void copyToDiskInfo() throws Exception {
        // 获取系统信息
        SystemInfo si = new SystemInfo();
        OperatingSystem operatingSystem = si.getOperatingSystem();

        // 根据SystemInfo获取硬件实例
        HardwareAbstractionLayer hal = si.getHardware();
        // 设置磁盘信息
        setSysFiles(si.getOperatingSystem());
    }

    public void copyToNetworkIfInfo() throws Exception {
        // 获取系统信息
        SystemInfo si = new SystemInfo();
        OperatingSystem operatingSystem = si.getOperatingSystem();

        // 根据SystemInfo获取硬件实例
        HardwareAbstractionLayer hal = si.getHardware();
        // 设置磁盘信息
        setNetWorkIf(si.getHardware().getNetworkIFs());
    }

    public void copyToDynamicData() throws Exception {
        // 获取系统信息
        SystemInfo si = new SystemInfo();
        OperatingSystem operatingSystem = si.getOperatingSystem();

        // 根据SystemInfo获取硬件实例
        HardwareAbstractionLayer hal = si.getHardware();
        // 设置上行下行速度
        setNetWork(operatingSystem.getNetworkParams());
        // 获取硬件CPU信息
        setCpuInfo(hal.getProcessor());
        // 获取硬件内存信息
        setMemInfo(hal.getMemory());
    }


    /**
     * 设置网络上行下行速度
     */
    public void setNetWork(NetworkParams networkParams) {
        Properties props = System.getProperties();
        String os = props.getProperty("os.name").toLowerCase();
        os = os.startsWith("win") ? "windows" : "linux";
        Map<String, String> result = new HashMap<>();
        Process pro = null;
        Runtime r = Runtime.getRuntime();
        BufferedReader input = null;
        try {
            String command = "windows".equals(os) ? "netstat -e" : "ifconfig";
            pro = r.exec(command);
            input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            long[] result1 = readInLine(input, os);
            Thread.sleep(SLEEP_TIME);
            pro.destroy();
            input.close();
            pro = r.exec(command);
            input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            long[] result2 = readInLine(input, os);
            // 下行速率(MB/s)
            netWork.setRxPercent(formatNumber((result2[0] - result1[0]) / (1024.0 * 1024.0 * (SLEEP_TIME / 1000))));
            // 上行速率(MB/s)
            netWork.setTxPercent(formatNumber((result2[1] - result1[1]) / (1024.0 * 1024.0 * (SLEEP_TIME / 1000))));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Optional.ofNullable(pro).ifPresent(Process::destroy);
        }
        InetAddress localHost = null;
        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        netWork.setHostAddress(localHost.getHostAddress());
        netWork.setHostName(networkParams.getHostName());
        netWork.setDomainName(networkParams.getDomainName());
        netWork.setDnsServers(Arrays.asList(networkParams.getDnsServers()));
        netWork.setIpv4DefaultGateway(networkParams.getIpv4DefaultGateway());
        netWork.setIpv6DefaultGateway(networkParams.getIpv6DefaultGateway());
    }

    private static long[] readInLine(BufferedReader input, String osType) {
        long[] arr = new long[2];
        StringTokenizer tokenStat = null;
        try {
            // 获取linux环境下的网口上下行速率
            if (OsConstant.LINUX.equals(osType)) {
                long rx = 0, tx = 0;
                String line = null;
                //RX packets:4171603 errors:0 dropped:0 overruns:0 frame:0
                //TX packets:4171603 errors:0 dropped:0 overruns:0 carrier:0
                while ((line = input.readLine()) != null) {
                    if (line.contains("RX packets")) {
                        rx += Long.parseLong(line.substring(line.indexOf("RX packets") + 11, line.indexOf(" ", line.indexOf("RX packets") + 11)));
                    } else if (line.contains("TX packets")) {
                        tx += Long.parseLong(line.substring(line.indexOf("TX packets") + 11, line.indexOf(" ", line.indexOf("TX packets") + 11)));
                    }
                }
                arr[0] = rx;
                arr[1] = tx;
            } else { // 获取windows环境下的网口上下行速率
                input.readLine();
                input.readLine();
                input.readLine();
                input.readLine();
                tokenStat = new StringTokenizer(input.readLine());
                tokenStat.nextToken();
                arr[0] = Long.parseLong(tokenStat.nextToken());
                arr[1] = Long.parseLong(tokenStat.nextToken());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }

    private static String formatNumber(double f) {
        return new Formatter().format("%.2f", f).toString();
    }


    /**
     * 设置CPU信息
     */
    private void setCpuInfo(CentralProcessor processor) {
        // CPU信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(OSHI_WAIT_SECOND);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;

        String str = processor.toString();
        // CPU型号
        cpu.setCpuName(processor.toString().substring(0, str.indexOf('\n')));
        // Cpu路数
        cpu.setPhysicalPackageCount(processor.getPhysicalPackageCount());
        // Cpu核心数
        cpu.setPhysicalProcessorCount(processor.getPhysicalProcessorCount());
        // Cpu线程数
        cpu.setLogicalProcessorCount(processor.getLogicalProcessorCount());
        // CPU Load
        cpu.setCpuLoad(processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100);
        // CPU 最大频率
        cpu.setMaxFreq(processor.getMaxFreq());
        // CPU总的使用率
        cpu.setTotal(totalCpu);
        // CPU系统使用率
        cpu.setSys(cSys);
        // CPU用户使用率
        cpu.setUsed(user);
        // CPU当前等待率
        cpu.setWait(iowait);
        // CPU当前空闲率
        cpu.setFree(idle);
    }

    /**
     * 设置内存信息
     */
    private void setMemInfo(GlobalMemory memory) {
        // 总内存大小
        mem.setTotal(memory.getTotal());
        // 已使用内存大小
        mem.setUsed(memory.getTotal() - memory.getAvailable());
        // 空闲内存大小
        mem.setFree(memory.getAvailable());
    }

    /**
     * 设置系统信息
     */
    private void setSysInfo(ComputerSystem computerSystem) {
        sys.setManufacturer(computerSystem.getManufacturer());
        sys.setModel(computerSystem.getModel());
        sys.setSerialNumber(computerSystem.getSerialNumber());
        sys.setUuid(computerSystem.getHardwareUUID());
    }

    private void setOsInfo(OperatingSystem operatingSystem) {
        os.setOsName(operatingSystem.toString());
        os.setBooted(String.valueOf(Instant.ofEpochSecond(operatingSystem.getSystemBootTime())));
        os.setUptime(FormatUtil.formatElapsedSecs(operatingSystem.getSystemUptime()));
    }

    /**
     * 设置磁盘信息
     */
    private void setSysFiles(OperatingSystem os) {
        // 根据 操作系统（OS） 获取 FileSystem
        FileSystem fileSystem = os.getFileSystem();
        // 根据 FileSystem 获取主机磁盘信息list集合
        List<OSFileStore> fileStores = fileSystem.getFileStores();
        for (OSFileStore fs : fileStores) {
            // 磁盘空闲容量
            long free = fs.getUsableSpace();
            // 磁盘总容量
            long total = fs.getTotalSpace();
            // 磁盘已使用容量
            long used = total - free;
            SysFile sysFile = new SysFile();
            // 磁盘符号 C:\
            sysFile.setDirName(fs.getMount());
            // 磁盘类型 NTFS
            sysFile.setSysTypeName(fs.getType());
            // 磁盘名称 本地固定磁盘 (C:)
            sysFile.setTypeName(fs.getName());
            // 磁盘总容量
            sysFile.setTotal(convertFileSize(total));
            // 磁盘空闲容量
            sysFile.setFree(convertFileSize(free));
            // 磁盘已使用容量
            sysFile.setUsed(convertFileSize(used));
            // 磁盘资源的使用率
            if (used != 0) {
                sysFile.setUsage(Arith.mul(Arith.div(used, total, 4), 100));
            }else {
                sysFile.setUsage(-1L);
            }
            sysFiles.add(sysFile);
        }
    }

    /**
     * 字节转换
     *
     * @param size 字节大小
     * @return 转换后值
     */
    public String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }
}

