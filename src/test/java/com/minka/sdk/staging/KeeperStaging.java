package com.minka.sdk.staging;

import com.minka.ExceptionResponseTinApi;
import com.minka.sdk.TestingConstants;
import com.minka.wallet.primitives.utils.SdkApiClient;
import io.minka.api.handler.ApiException;
import io.minka.api.model.TokenResponse;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

public class KeeperStaging {

    @Test
    public void shouldReturnKeypair() {

        SdkApiClient sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN_STAGING,
                TestingConstants.API_KEY, TestingConstants.STAGING_BASE);

        if (TestingConstants.proxy) {
            sdkApiClient.setProxy(TestingConstants.PROXY_HOST, TestingConstants.PROXY_PORT);
        }
        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);


        io.minka.api.model.Keeper keeper = null;
        try {
            keeper = sdkApiClient.getKeeper();
        } catch (ExceptionResponseTinApi exceptionResponseTinApi) {
           // exceptionResponseTinApi.printStackTrace();
        }

        assertNotEquals(null, keeper.getSecret() );
        assertNotEquals(null, keeper.getPublic() );

        System.out.println(keeper);
    }

    @Test
    public void shouldGetToken() throws ApiException {

        SdkApiClient sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN_TESTING,
                TestingConstants.API_KEY,TestingConstants.TESTING_BASE);

        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);

        if (TestingConstants.proxy){
            sdkApiClient.setProxy(TestingConstants.PROXY_HOST, TestingConstants.PROXY_PORT);
        }

        sdkApiClient.setOauth2On();
        try {
            TokenResponse ping = sdkApiClient.getToken();
            System.out.println(ping);

        } catch (ApiException e) {
            e.printStackTrace();
            System.out.print(e.getCode());

            System.out.print(e.getResponseBody());
        }
    }

}
