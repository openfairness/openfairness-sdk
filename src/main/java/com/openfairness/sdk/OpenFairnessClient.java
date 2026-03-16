package com.openfairness.sdk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.openfairness.sdk.constant.BaseConstant;
import com.openfairness.sdk.exception.OpenFairnessException;
import com.openfairness.sdk.model.*;
import com.openfairness.sdk.util.HttpUtils;
import okhttp3.Headers;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
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
        return doPost("/open_fairness", req, new TypeReference<BaseRes<OpenFairnessResult>>() {
        });
    }

    public static MultiOpenFairnessResult multipleOpenFairness(MultiOpenFairnessReq req) {
        return multipleOpenFairness(req, requestOptions);
    }

    public static MultiOpenFairnessResult multipleOpenFairness(MultiOpenFairnessReq req, RequestOptions requestOptions) {
        validRequestOptions(requestOptions);
        MultiOpenFairnessResult result = doPost("/multi_open_fairness", req,new TypeReference<BaseRes<MultiOpenFairnessResult>>() {
        });
        return result;
    }

    public static List<BoxMockTypesResult> boxMockTypes() {
        return boxMockTypes(requestOptions);
    }

    public static List<BoxMockTypesResult> boxMockTypes(RequestOptions requestOptions) {
        validRequestOptions(requestOptions);
        List<BoxMockTypesResult> results = doGet("/box_mock/type/list", null, new TypeReference<BaseRes<List<BoxMockTypesResult>>>() {
        });
        return results;
    }

    public static BoxMockResult boxMock(BoxMockReq req) {
        return boxMock(req, requestOptions);
    }

    public static BoxMockResult boxMock(BoxMockReq req, RequestOptions requestOptions) {
        validRequestOptions(requestOptions);
        BoxMockResult result = doPost("/box_mock/box_create", req, new TypeReference<BaseRes<BoxMockResult>>() {
        });
        return result;
    }

    private static <T> T doPost(String path, Object req, TypeReference<BaseRes<T>> type) {
        HttpResult result = HttpUtils.doPost(requestOptions.getApiUrl() + path,
                new Headers.Builder()
                        .add(BaseConstant.AUTHORIZATION, requestOptions.getAppId())
                        .add(BaseConstant.PROXY_KEY, StringUtils.trimToEmpty(requestOptions.getProxyUrl()))
                , req);
        if (result.isSuccess()) {
            return JSON.parseObject(result.getResponse(), type).getData();
        }
        if (StringUtils.isNotBlank(result.getResponse())) {
            BaseRes baseRes = JSON.parseObject(result.getResponse(), type);
            throw new OpenFairnessException(baseRes.getCode(), baseRes.getMsg());
        }
        throw new OpenFairnessException(String.valueOf(result.getCode()), result.getMessage());
    }

    private static <T> T doGet(String path, Object req,TypeReference<BaseRes<T>> type) {
        HttpResult result = HttpUtils.doGet(requestOptions.getApiUrl() + path,
                new Headers.Builder()
                        .add(BaseConstant.AUTHORIZATION, requestOptions.getAppId())
                        .add(BaseConstant.PROXY_KEY, StringUtils.trimToEmpty(requestOptions.getProxyUrl()))
                , req);
        if (result.isSuccess()) {
            return JSON.parseObject(result.getResponse(), type).getData();
        }
        if (StringUtils.isNotBlank(result.getResponse())) {
            BaseRes baseRes = JSON.parseObject(result.getResponse(), type);
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
