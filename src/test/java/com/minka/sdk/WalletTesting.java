package com.minka.sdk;

import com.minka.Constants;
import com.minka.api.handler.ApiException;
import com.minka.api.model.GetWalletResponse;
import com.minka.api.model.WalletResponse;
import com.minka.wallet.primitives.utils.SdkApiClient;
import com.minka.wallet.primitives.utils.WalletCreationException;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.HashMap;
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
        existingHandle = "$usxshvwjoptfixfdakh";

        assertNotEquals(existingHandle, null);
        try {
            GetWalletResponse wallet = sdkApiClient.getWallet(existingHandle);
            System.out.println(wallet);
            assertEquals(wallet.getError().getCode().intValue(), TestingConstants.SUCCESS_ERROR_CODE);

        } catch (ApiException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void shouldNotRetrieveWallet() {
        String nonexistingHandle = "$!usxshvwjoptfixfdakh";

        assertNotEquals(nonexistingHandle, null);
        try {
            GetWalletResponse wallet = sdkApiClient.getWallet(nonexistingHandle);
            System.out.println(wallet);

        } catch (ApiException e) {
            e.printStackTrace();
            System.out.println(e.getResponseBody());
        }

    }

}
