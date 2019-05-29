package com.minka.sdk;

import com.minka.ExceptionResponseTinApi;
import com.minka.wallet.primitives.utils.SdkApiClient;
import io.minka.api.handler.ApiException;
import io.minka.api.model.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.assertNotEquals;


public class ShdTesting {

    private Logger logger = Logger.getLogger(KeeperTesting.class.getName());

    SdkApiClient sdkApiClient;

    @Before
    public void prepare(){
        sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN_TESTING,
                TestingConstants.API_KEY,"https://achtin-dev.minka.io/v1");
//        TestingConstants.API_KEY,"http://e0e09e39.ngrok.io/v1");



//        sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN_TESTING,
//                TestingConstants.API_KEY,"https://b450c068.ngrok.io");
        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);

        if (TestingConstants.proxy){
            sdkApiClient.setProxy(TestingConstants.PROXY_HOST, TestingConstants.PROXY_PORT);
        }

    }

    @Ignore
    @Test
    public void getActionsWithCustomQuery() throws ApiException {
        String vendorId = "$shd_bogota";
        String invoiceId = "1000";
        String customQuery = "?";
        GetVendorsResponse getVendorsResponse = sdkApiClient.verifyPayment(vendorId, invoiceId, customQuery);

        System.out.println(getVendorsResponse);
    }

    @Ignore
    @Test
    public void createPaymentAction() throws ApiException {
        CreateActionRequest req = new io.minka.api.model.CreateActionRequest();
        req.setSource("$banco_rojo");
        req.setSymbol("$tin");
        req.setTarget("$shd_bogota");
        req.setAmount("100.23");
        req.setSymbol("$tin");
        CreateActionRequestLabels labels = new CreateActionRequestLabels();
        labels.setType("PAYMENT");
        labels.setStatus("PENDING");
        labels.setDomain("shd");
        labels.setDetail("23412342134");
        labels.setNature("60");
        labels.setInvoice("6676532982721");
        labels.setSubscription("1234784901");
        req.setLabels(labels);
        CreateActionResponse action = sdkApiClient.createAction(req);
        System.out.println(action);
    }

    @Ignore
    @Test
    public void signActionOffline() throws ApiException {
        String actionId = "ca2d76e3-8887-48b7-a572-b4ca6bf0442d";

//        OfflineSigningKeys keys = new OfflineSigningKeys();
//        List<PublicKeys> keeper = new ArrayList<>();
//        PublicKeys theKeys = new PublicKeys();
//        theKeys.setPublic("73962f7467934bcd42d9c9e4a65efa5002c20b390d90f474f7c7e74d9c439cc3");
//        theKeys.setScheme("eddsa-ed25519");
//        theKeys.setSecret("0bf2ee2e4847991be298b0b28f6bf254de5e8479af16f7f14ac2c816adb68ff6");
//        keeper.add(theKeys);
//        keys.setKeeper(keeper);
        ActionSignedLabels actionLabels = new ActionSignedLabels();
        ActionSigned actionSigned = sdkApiClient.signAction(actionId, actionLabels);
        System.out.println(actionSigned);
    }
    @Ignore
    @Test
    public void createPaymentTransfer() throws ApiException {
        CreateTransferRequest req = new CreateTransferRequest();
        req.setActionId("ca2d76e3-8887-48b7-a572-b4ca6bf0442d");
        CreateTransferRequestLabels labels = new CreateTransferRequestLabels();
        labels.setDetail("23412342134");
        labels.setNature("60");
        labels.setInvoice("6676532982721");
        labels.setSubscription("1234784901");
        labels.setTxId("ee991f91-e118-4ecf-9fa7-fadd36a15c0e");
        labels.setType("PAYMENT");
        labels.setDescription("Description of a transfer");
        req.setLabels(labels);
        CreateTransferResponse paymentTransfer = sdkApiClient.createPaymentTransfer(req);
        System.out.println(paymentTransfer);

    }
}
