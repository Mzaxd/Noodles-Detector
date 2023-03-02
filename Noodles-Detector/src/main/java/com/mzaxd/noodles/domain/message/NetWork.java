package com.mzaxd.noodles.domain.message;

/**
 * @author Mzaxd
 * @since 2023-02-05 10:52
 */

import lombok.Data;

import java.util.List;

/**
 * 网速相关信息
 *
 * @author huasheng
 */
@Data
public class NetWork {

    /**
     * 主机ip
     */
    private String hostAddress;

    /**
     * hostName
     */
    private String hostName;

    /**
     * domainName
     */
    private String domainName;

    /**
     * dnsServers
     */
    private List<String> dnsServers;

    /**
     * ipv4DefaultGateway
     */
    private String ipv4DefaultGateway;

    /**
     * ipv6DefaultGateway
     */
    private String ipv6DefaultGateway;

    /**
     * 上行速度
     */
    private String txPercent;
    /**
     * 下行速度
     */
    private String rxPercent;
}

