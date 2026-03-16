package com.openfairness.sdk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MultiOpenFairnessReq {

    private String requestId;

    private List<Seed> seeds;

    private List<BoxDetail> boxDetails;


    @Data
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class BoxDetail {

        private Integer rangeStart;

        private Integer rangeEnd;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Seed {
        private String serverSeed;
        private String clientSeed;

        private Integer nonce;
    }
}
