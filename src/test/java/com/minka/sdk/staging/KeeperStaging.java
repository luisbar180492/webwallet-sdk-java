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
    public void shouldReturnKeypair() throws ExceptionResponseTinApi, ApiException {

        SdkApiClient sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN_STAGING,
                TestingConstants.API_KEY);

        sdkApiClient.setProxy(TestingConstants.PROXY_HOST, TestingConstants.PROXY_PORT);

        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);

        sdkApiClient.setOauth2On();
        io.minka.api.model.Keeper keeper = sdkApiClient.getKeeper();

        assertNotEquals(null, keeper.getSecret() );
        assertNotEquals(null, keeper.getPublic() );

        System.out.println(keeper);
    }

    @Test
    public void shouldGetToken() throws ApiException {

        SdkApiClient sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN_STAGING,
                TestingConstants.API_KEY);

        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);

        sdkApiClient.setProxy(TestingConstants.PROXY_HOST, TestingConstants.PROXY_PORT);

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
