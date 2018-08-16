package com.minka.wallet.primitives.utils;

import com.google.gson.Gson;
import com.minka.ExceptionResponseTinApi;
import com.minka.api.handler.*;
import com.minka.api.model.*;
import com.minka.utils.Constants;

import java.util.List;
import java.util.Map;

/***
 * Cliente para la integración con el servicio WEB de TINAPI de MINKA
 */
public class SdkApiClient {

    private String url;
    private String apiKey;
    private String secret;
    private String clientId;
    private int timeout;

    /**
     *
     * @param domain
     * @param apiKey
     */
    public SdkApiClient(String domain, String apiKey) {
        this.url = "https://" + domain + ".minka.io/v1";
        this.apiKey = apiKey;
    }

    /**
     *
     * @param secret
     * @return
     */
    public SdkApiClient setSecret(String secret){
        this.secret = secret;
        return this;
    }

    /**
     *
     * @param clientId
     * @return
     */
    public SdkApiClient setClientId(String clientId){
        this.clientId = clientId;
        return this;
    }

    public SdkApiClient setTimeout(int timeout){
        this.timeout = timeout;
        return this;
    }

    /**
     * Solicita una pareja de llave privada y pública al Web service de TINAPI
     * @return un objeto con las llaves (privada y pública)
     */
    public Keeper getKeeper() throws ExceptionResponseTinApi {
        KeeperApi api = new KeeperApi();
        api.getApiClient().setBasePath(url);

        if (timeout > 0){
            api.getApiClient().setConnectTimeout(timeout);
        }

        try {
            return api.obtenerKeeper(apiKey);
        } catch (ApiException e) {

            String responseBody = e.getResponseBody();

            if (e.getCode() == Constants.FORBIDDEN){
                ErrorForbidden errorForbidden = new Gson().fromJson(responseBody, ErrorForbidden.class);
                throw new ExceptionResponseTinApi(e.getCode(), errorForbidden.getError());
            } else{
                throw new ExceptionResponseTinApi(e.getCode(), "Error inesperado");
            }
        }
    }

    public WalletResponse createWallet(String handle, Map<String, Object> labelsWallet) throws WalletCreationException {
        WalletApi api = new WalletApi();

        api.getApiClient().setBasePath(url);

        if (timeout > 0){
            api.getApiClient().setConnectTimeout(timeout);
        }

        WalletRequest walletRe = new WalletRequest();
        walletRe.setHandle(handle);
        walletRe.setLabels(labelsWallet);
        try {
            return api.createWallet(apiKey, walletRe);
        } catch (ApiException e) {

            if (e.getCode() == Constants.BAD_REQUEST){
                ErrorResponse errorGenerico = new Gson().fromJson(e.getResponseBody(), ErrorResponse.class);
                throw new WalletCreationException(errorGenerico.getError().getCode(), errorGenerico.getError().getMessage());
            } else{
                throw new WalletCreationException(Constants.UNEXPECTED_ERROR,Constants.UNEXPECTED_ERROR_MESSAGE);
            }
        }
    }


    public GetWalletResponse getWallet(String handle) throws ExceptionResponseTinApi {
        WalletTempoApi api = new WalletTempoApi();
        api.getApiClient().setBasePath(url);
        if (timeout > 0){
            api.getApiClient().setConnectTimeout(timeout);
        }

        try {
            return api.getWallet(apiKey, handle);
        } catch (ApiException e) {
            throw new ExceptionResponseTinApi(e.getCode(), e.getMessage());
        }
    }


    public SignerResponse createSigner(Map<String, Object> labels) throws ApiException {
        SignerRequest signerRequest = new SignerRequest();
        signerRequest.setLabels(labels);
        SignerApi signerApi = new SignerApi();
        signerApi.getApiClient().setBasePath(url);
        return signerApi.createSigner(signerRequest, apiKey);
    }


