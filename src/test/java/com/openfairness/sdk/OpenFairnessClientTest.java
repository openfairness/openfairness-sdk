package com.openfairness.sdk;


import com.alibaba.fastjson.JSON;
import com.openfairness.sdk.model.OpenFairnessReq;
import com.openfairness.sdk.model.OpenFairnessResult;
import com.openfairness.sdk.model.RequestOptions;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.util.Arrays;

public class OpenFairnessClientTest {

    @Test
    public void testOpenFairness() {
        try {
            OpenFairnessClient.build(RequestOptions.builder()
                    .appId("qwertyuio").build());
            OpenFairnessResult result = OpenFairnessClient.openFairness(OpenFairnessReq.builder()
                    .requestId(RandomStringUtils.randomAlphanumeric(32))
                    .nonce(1)
                    .boxDetails(Arrays.asList(
                            OpenFairnessReq.BoxDetail.builder().rangeStart(1).rangeEnd(5_000_000).build(),
                            OpenFairnessReq.BoxDetail.builder().rangeStart(5_000_001).rangeEnd(10_000_000).build(),
                            OpenFairnessReq.BoxDetail.builder().rangeStart(10_000_001).rangeEnd(50_000_000).build(),
                            OpenFairnessReq.BoxDetail.builder().rangeStart(50_000_001).rangeEnd(100_000_000).build()
                    ))
                    .build());
            System.out.println(JSON.toJSONString(result));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}