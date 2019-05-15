package com.minka.sdk;

import com.minka.ExceptionResponseTinApi;
import com.minka.wallet.primitives.utils.SdkApiClient;
import io.minka.api.handler.ApiException;
import io.minka.api.model.Keeper;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.assertNotEquals;


public class KeeperTesting {

    private Logger logger = Logger.getLogger(KeeperTesting.class.getName());

    SdkApiClient sdkApiClient;

    @Before
    public void prepare(){
        sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN_TESTING,
                TestingConstants.API_KEY,TestingConstants.STAGING_BASE);

        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);

    }

    @Test
    public void shouldReturnKeypairTesting() throws ExceptionResponseTinApi, ApiException {

        io.minka.api.model.Keeper keeper = sdkApiClient.getKeeper();

        assertNotEquals(null, keeper.getSecret() );
        assertNotEquals(null, keeper.getPublic() );

        logger.info(keeper.toString());


    }

    @Test
    public void shouldGenerateOfflineSigningKeypair() throws ApiException {

        SdkApiClient sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN_TESTING,
                TestingConstants.API_KEY, TestingConstants.TESTING_BASE);

        Keeper keypair = sdkApiClient.getKeeperForOfflineSigning();
        System.out.println(keypair);
        assertNotEquals(keypair.getPublic(), null);
        assertNotEquals(keypair.getScheme(), null);
        assertNotEquals(keypair.getSecret(), null);
    }
}
