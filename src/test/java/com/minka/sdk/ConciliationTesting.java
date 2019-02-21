package com.minka.sdk;


import com.minka.wallet.primitives.utils.SdkApiClient;
import io.minka.api.handler.ApiException;
import io.minka.api.model.Conciliation;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ConciliationTesting {
    SdkApiClient sdkApiClient;
    @Before
    public void prepare(){

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
        Conciliation conciliation = sdkApiClient.getConciliation("?bankId=$bancorojo&startDate=2019-02-18&endDate=2019-02-21");
        System.out.println(conciliation.getFileName());
        System.out.println(conciliation.getText());

    }

    @Ignore
    @Test
    public void getAnalytics() throws ApiException {
        sdkApiClient.getAnalytics("?");
    }
}
