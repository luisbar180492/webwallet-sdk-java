package com.minka.sdk;

import com.minka.ExceptionResponseTinApi;
import com.minka.api.handler.ApiException;
import com.minka.api.model.*;
import com.minka.wallet.primitives.utils.SdkApiClient;
import com.minka.wallet.primitives.utils.WalletCreationException;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    }

    @Test
    public void createWalletProperly() {
        String handle = "$" + RandomStringUtils.randomAlphabetic(19).toLowerCase();
        System.out.println("handle");
        System.out.println(handle);

        Map<String, Object> walletLabels = new HashMap<>();
        walletLabels.put("email", "aranibarIvan@mgali.com");
        walletLabels.put("phoneNumber", new Integer(21721821));

        try {
            WalletResponse wallet = sdkApiClient.createWallet(handle, walletLabels);
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

        Map<String, Object> walletLabels = new HashMap<>();

        WalletResponse wallet = sdkApiClient.createWallet(handle, walletLabels);

    }

    @Test
    public void shouldRetrieveCreatedWallet() {
        existingHandle = "$3104845181";

        assertNotEquals(existingHandle, null);
        try {
            GetWalletResponse wallet = sdkApiClient.getWallet(existingHandle);
            System.out.println(wallet);
            assertEquals(wallet.getError().getCode().intValue(), TestingConstants.SUCCESS_ERROR_CODE);

        } catch (ExceptionResponseTinApi exceptionResponseTinApi) {
            exceptionResponseTinApi.printStackTrace();
        }

    }

    @Test
    public void sendSms(){

        ErrorResponse response = sdkApiClient.sendSms("$3104845181", "SMS de prueba desde junit!");
        System.out.println("errorGenerico");
        System.out.println(response);
        assertEquals(TestingConstants.SUCCESS_ERROR_CODE, response.getError().getCode().intValue());
    }

    @Test
    public void shouldNotRetrieveWallet() {
        String nonexistingHandle = "$!usxshvwjoptfixfdakh";

        assertNotEquals(nonexistingHandle, null);
        GetWalletResponse wallet = null;
        try {
            wallet = sdkApiClient.getWallet(nonexistingHandle);
        } catch (ExceptionResponseTinApi exceptionResponseTinApi) {
            exceptionResponseTinApi.printStackTrace();
        }
        assertNotEquals(wallet.getError().getCode().intValue(), TestingConstants.SUCCESS_ERROR_CODE);
        System.out.println(wallet);

    }

    @Test
    public void shouldGetWalletBalance(){
        existingHandle = "$usxshvwjoptfixfdakh";

        String bankName = "$yourbankname1221";
        String currency = "$tin";
        BalanceResponse balance = sdkApiClient.getBalance(bankName, currency);
        System.out.println(balance);
    }

    @Test
    public void shouldUpdateWallet() throws ExceptionResponseTinApi {
        String handle = "$userphonenumber1";
        String defaultAddress = "wXK6boH2pLf8raZwpSQH3JRgzW7qH7WyVf";
        List<String> signers = new ArrayList<>();
        signers.add(defaultAddress);

        WalletUpdateResponse walletUpdateResponse = sdkApiClient.updateWallet(handle, signers, defaultAddress);
        assertEquals(walletUpdateResponse.getError().getCode().intValue(), TestingConstants.SUCCESS_ERROR_CODE);
    }

    @Test(expected = ExceptionResponseTinApi.class)
    public void shouldNotUpdateWallet() throws ExceptionResponseTinApi {
        String handle = "$1serphonenumber1";
        String defaultAddress = "!wXK6boH2pLf8raZwpSQH3JRgzW7qH7WyVf";
        List<String> signers = new ArrayList<>();
        signers.add(defaultAddress);

            WalletUpdateResponse walletUpdateResponse = sdkApiClient.updateWallet(handle, signers, defaultAddress);
    }
}