package com.minka.sdk;

import com.minka.ExceptionResponseTinApi;
import com.minka.wallet.primitives.utils.SdkApiClient;
import io.minka.api.handler.ApiException;
import io.minka.api.model.GetTransfersResponse;
import io.minka.api.model.GetVendorsResponse;
import io.minka.api.model.Keeper;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.assertNotEquals;


public class ShdTesting {

    private Logger logger = Logger.getLogger(KeeperTesting.class.getName());

    SdkApiClient sdkApiClient;

    @Before
    public void prepare(){
        sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN_TESTING,
                TestingConstants.API_KEY,"http://e0e09e39.ngrok.io");

//        sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN_TESTING,
//                TestingConstants.API_KEY,"https://b450c068.ngrok.io");
        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);

        if (TestingConstants.proxy){
            sdkApiClient.setProxy(TestingConstants.PROXY_HOST, TestingConstants.PROXY_PORT);
        }

    }


    @Test
    public void getActionsWithCustomQuery() throws ApiException {
        String vendorId = "$shd_bogota";
        String invoiceId = "1000";
        String customQuery = "?param=value&apd=32";
        GetVendorsResponse getVendorsResponse = sdkApiClient.verifyPayment(vendorId, invoiceId, customQuery);

    }


}
