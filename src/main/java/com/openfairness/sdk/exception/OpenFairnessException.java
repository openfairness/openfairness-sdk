package com.openfairness.sdk.exception;

import lombok.Getter;

@Getter
public class OpenFairnessException extends RuntimeException {

    private static final long serialVersionUID = 6205986582129733835L;

    private String code;

    private String msg;

    public OpenFairnessException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public OpenFairnessException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}
