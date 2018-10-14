package com.minka.sdk;

import com.minka.ExceptionResponseTinApi;
import com.minka.wallet.primitives.utils.SdkApiClient;
import io.minka.api.handler.ApiException;
import io.minka.api.model.Keeper;
import io.minka.api.model.TokenResponse;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.assertNotEquals;


public class KeeperTesting {

    private Logger logger = Logger.getLogger(KeeperTesting.class.getName());

    @Test
    public void shouldReturnKeypairTesting() throws ExceptionResponseTinApi, ApiException {

        SdkApiClient sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN,
                TestingConstants.API_KEY);

        io.minka.api.model.Keeper keeper = sdkApiClient.getKeeper();

        assertNotEquals(null, keeper.getSecret() );
        assertNotEquals(null, keeper.getPublic() );

        logger.info(keeper.toString());


    }

    @Test
    public void shouldReturnKeypair() throws ExceptionResponseTinApi, ApiException {

        SdkApiClient sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN_STAGING,
                                                    TestingConstants.API_KEY);

        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);

        io.minka.api.model.Keeper keeper = sdkApiClient.getKeeper();

        assertNotEquals(null, keeper.getSecret() );
        assertNotEquals(null, keeper.getPublic() );

        logger.info(keeper.toString());


    }


    @Test
    public void shouldGetHelloWorldOauth() throws ApiException {

        SdkApiClient sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN_STAGING,
                TestingConstants.API_KEY);

        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);

        try {
            TokenResponse ping = sdkApiClient.getToken();
            System.out.println(ping);

        } catch (ApiException e) {
            e.printStackTrace();
            System.out.print(e.getCode());

            System.out.print(e.getResponseBody());
        }
    }

    @Test
    public void shouldGenerateOfflineSigningKeypair() throws ApiException {

        SdkApiClient sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN,
                TestingConstants.API_KEY);

        Keeper keypair = sdkApiClient.getKeeperForOfflineSigning();
        System.out.println(keypair);
        assertNotEquals(keypair.getPublic(), null);
        assertNotEquals(keypair.getScheme(), null);
        assertNotEquals(keypair.getSecret(), null);
    }
}
