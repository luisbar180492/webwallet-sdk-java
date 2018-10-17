package com.minka.sdk;

import com.minka.ExceptionResponseTinApi;
import com.minka.wallet.primitives.utils.SdkApiClient;
import com.minka.wallet.primitives.utils.WalletCreationException;
import io.minka.api.model.*;
import io.minka.api.model.WalletUpdateRequest;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Ignore;

public class WalletTesting {

    private static final int LENGTH_HANDLE = 10;
    SdkApiClient sdkApiClient;
    String existingHandle;

    @Before
    public void prepare(){
        sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN,
                TestingConstants.API_KEY);

        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);

        sdkApiClient.setOauth2Off();

        if (TestingConstants.proxy){
            sdkApiClient.setProxy(TestingConstants.PROXY_HOST, TestingConstants.PROXY_PORT);
        }

    }

    @Test
    public void createWalletProperly() {
        String handle = "$" + RandomStringUtils.randomAlphabetic(19).toLowerCase();

        try {
            WalletRequest walletReq  = new WalletRequest();
            walletReq.setHandle(handle);
            WalletRequestLabels labelsReq = new WalletRequestLabels();
            labelsReq.setType("TROUPE");
            labelsReq.setRouterAction("https://api.bank.com/v1/action");
            labelsReq.setRouterDownload("");

            walletReq.setLabels(labelsReq);
            io.minka.api.model.WalletResponse wallet = sdkApiClient.createWallet(walletReq);
            System.out.println(wallet);

            assertEquals(TestingConstants.SUCCESS_ERROR_CODE, wallet.getError().getCode().intValue());
            existingHandle = handle;

        } catch (WalletCreationException e) {
            e.printStackTrace();
        }

    }

    @Test(expected = WalletCreationException.class)
    public void shouldNotCreateWalletWithWrongHandle() throws WalletCreationException {
        String handle = "$" + RandomStringUtils.randomAlphabetic(21).toLowerCase();

        System.out.println("handle");
        System.out.println(handle);

        WalletRequest walletReq =  new WalletRequest();
        WalletRequestLabels labels = new WalletRequestLabels();
        walletReq.setLabels(labels);
        walletReq.setHandle(handle);
        io.minka.api.model.WalletResponse wallet;
        wallet = sdkApiClient.createWallet(walletReq);

    }

    @Test
    public void shouldRetrieveCreatedWallet() {
        existingHandle = "$3104845181";

        assertNotEquals(existingHandle, null);
        try {
            io.minka.api.model.GetWalletResponse wallet = sdkApiClient.getWallet(existingHandle);
            System.out.println(wallet);
            assertEquals(wallet.getError().getCode().intValue(), TestingConstants.SUCCESS_ERROR_CODE);

        } catch (ExceptionResponseTinApi exceptionResponseTinApi) {
            exceptionResponseTinApi.printStackTrace();
        }

    }

    @Test
    public void shouldGetWalletByAlias() throws io.minka.api.handler.ApiException {
        io.minka.api.model.GetWalletResponse wallets;
        wallets =
                sdkApiClient.getWalletByAlias("" +
                        "$offlinedemoath");
        System.out.println(wallets);

//        io.minka.api.model.GetWalletResponse wallets;
//        wallets = sdkApiClient.deleteWalletByAlias("$usxshvwjoptfixfdakh");
//        System.out.println(wallets);

    }

    @Test
    public void shouldGetWallets() throws io.minka.api.handler.ApiException {
        WalletListResponse wallets = sdkApiClient.getWallets(1, 3);
        System.out.println(wallets.size());

        for (io.minka.api.model.WalletResponse curr: wallets) {
            System.out.println(curr);

        }
    }

    @Test
    public void shouldNotRetrieveWallet() {
        String nonexistingHandle = "usxshvwjoptfixfdakh";

        io.minka.api.model.GetWalletResponse wallet = null;
        try {
            wallet = sdkApiClient.getWallet(nonexistingHandle);
        } catch (ExceptionResponseTinApi exceptionResponseTinApi) {
//            exceptionResponseTinApi.printStackTrace();
        }
//        assertNotEquals(wallet.getError().getCode().intValue(), TestingConstants.SUCCESS_ERROR_CODE);
//        System.out.println(wallet);

    }

    @Test
    public void shouldGetWalletBalance(){
//        existingHandle = "$usxshvwjoptfixfdakh";

        String bankName = "$offlinedemoath";
        String currency = "$tin";
        io.minka.api.model.BalanceResponse balance;
        balance = sdkApiClient.getBalance(bankName, currency);
        System.out.println(balance);
    }

    @Test
    public void shouldUpdateWallet() throws ExceptionResponseTinApi {
        String handle = "$offlinedemoath";
        String defaultAddress = "weyFqsN2ogzFadsXDzEwXNNWDiDLy5yUjv";
        List<String> signers = new ArrayList<>();
        signers.add(defaultAddress);


        WalletUpdateRequest req = new WalletUpdateRequest();
        Map<String, Object> labels = new HashMap<>();
        labels.put("routerDownload", "testingupdate");
        //req.setLabels(labels);
        req.setSigner(signers);
        req.setDefault(defaultAddress);
        io.minka.api.model.WalletUpdateResponse walletUpdateResponse
                = sdkApiClient.updateWallet(handle, req);
        System.out.println(walletUpdateResponse);
        assertEquals(walletUpdateResponse.getError().getCode().intValue(), TestingConstants.SUCCESS_ERROR_CODE);
    }

    @Test(expected = ExceptionResponseTinApi.class)
    public void shouldNotUpdateWallet() throws ExceptionResponseTinApi {
        String handle = "$1serphonenumber1";
        String defaultAddress = "!wXK6boH2pLf8raZwpSQH3JRgzW7qH7WyVf";
        List<String> signers = new ArrayList<>();
        signers.add(defaultAddress);
        WalletUpdateRequest req = new WalletUpdateRequest();
        io.minka.api.model.WalletUpdateResponse walletUpdateResponse = sdkApiClient.updateWallet(handle, req);
    }
}
