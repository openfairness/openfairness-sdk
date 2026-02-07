package com.openfairness.sdk.model;

import lombok.Data;

@Data
public class OpenFairnessResult {

    private String requestId;

    private String serverSeed;

    private String clientSeed;

    private Integer nonce;

    private Integer seedResult;
}
