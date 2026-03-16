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
public class MultiOpenFairnessResult {
    private String requestId;

    private List<SeedResult> seedResults;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SeedResult {
        private Integer seedResult;

        private String clientSeed;

        private String serverSeed;
        private String serverSeedHashed;

        private Integer nonce;
    }
}
