package com.minka.wallet.primitives.utils;

import com.minka.api.handler.*;
import com.minka.api.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SdkApiClient {
    static String CLOUD_URL = "https://achtin-tst.minka.io/v1";
    static String API_KEY = "5b481fc2ae177010e197026b9778ac575c36480a918674e7a76d8037";

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
        System.out.println("signerRequest");
        System.out.println(signerRequest);

        SignerResponse signer = signerApi.createSigner(signerRequest, API_KEY);

        System.out.println("SignerResponse");
        System.out.println(signer);

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
        result.setSecret(keeper.getSecret());
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


    public WalletUpdateResponse updateWallet(String handle, WalletUpdateRequest walletUpdateRequest) throws ApiException {
        WalletApi walletApi = new WalletApi();
        walletApi.getApiClient().setBasePath(CLOUD_URL);

        return walletApi.updateWallet(API_KEY, handle, walletUpdateRequest);
    }

    public CreateClaimResponse createClaim(CreateClaimRequest req) throws ApiException {
        KlemerApi klemerApi = new KlemerApi();

        klemerApi.getApiClient().setBasePath(CLOUD_URL);
        return klemerApi.createClaim(API_KEY, req);
    }

    public static TransactionRequest createTransfer(Object iou, Map<String, Object> labelsIou) throws ApiException {
        TransactionApi api = new TransactionApi();

        api.getApiClient().setBasePath(CLOUD_URL);
        TransactionRequest transactionReq = new TransactionRequest();
        transactionReq.setLabels(labelsIou);
        transactionReq.setIou(iou);
        System.out.println("transactionReq");
        System.out.println(transactionReq);
        TransactionRequest transfer = api.createTransfer(API_KEY, transactionReq);
        return transfer;
    }
}
