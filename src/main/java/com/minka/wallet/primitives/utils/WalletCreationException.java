package com.minka.wallet.primitives.utils;


import com.minka.ExceptionResponseTinApi;

public class WalletCreationException extends ExceptionResponseTinApi {

    public WalletCreationException(int errorCode, String message) {
        super(errorCode, message);
    }
}
