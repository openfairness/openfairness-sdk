package com.openfairness.sdk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestOptions {

    private String appId;

    private String apiUrl;
    /**
     * socks url
     */
    private String proxyUrl;
}
