package com.openfairness.sdk.model;

import lombok.Data;

@Data
public class BaseRes <T>{

    private String code;

    private String msg;

    private T data;
}
