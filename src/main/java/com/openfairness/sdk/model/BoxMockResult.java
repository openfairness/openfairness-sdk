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
public class BoxMockResult {

    private BigDecimal minThreshold;

    private BigDecimal middleThreshold;

    private BigDecimal maxThreshold;

    private List<MockSku> skuList;

    @Data
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class MockSku {

        private String skuNo;

        private BigDecimal price;

        private BigDecimal rate;

    }
}
