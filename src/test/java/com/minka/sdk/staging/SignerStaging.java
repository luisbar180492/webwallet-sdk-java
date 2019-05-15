package com.minka.sdk.staging;


import com.minka.ExceptionResponseTinApi;
import com.minka.sdk.TestingConstants;
import com.minka.wallet.primitives.utils.SdkApiClient;
import io.minka.api.handler.ApiException;
import io.minka.api.model.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class SignerStaging {


    SdkApiClient sdkApiClient;

    @Before
    public void prepare() throws ApiException {

        sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN_STAGING,
                TestingConstants.API_KEY, TestingConstants.STAGING_BASE);

        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);


    }


    @Test
    public void shouldGetSignersWithPaging() {

        SignerListResponse signers = null;
        try {
            signers = sdkApiClient.getSigners(1, 2);
          //  System.out.println(signers.size());
            System.out.println(signers);
        } catch (io.minka.api.handler.ApiException e) {
            System.out.println(e.getResponseBody());
        }
    }


    @Ignore
    @Test
    public void createSignerForOfflineUse() throws ExceptionResponseTinApi {

        //Keeper offlineKeypair = sdkApiClient.getKeeperForOfflineSigning();

        try {
            Keeper offlineKeypair = sdkApiClient.getKeeper();

        SignerRequestLabels labels = new SignerRequestLabels();

         System.out.println("offlineKeypair.getSecret()");

         System.out.println(offlineKeypair.getSecret());
         io.minka.api.model.SignerResponse signerOfflineSigning;
         PublicKeys publickey = new PublicKeys();
         publickey.setPublic(offlineKeypair.getPublic()  );

         publickey.setScheme(offlineKeypair.getScheme());
            signerOfflineSigning = sdkApiClient.createSignerOfflineSigning(labels, publickey);
        } catch (ApiException e) {
            System.out.println(e.getResponseBody());
            System.out.println(e.getCode());

        }

        // System.out.println(signerOfflineSigning);

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
