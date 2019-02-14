package com.minka.sdk;


import com.minka.wallet.primitives.utils.SdkApiClient;
import io.minka.api.handler.ApiException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ConciliationTesting {
    SdkApiClient sdkApiClient;
    @Before
    public void prepare() throws ApiException {

        sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN_TESTING,
                TestingConstants.API_KEY, TestingConstants.TESTING_BASE);

        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);
        if (TestingConstants.proxy){
            sdkApiClient.setProxy(TestingConstants.PROXY_HOST, TestingConstants.PROXY_PORT);
        }
    }

    @Ignore
    @Test
    public void concilia() throws ApiException {
        sdkApiClient.getConciliation("?");
    }

    @Ignore
    @Test
    public void getAnalytics() throws ApiException {
        sdkApiClient.getAnalytics("?");
    }
}
