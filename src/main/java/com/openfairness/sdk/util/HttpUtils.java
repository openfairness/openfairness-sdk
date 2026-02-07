package com.openfairness.sdk.util;


import com.alibaba.fastjson.JSON;
import com.openfairness.sdk.constant.BaseConstant;
import com.openfairness.sdk.model.HttpResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public class HttpUtils {
    private static Map<String, OkHttpClient> clientMap = new HashMap<>();


    private static OkHttpClient client(ClientParam param) {
        String proxyUrl = param.getProxyUrl();
        OkHttpClient okHttpClient = clientMap.get(proxyUrl + "_" + param.getSsl());
        if (okHttpClient == null) {
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true);
            if (StringUtils.isNotBlank(proxyUrl)) {
                clientBuilder.proxy(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxyUrl.split(":")[0], Integer.parseInt(proxyUrl.split(":")[1]))));
            }
            //跳过证书验证
            X509TrustManager x509TrustManager = new X509TrustManager() {
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            };
            final TrustManager[] trustAllCerts = new TrustManager[]{x509TrustManager};
            try {
                final SSLContext sslContext;
                if (StringUtils.isNotBlank(param.getSsl())) {
                    sslContext = SSLContext.getInstance(param.getSsl());
                } else {
                    sslContext = SSLContext.getInstance("SSL");
                }
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                clientBuilder.sslSocketFactory(sslSocketFactory, x509TrustManager);
                clientBuilder.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
            } catch (KeyManagementException | NoSuchAlgorithmException e) {
                log.error(e.getMessage(), e);
            }
            okHttpClient = clientBuilder.build();
            clientMap.put(proxyUrl, okHttpClient);
        }
        return okHttpClient;
    }

    public static MediaType JSON_UTF8 = MediaType.parse("application/json; charset=utf-8");
    public static MediaType FORM_UTF8 = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");


    public static HttpResult doGet(String url) {
        return doGet(url, null, null);
    }

    public static HttpResult doGet(String url, Headers.Builder headers) {
        return doGet(url, headers, null);
    }

    /**
     * GET请求带参数
     *
     * @param url
     * @param queryParam
     * @return
     */
    public static HttpResult doGet(String url, Object queryParam) {
        return doGet(url, null, queryParam);
    }

    /**
     * GET请求带参数
     *
     * @param url
     * @param queryParam
     * @return
     */
    public static HttpResult doGet(String url, Map<String, Object> queryParam) {
        return doGet(url, null, queryParam);
    }

    /**
     * GET请求带参数
     *
     * @param url
     * @param queryParam
     * @return
     */
    public static HttpResult doGet(String url, Headers.Builder headers, Object queryParam) {
        if (queryParam == null) {
            return doGet(url, headers, null);
        }
        Map<String, Object> param;
        if (queryParam instanceof Map) {
            param = (Map<String, Object>) queryParam;
        } else {
            try {
                param = (Map<String, Object>) objectToMap(queryParam);
            } catch (Exception e) {
                log.error("queryParam occur error,e={}", e.getMessage(), e);
                return HttpResult.builder().isSuccess(false).code(10000).message("queryParam occur error," + e.getMessage()).build();
            }
        }
        return doGet(url, headers, param);
    }

    public static Map<?, ?> objectToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        return new org.apache.commons.beanutils.BeanMap(obj);
    }

    /**
     * GET请求
     *
     * @param url
     * @param param
     * @return
     */
    public static HttpResult doGet(String url, Headers.Builder headers, Map<String, Object> param) {
        HttpUrl.Builder httpBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        if (param != null) {
            param.forEach((k, v) -> {
                if (v != null && !"class".equalsIgnoreCase(k)) {
                    httpBuilder.addQueryParameter(k, v.toString());
                }
            });
        }
        final Request.Builder request = new Request.Builder().url(httpBuilder.build()).get();
        ClientParam key = initParam(headers, request);
        return newCall(key, request.build());
    }

    /**
     * POST请求
     *
     * @param url
     * @param param
     * @return
     */
    public static HttpResult doPost(String url, Object param) {
        return doPost(url, null, param);
    }

    /**
     * POST请求
     *
     * @param url
     * @param param
     * @return
     */
    public static HttpResult doPost(String url, String param) {
        return doPost(url, null, param);
    }

    /**
     * POST请求
     *
     * @param url
     * @param param
     * @return
     */
    public static HttpResult doPost(String url, Headers.Builder headers, Object param) {
        return doPost(url, headers, JSON.toJSONString(param));
    }

    public static HttpResult doPostJson(String url, Map<String, String> headers, Object param) {
        Headers.Builder headBuilder = new Headers.Builder();
        if (headers != null) {
            headers.forEach(headBuilder::add);
        }
        return doPost(url, headBuilder, param);
    }

    /**
     * POST form 请求
     *
     * @param url
     * @param param
     * @return
     */
    public static HttpResult doPostForm(String url, Headers.Builder headers, Map<String, Object> param) {
        List<String> params = new ArrayList<>();
        if (param != null) {
            for (Map.Entry<String, Object> stringObjectEntry : param.entrySet()) {
                params.add(stringObjectEntry.getKey() + "=" + stringObjectEntry.getValue());
            }
        }
        return doPost(url, headers, FORM_UTF8, StringUtils.join(params, "&"));
    }

    /**
     * POST请求
     *
     * @param url
     * @param paramJsonStr
     * @return
     */
    public static HttpResult doPost(String url, Headers.Builder headers, MediaType mediaType, String paramJsonStr) {
        HttpUrl.Builder httpBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        RequestBody requestBody = RequestBody.create(mediaType, paramJsonStr);
        final Request.Builder request = new Request.Builder().url(httpBuilder.build()).post(requestBody);
        ClientParam key = initParam(headers, request);
        return newCall(key, request.build());
    }

    /**
     * POST请求
     *
     * @param url
     * @param paramJsonStr
     * @return
     */
    public static HttpResult doDelete(String url, Headers.Builder headers, MediaType mediaType, String paramJsonStr) {
        HttpUrl.Builder httpBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        RequestBody requestBody = RequestBody.create(mediaType, paramJsonStr);
        final Request.Builder request = new Request.Builder().url(httpBuilder.build()).delete(requestBody);
        ClientParam key = initParam(headers, request);
        return newCall(key, request.build());
    }

    /**
     * POST请求
     *
     * @param url
     * @param paramJsonStr
     * @return
     */
    public static HttpResult doPut(String url, Headers.Builder headers, MediaType mediaType, String paramJsonStr) {
        HttpUrl.Builder httpBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        RequestBody requestBody = RequestBody.create(mediaType, paramJsonStr);
        final Request.Builder request = new Request.Builder().url(httpBuilder.build()).put(requestBody);
        ClientParam key = initParam(headers, request);
        return newCall(key, request.build());
    }

    public static String map2Params(Map<String, Object> param) {
        List<String> params = new ArrayList<>();
        if (param != null) {
            for (Map.Entry<String, Object> stringObjectEntry : param.entrySet()) {
                params.add(stringObjectEntry.getKey() + "=" + stringObjectEntry.getValue());
            }
        }
        return StringUtils.join(params, "&");
    }

    /**
     * POST请求
     *
     * @param url
     * @param paramJsonStr
     * @return
     */
    public static HttpResult doPost(String url, Headers.Builder headers, String paramJsonStr) {
        return doPost(url, headers, JSON_UTF8, paramJsonStr);
    }

    private static HttpResult newCall(ClientParam clientParam, Request request) {
        String result = "";
        try (Response response = client(clientParam).newCall(request).execute()) {
            //获取错误信息
            final String message = response.code() + " : " + response.message();
            if (response.body() != null) {
                result = response.body().string();
            }
            if (response.isSuccessful()) {
                log.info("request success，url={},result:{}", request.url(), result);
                return HttpResult.builder().isSuccess(true).code(response.code()).response(result).build();
            } else {
                log.error("request failed,url:{},message={},result={}", request.url(), message, result);
                return HttpResult.builder().isSuccess(false).code(response.code()).message(response.message()).response(result).build();
            }
        } catch (Exception e) {
            log.error("request error,url:{}", request.url(), e);
            return HttpResult.builder().isSuccess(false).code(999).message(e.getMessage()).build();
        }
    }


    private static ClientParam initParam(Headers.Builder headers, Request.Builder request) {
        if (headers != null) {
            String proxyUrl = headers.get(BaseConstant.PROXY_KEY);
            String ssl = headers.get(BaseConstant.SSL);
            headers.set(BaseConstant.PROXY_KEY, "");
            headers.set(BaseConstant.SSL, "");
            request.headers(headers.build());
            return ClientParam.builder().proxyUrl(proxyUrl).ssl(ssl).build();
        }
        return ClientParam.builder().build();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ClientParam {
        private String proxyUrl;
        private String ssl;
    }

}