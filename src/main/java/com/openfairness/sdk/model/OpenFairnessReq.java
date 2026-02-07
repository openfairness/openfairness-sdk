package com.openfairness.sdk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OpenFairnessReq {
    private String requestId;

    private String serverSeed;

    private String clientSeed;

    private Integer nonce;

    private List<BoxDetail> boxDetails;


    @Data
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class BoxDetail {

        private Integer rangeStart;

        private Integer rangeEnd;
    }
}
