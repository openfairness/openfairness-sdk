package com.openfairness.sdk.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description
 * @author: Mi
 * @Date: 2026/02/06
 **/
@Data
public class BoxMockTypesResult {

    private String type;

    private String name;

    private BigDecimal defaultWinRate;

    private BigDecimal defaultMinProfitRate;

    private BigDecimal defaultMaxProfitRate;

    private BigDecimal defaultPMin;

    private BigDecimal defaultPMax;

    private String defaultProfitType;

    private String remark;
}
