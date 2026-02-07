package com.openfairness.sdk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description
 * @author: Mi
 * @Date: 2026/02/06
 **/
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BoxMockReq {

    /**
     * 开盒价
     */
    private BigDecimal boxPrice;

    /**
     * 最小利润率
     */
    private BigDecimal minProfitRate;

    /**
     * 最大利润率
     */
    private BigDecimal maxProfitRate;

    /**
     * 胜率
     */
    private BigDecimal winRate;

    /**
     * 输赢比范围
     */
    private BigDecimal pMin;
    private BigDecimal pMax;

    /**
     * 模板类型
     */
    private String temType;

    private List<BoxSku> skuList;

    @Data
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class BoxSku {

        private String skuNo;

        private String groupCode;

        private BigDecimal price;

    }
}
