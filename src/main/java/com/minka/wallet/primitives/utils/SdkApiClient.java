package com.minka.wallet.primitives.utils;

import com.minka.api.handler.ApiException;
import com.minka.api.handler.KeeperApi;
import com.minka.api.handler.SignerApi;
import com.minka.api.handler.WalletApi;
import com.minka.api.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SdkApiClient {
    static String CLOUD_URL = "https://achtin-tst.minka.io/v1";
    static String API_KEY = "5b481fc2ae177010e197026b39c58cdb000f4c3897e841714e82c84c";

    public Keeper getKeeper() throws ApiException {
        KeeperApi keeperApi = new KeeperApi();
        keeperApi.getApiClient().setBasePath(CLOUD_URL);
        return keeperApi.obtenerKeeper(API_KEY);
    }

    public WalletUpdateResponse getWallet(Keeper keeper, String handle, Map<String, Object> labelsSigner, Map<String, Object> labelsWallet) throws ApiException {
        WalletApi walletApi = new WalletApi();
        walletApi.getApiClient().setBasePath(CLOUD_URL);

        WalletRequest walletCreationRequest = createRequestWallet(handle, labelsWallet);
        WalletResponse wallet = createWallet(walletCreationRequest, walletApi);

        SignerRequest signerRequest = createSignerRequest(keeper, labelsSigner);
        SignerApi signerApi = new SignerApi();
//        System.out.println("signerRequest");
//        System.out.println(signerRequest);

        SignerResponse signer = signerApi.createSigner(signerRequest, API_KEY);

        WalletUpdateRequest walletUpdateRequest = createUpdateWalletReq(signer.getHandle());

        WalletUpdateResponse walletUpdateResponse = walletApi.updateWallet(API_KEY, handle, walletUpdateRequest);
        return walletUpdateResponse;
    }

    private WalletUpdateRequest createUpdateWalletReq(String aPublic) {
        WalletUpdateRequest updateWalletReq = new WalletUpdateRequest();
        updateWalletReq.setDefault(aPublic);
        List<String> listaSigners = new ArrayList<>();
        listaSigners.add(aPublic);
        updateWalletReq.setSigner(listaSigners);
        return updateWalletReq;
    }

    private SignerRequest createSignerRequest(Keeper keeper, Map<String, Object> labelsSigner) {
        SignerRequest result = new SignerRequest();
        result.setLabels(labelsSigner);
        List<PublicKeys> keepers = new ArrayList<>();
        PublicKeys llave = convert(keeper);
        keepers.add(llave);
        result.setKeeper(keepers);
        return result;
    }

    private PublicKeys convert(Keeper keeper) {
        PublicKeys result = new PublicKeys();
        result.setPublic(keeper.getPublic());
        //TODO
        return result;
    }

    private WalletRequest createRequestWallet(String handle, Map<String, Object> labelsWallet) {
        WalletRequest result = new WalletRequest();
        result.setHandle(handle);
        result.setLabels(labelsWallet);
        return result;
    }

    private WalletResponse createWallet(WalletRequest handle, WalletApi walletApi) throws ApiException {
        return walletApi.createWallet(API_KEY, handle);
    }


}
