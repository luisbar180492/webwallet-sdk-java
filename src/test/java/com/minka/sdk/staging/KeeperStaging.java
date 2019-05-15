package com.minka.sdk.staging;

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
                TestingConstants.API_KEY, TestingConstants.DEV_BASE);

        if (TestingConstants.proxy) {
            sdkApiClient.setProxy(TestingConstants.PROXY_HOST, TestingConstants.PROXY_PORT);
        }
        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);


        io.minka.api.model.Keeper keeper = null;
        try {
            keeper = sdkApiClient.getKeeper();
        } catch (ApiException e) {
            e.printStackTrace();
        }

        assertNotEquals(null, keeper.getSecret() );
        assertNotEquals(null, keeper.getPublic() );

        System.out.println(keeper);
    }

}
