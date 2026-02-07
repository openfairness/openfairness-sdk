package com.openfairness.sdk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.openfairness.sdk.constant.BaseConstant;
import com.openfairness.sdk.exception.OpenFairnessException;
import com.openfairness.sdk.model.*;
import com.openfairness.sdk.util.HttpUtils;
import okhttp3.Headers;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class OpenFairnessClient {

    private static RequestOptions requestOptions;

    public static void build(RequestOptions requestOptions) {
        OpenFairnessClient.requestOptions = requestOptions;
    }

    public static void build(String appId) {
        requestOptions = RequestOptions.builder()
                .apiUrl("https://api.openfairness.com")
                .appId(appId).build();
    }


    public static OpenFairnessResult openFairness(OpenFairnessReq req) {
        return openFairness(req, requestOptions);
    }

    public static OpenFairnessResult openFairness(OpenFairnessReq req, RequestOptions requestOptions) {
        validRequestOptions(requestOptions);
        HttpResult result = HttpUtils.doPost(requestOptions.getApiUrl() + "/open_fairness",
                new Headers.Builder()
                        .add(BaseConstant.AUTHORIZATION, requestOptions.getAppId())
                        .add(BaseConstant.PROXY_KEY, StringUtils.trimToEmpty(requestOptions.getProxyUrl()))
                , req);
        if (result.isSuccess()) {
            return JSON.parseObject(result.getResponse(), new TypeReference<BaseRes<OpenFairnessResult>>() {
            }).getData();
        }
        if (StringUtils.isNotBlank(result.getResponse())) {
            BaseRes baseRes = JSON.parseObject(result.getResponse(), new TypeReference<BaseRes<OpenFairnessResult>>() {
            });
            throw new OpenFairnessException(baseRes.getCode(), baseRes.getMsg());
        }
        throw new OpenFairnessException(String.valueOf(result.getCode()), result.getMessage());
    }

    public static List<BoxMockTypesResult> boxMockTypes() {
        return boxMockTypes(requestOptions);
    }

    public static List<BoxMockTypesResult> boxMockTypes(RequestOptions requestOptions) {
        validRequestOptions(requestOptions);
        HttpResult result = HttpUtils.doGet(requestOptions.getApiUrl() + "/box_mock/type/list",
                new Headers.Builder()
                        .add(BaseConstant.AUTHORIZATION, requestOptions.getAppId())
                        .add(BaseConstant.PROXY_KEY, StringUtils.trimToEmpty(requestOptions.getProxyUrl())));
        if (result.isSuccess()) {
            return JSON.parseObject(result.getResponse(), new TypeReference<BaseRes<List<BoxMockTypesResult>>>() {
            }).getData();
        }
        if (StringUtils.isNotBlank(result.getResponse())) {
            BaseRes baseRes = JSON.parseObject(result.getResponse(), new TypeReference<BaseRes<List<BoxMockTypesResult>>>() {
            });
            throw new OpenFairnessException(baseRes.getCode(), baseRes.getMsg());
        }
        throw new OpenFairnessException(String.valueOf(result.getCode()), result.getMessage());
    }

    public static BoxMockResult boxMock(BoxMockReq req) {
        return boxMock(req, requestOptions);
    }

    public static BoxMockResult boxMock(BoxMockReq req, RequestOptions requestOptions) {
        validRequestOptions(requestOptions);
        HttpResult result = HttpUtils.doPost(requestOptions.getApiUrl() + "/box_mock/box_create",
                new Headers.Builder()
                        .add(BaseConstant.AUTHORIZATION, requestOptions.getAppId())
                        .add(BaseConstant.PROXY_KEY, StringUtils.trimToEmpty(requestOptions.getProxyUrl()))
                , req);
        if (result.isSuccess()) {
            return JSON.parseObject(result.getResponse(), new TypeReference<BaseRes<BoxMockResult>>() {
            }).getData();
        }
        if (StringUtils.isNotBlank(result.getResponse())) {
            BaseRes baseRes = JSON.parseObject(result.getResponse(), new TypeReference<BaseRes<BoxMockResult>>() {
            });
            throw new OpenFairnessException(baseRes.getCode(), baseRes.getMsg());
        }
        throw new OpenFairnessException(String.valueOf(result.getCode()), result.getMessage());
    }

    private static void validRequestOptions(RequestOptions requestOptions) {
        if (requestOptions == null) {
            throw new OpenFairnessException("RequestOptions is required");
        }
        if (StringUtils.isBlank(requestOptions.getAppId())) {
            throw new OpenFairnessException("AppId is required");
        }
        if (StringUtils.isBlank(requestOptions.getApiUrl())) {
            throw new OpenFairnessException("AppUrl is required");
        }
    }
}
