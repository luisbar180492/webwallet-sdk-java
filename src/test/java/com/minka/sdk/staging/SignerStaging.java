package com.minka.sdk.staging;


import com.minka.sdk.TestingConstants;
import com.minka.wallet.primitives.utils.SdkApiClient;
import io.minka.api.handler.ApiException;
import io.minka.api.model.*;
import org.junit.Before;
import org.junit.Test;

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
        if (TestingConstants.proxy){
            sdkApiClient.setProxy(TestingConstants.PROXY_HOST,
                    TestingConstants.PROXY_PORT);
        }
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
    }


    @Test
    public void createSignerForOfflineUse() {

        try {
            Keeper offlineKeypair = sdkApiClient.getKeeperForOfflineSigning();

//            Keeper offlineKeypair = sdkApiClient.getKeeper();

         System.out.println("offlineKeypair.getSecret()");

         System.out.println(offlineKeypair.getSecret());
         io.minka.api.model.SignerResponse signerOfflineSigning;
         PublicKeys publickey = new PublicKeys();
         publickey.setPublic(offlineKeypair.getPublic()  );

         publickey.setScheme(offlineKeypair.getScheme());
            SignerLabels labels = new SignerLabels();
            signerOfflineSigning = sdkApiClient.createSignerOfflineSigning(labels, publickey);
            System.out.println(signerOfflineSigning);
        } catch (ApiException e) {
            System.out.println(e.getResponseBody());
            System.out.println(e.getCode());

        }

        // System.out.println(signerOfflineSigning);

     }


    @Test
    public void createSignerForOnlineUse(){

        try {
            SignerLabels labelss = new SignerLabels();
            labelss.put("routerReference","$davjuliandiaz");
            io.minka.api.model.SignerResponse signer = sdkApiClient.createSigner(labelss);
            System.out.println(signer);
            System.out.println(signer.getHandle());//address

        } catch (io.minka.api.handler.ApiException e) {
            System.out.println(e.getResponseBody());
        }
    }


}
