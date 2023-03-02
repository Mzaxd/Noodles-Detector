package com.mzaxd.noodles.domain.message;

/**
 * @author Mzaxd
 * @since 2023-02-05 10:51
 */

import lombok.Data;

/**
 * 系统相关信息
 *
 * @author huasheng
 */
@Data
public class Sys
{
    /**
     * manufacturer
     */
    private String manufacturer;

    /**
     * model
     */
    private String model;

    /**
     * serialNumber
     */
    private String serialNumber;

    /**
     * uuid
     */
    private String uuid;
}



