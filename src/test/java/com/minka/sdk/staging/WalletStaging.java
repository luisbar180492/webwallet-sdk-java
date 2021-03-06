package com.minka.sdk.staging;

import com.minka.ExceptionResponseTinApi;
import com.minka.sdk.TestingConstants;
import com.minka.wallet.primitives.utils.SdkApiClient;
import com.minka.wallet.primitives.utils.WalletCreationException;
import io.minka.api.handler.ApiException;
import io.minka.api.model.WalletListResponse;
import io.minka.api.model.WalletRequest;
import io.minka.api.model.WalletRequestLabels;
import io.minka.api.model.WalletUpdateRequest;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class WalletStaging {

    private static final int LENGTH_HANDLE = 10;
    SdkApiClient sdkApiClient;
    String existingHandle;

    @Before
    public void prepare(){
        sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN_STAGING,
                TestingConstants.API_KEY, TestingConstants.STAGING_BASE);

        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);

        if (TestingConstants.proxy){
            sdkApiClient.setProxy(TestingConstants.PROXY_HOST,
                    TestingConstants.PROXY_PORT);
        }

    }

    @Test
    public void createWalletProperly() {
//        String handle = "$" + RandomStringUtils.randomAlphabetic(19).toLowerCase();
        String handle = "$bancoheroku";
        String urlBase = "https://b450c068.ngrok.io";

        try {
            WalletRequest walletReq  = new WalletRequest();
            walletReq.setHandle(handle);
            WalletRequestLabels labelsReq = new WalletRequestLabels();
            labelsReq.setType("TROUPE");
            labelsReq.setRouterAction(urlBase + "/v1/action");
            labelsReq.setRouterDownload(urlBase + "/v1/credit");
            labelsReq.setRouterStatus(urlBase + "/v1/status");
            labelsReq.setRouterUpload(urlBase + "/v1/debit");
            walletReq.setLabels(labelsReq);
            io.minka.api.model.WalletResponse wallet = sdkApiClient.createWallet(walletReq);
            System.out.println(wallet);

            assertEquals(TestingConstants.SUCCESS_ERROR_CODE, wallet.getError().getCode().intValue());
            existingHandle = handle;

        } catch (WalletCreationException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void shouldRetrieveCreatedWallet() {
        existingHandle = "$plthbkikvtdznvndhqm";

        assertNotEquals(existingHandle, null);
        try {
            io.minka.api.model.GetWalletResponse wallet;
            wallet = sdkApiClient.getWallet(existingHandle);
            System.out.println(wallet);
            assertEquals(wallet.getError().getCode().intValue(), TestingConstants.SUCCESS_ERROR_CODE);

        } catch (ExceptionResponseTinApi exceptionResponseTinApi) {
            exceptionResponseTinApi.printStackTrace();
        }

    }

    @Test
    public void shouldGetWalletByAlias()
            throws io.minka.api.handler.ApiException {

        io.minka.api.model.GetWalletResponse wallets;
        wallets =
                sdkApiClient.getWalletByAlias("$qmblkkdzkwbgepdinyr");
        sdkApiClient.getWalletByAlias("$qmblkkdzkwbgepdinyr");
        sdkApiClient.getWalletByAlias("$qmblkkdzkwbgepdinyr");
        //System.out.println(wallets);

    }

    @Test
    public void shouldGetWallets()
            throws io.minka.api.handler.ApiException {
        WalletListResponse wallets =
                sdkApiClient.getWallets(1, 3);
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
            exceptionResponseTinApi.printStackTrace();
        }

    }

    @Ignore
    @Test
    public void shouldGetWalletBalance() throws ApiException {

        String bankName = "$qmblkkdzkwbgepdinyr";
        String currency = "$tin";
        io.minka.api.model.BalanceResponse balance;
        balance = sdkApiClient.getBalance(bankName, currency);
        System.out.println(balance);
    }

    @Ignore
    @Test
    public void shouldUpdateWallet() throws ExceptionResponseTinApi {
        String handle = "$cofptfyhpngqgedhrqc";
        String defaultAddress = "whYQRj4djgw32wuwwCGQfvrLhXyiNrjzUL";
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

   @Ignore
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
