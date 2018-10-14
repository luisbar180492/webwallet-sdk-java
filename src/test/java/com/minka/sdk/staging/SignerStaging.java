package com.minka.sdk.staging;


import com.minka.sdk.TestingConstants;
import com.minka.wallet.primitives.utils.SdkApiClient;
import io.minka.api.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SignerStaging {


    SdkApiClient sdkApiClient;

    @Before
    public void prepare(){

        sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN_STAGING,
                TestingConstants.API_KEY);

        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);
        sdkApiClient.setOauth2On();
    }


    @Test
    public void shouldGetSignersWithPaging() {

        SignerListResponse signers = null;
        try {
            signers = sdkApiClient.getSigners(1, 2);
            System.out.println(signers.size());
            System.out.println(signers);
        } catch (io.minka.api.handler.ApiException e) {
            System.out.println(e.getResponseBody());
        }

//        assertEquals(signers.size(), 3);//BECAUSE OF THE pagesize + 1 error element

//        io.minka.api.model.SignerResponse wAddress = sdkApiClient.getSignerByAddress("wTLsUYdoo8vNLwJqxgb5aUpxSCm6zfCBPz");
//        System.out.println(wAddress);

//        SignerRequest req = new SignerRequest();
//        Map<String, Object> labels = new HashMap<>();
//        labels.put("description", "saving");
//        req.setLabels(labels);
//        sdkApiClient.updateSigner("wTLsUYdoo8vNLwJqxgb5aUpxSCm6zfCBPz", req);
    }


//    @Test
//    public void createSignerForOfflineUse() throws io.minka.api.handler.ApiException, ExceptionResponseTinApi {
//
//        Keeper offlineKeypair = sdkApiClient.getKeeperForOfflineSigning();
//
//        SignerRequestLabels labels = new SignerRequestLabels();
//
//        System.out.println("offlineKeypair.getSecret()");
//
//        System.out.println(offlineKeypair.getSecret());
//        io.minka.api.model.SignerResponse signerOfflineSigning;
//        PublicKeys publickey = new PublicKeys();
//        publickey.setPublic(offlineKeypair.getPublic());
//
//        publickey.setScheme(offlineKeypair.getScheme());
//        signerOfflineSigning = sdkApiClient.createSignerOfflineSigning(labels, publickey);
//
//        System.out.println(signerOfflineSigning);
//
//    }


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
