package com.minka.sdk;

import com.minka.ExceptionResponseTinApi;
import com.minka.api.model.Keeper;
import com.minka.wallet.primitives.utils.SdkApiClient;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.assertNotEquals;


public class KeeperTesting {

    private Logger logger = Logger.getLogger(KeeperTesting.class.getName());

    @Test
    public void shouldReturnKeypair() throws ExceptionResponseTinApi {

        SdkApiClient sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN,
                                                    TestingConstants.API_KEY);

        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);


        Keeper keeper = sdkApiClient.getKeeper();

        assertNotEquals(null, keeper.getSecret() );
        assertNotEquals(null, keeper.getPublic() );

        logger.info(keeper.toString());


    }

    @Test
    public void shouldFail() {

        SdkApiClient sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN,
                TestingConstants.API_KEY + "WRONG");

        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);

        try {
            Keeper keeper = sdkApiClient.getKeeper();
        } catch (ExceptionResponseTinApi exceptionResponseTinApi) {
            int errorCode = exceptionResponseTinApi.getErrorCode();
            String message = exceptionResponseTinApi.getMessage();
            logger.info("errorCode");
            logger.info(String.valueOf(errorCode));
            logger.info("message");
            logger.info(message);

        }


    }
}