    public WalletUpdateResponse updateWallet(String handle, List<String> signers , String defaultAddress) throws ExceptionResponseTinApi {
        WalletApi walletApi = new WalletApi();
        walletApi.getApiClient().setBasePath(url);

        WalletUpdateRequest walletUpdateRequest = createUpdateWalletReq(signers, defaultAddress);

        try {
            return walletApi.updateWallet(apiKey, handle, walletUpdateRequest);
        } catch (ApiException e) {
            String responseBody = e.getResponseBody();
            if (e.getCode() == Constants.BAD_REQUEST){
                WalletUpdateResponse response= new Gson().fromJson(responseBody, WalletUpdateResponse.class);
                throw new ExceptionResponseTinApi(response.getError().getCode(),response.getError().getMessage());
            }else{
                throw new ExceptionResponseTinApi(Constants.UNEXPECTED_ERROR ,Constants.UNEXPECTED_ERROR_MESSAGE);
            }
        }
    }

    private WalletUpdateRequest createUpdateWalletReq(List<String> signers , String defaultAddress) {
        WalletUpdateRequest updateWalletReq = new WalletUpdateRequest();
        updateWalletReq.setDefault(defaultAddress);
        updateWalletReq.setSigner(signers);
        return updateWalletReq;
    }

    public BalanceResponse getBalance(String bankName, String currency)  {
        WalletApi walletApi = new WalletApi();
        walletApi.getApiClient().setBasePath(url);

        BalanceResponse balance = null;
        try {
            balance = walletApi.getBalance(apiKey, bankName, currency);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return balance;
    }



    public CreateActionResponse createAction(CreateActionRequest actionReq ) throws ApiException {
        ActionApi actionApi = new ActionApi();
        actionApi.getApiClient().setBasePath(url);
        return actionApi.createAction(apiKey, actionReq);
    }

    public GenericResponse signAction( String actionId) throws ApiException {
        ActionApi actionApi = new ActionApi();
        actionApi.getApiClient().setBasePath(url);
        return actionApi.signAction(apiKey, actionId);
    }

    public GenericResponse getAction(String hashValue) throws ApiException {
        ActionApi actionApi = new ActionApi();
        actionApi.getApiClient().setBasePath(url);
        return actionApi.getAction(apiKey, hashValue);
    }

//    private SignerRequest createSignerRequest(Keeper keeper, Map<String, Object> labelsSigner) {
//        SignerRequest result = new SignerRequest();
//        result.setLabels(labelsSigner);
//        List<PublicKeys> keepers = new ArrayList<>();
//        PublicKeys llave = convert(keeper);
//        keepers.add(llave);
//        result.setKeeper(keepers);
//        return result;
//    }
//
//    private PublicKeys convert(Keeper keeper) {
//        PublicKeys result = new PublicKeys();
//        result.setPublic(keeper.getPublic());
//        result.setSecret(keeper.getSecret());
//        return result;
//    }
//
//    private WalletRequest createRequestWallet(String handle, Map<String, Object> labelsWallet) {
//        WalletRequest result = new WalletRequest();
//        result.setHandle(handle);
//        result.setLabels(labelsWallet);
//        return result;
//    }
//
//    private WalletResponse createWallet(WalletRequest handle, WalletApi walletApi) throws ApiException {
//        return walletApi.createWallet(API_KEY, handle);
//    }
//
//
//    public WalletUpdateResponse updateWallet(String handle, WalletUpdateRequest walletUpdateRequest) throws ApiException {
//        WalletApi walletApi = new WalletApi();
//        walletApi.getApiClient().setBasePath(CLOUD_URL);
//
//        return walletApi.updateWallet(API_KEY, handle, walletUpdateRequest);
//    }
//
//    public CreateClaimResponse createClaim(CreateClaimRequest req) throws ApiException {
//        KlemerApi klemerApi = new KlemerApi();
//
//        klemerApi.getApiClient().setBasePath(CLOUD_URL);
//        return klemerApi.createClaim(API_KEY, req);
//    }
//
//    public static TransactionRequest createTransfer(Object iou, Map<String, Object> labelsIou) throws ApiException {
//        TransactionApi api = new TransactionApi();
//
//        api.getApiClient().setBasePath(CLOUD_URL);
//        TransactionRequest transactionReq = new TransactionRequest();
//        transactionReq.setLabels(labelsIou);
//        transactionReq.setIou(iou);
//        System.out.println("transactionReq");
//        System.out.println(transactionReq);
//        TransactionRequest transfer = api.createTransfer(API_KEY, transactionReq);
//        return transfer;
//    }
}
