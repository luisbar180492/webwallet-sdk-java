package com.minka.sdk.staging;

import com.minka.OauthClient;
import com.minka.sdk.TestingConstants;
import com.minka.wallet.primitives.utils.SdkApiClient;
import io.minka.api.handler.ApiException;
import io.minka.api.model.Keeper;
import io.minka.api.model.TokenResponse;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.assertNotEquals;

public class KeeperStaging {

    private Logger logger = Logger.getLogger(KeeperStaging.class.getName());

    @Test
    public void shouldReturnKeypair() throws ApiException {

        SdkApiClient sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN_STAGING,
                TestingConstants.API_KEY, TestingConstants.STAGING_BASE);

        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);

        Keeper keeper = null;
        try {
            keeper = sdkApiClient.getKeeper();
            assertNotEquals(null, keeper.getSecret() );
            assertNotEquals(null, keeper.getPublic() );
            logger.info(keeper.toString());
        } catch (ApiException e){
            logger.info(e.getResponseBody());
        }
    }

    @Test
    public void shouldGetToken() throws ApiException {

        OauthClient oauthClient = new OauthClient(TestingConstants.CLIENT_ID, TestingConstants.SECRET,
                "https://achtin-dev.minka.io/");

        TokenResponse token = oauthClient.getToken();
        logger.info("This is a token:" + token.getAccessToken());
    }
}
