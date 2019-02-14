package com.minka.sdk;


import com.minka.ExceptionResponseTinApi;
import com.minka.wallet.primitives.utils.SdkApiClient;
import io.minka.api.handler.ApiException;
import io.minka.api.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SignerTesting {

    SdkApiClient sdkApiClient;

    @Before
    public void prepare() throws ApiException {

        sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN_TESTING,
                TestingConstants.API_KEY,TestingConstants.TESTING_BASE);

        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);

        if (TestingConstants.proxy){
            sdkApiClient.setProxy(TestingConstants.PROXY_HOST,
                    TestingConstants.PROXY_PORT);
        }
    }


    @Test
    public void shouldGetSignersWithPaging() throws io.minka.api.handler.ApiException {

//        SignerResponse signer = sdkApiClient.getSignerByAddress("wQxWXHPCDcfmGnuNPRAxfVoxWH79YcGBJV");


        SignerRequest req = new SignerRequest();
        SignerRequestLabels labels = new SignerRequestLabels();
        labels.setRouterReference("$bancoheroku");
        req.setLabels(labels);
        sdkApiClient.updateSigner("wQxWXHPCDcfmGnuNPRAxfVoxWH79YcGBJV", req);
    }


    @Test
    public void createSignerForOfflineUse() throws io.minka.api.handler.ApiException, ExceptionResponseTinApi {

        Keeper offlineKeypair = sdkApiClient.getKeeperForOfflineSigning();

        SignerRequestLabels labels = new SignerRequestLabels();

        System.out.println("offlineKeypair.getSecret()");

        io.minka.api.model.SignerResponse signerOfflineSigning;
        PublicKeys publickey = new PublicKeys();
        publickey.setPublic(offlineKeypair.getPublic());

        publickey.setScheme(offlineKeypair.getScheme());
        signerOfflineSigning = sdkApiClient.createSignerOfflineSigning(labels, publickey);

        System.out.println(signerOfflineSigning);

    }


    @Test
    public void createSignerForOnlineUse(){

        try {
            SignerRequestLabels labels = new SignerRequestLabels();
            labels.setRouterReference("$davjuliandiaz");
            io.minka.api.model.SignerResponse signer = sdkApiClient.createSigner(labels);
            System.out.println(signer);
            System.out.println(signer.getHandle());//address

        } catch (io.minka.api.handler.ApiException e) {
            System.out.println(e.getResponseBody());
        }
    }


}
