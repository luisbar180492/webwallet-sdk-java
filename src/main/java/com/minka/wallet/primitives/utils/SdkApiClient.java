package com.minka.wallet.primitives.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.minka.ExceptionResponseTinApi;
import com.minka.KeyPairHolder;
import com.minka.utils.ActionType;
import com.minka.utils.AliasType;
import com.minka.utils.Constants;
import io.minka.api.handler.*;
import io.minka.api.model.*;
import io.minka.api.model.Keeper;
import io.minka.api.model.SignerListResponse;
import io.minka.api.model.WalletRequest;
import io.minka.api.model.WalletUpdateResponse;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.*;

/***
 * Cliente para la integración con el servicio WEB de TINAPI de MINKA
 */
public class SdkApiClient {

    private final ApiClient apiClient;
    private final String domain;
    private String url;
    private String apiKey;
    private String secret;
    private String clientId;
    private int timeout;
    private boolean oauth2;
    private Proxy proxy;

    /**
     *
     * @param domain
     * @param apiKey
     */
    public SdkApiClient(String domain, String apiKey) {
        this.domain = domain;
        this.url = "https://" + domain + ".minka.io/v1";
        this.apiKey = apiKey;
        apiClient = new ApiClient();
        apiClient.setApiKey(apiKey);
        apiClient.setBasePath(url);

        if (timeout > 0){
            apiClient.setConnectTimeout(timeout);
        }
    }

    public SdkApiClient setProxy(String host, int port){
        SocketAddress socketAddress = new InetSocketAddress(host, port);
        this.proxy = new Proxy(Proxy.Type.HTTP, socketAddress);
        this.apiClient.getHttpClient().setProxy(proxy);
        return this;
    }

    private void refreshToken() {
        if (oauth2){
            TokenResponse token = null;
            try {
                token = getToken();
                io.minka.api.handler.auth.OAuth oAuth2ClientCredentials;
                oAuth2ClientCredentials = (io.minka.api.handler.auth.OAuth)
                        apiClient.getAuthentication("oAuth2ClientCredentials");
                oAuth2ClientCredentials.setAccessToken(token.getAccessToken());
            } catch (io.minka.api.handler.ApiException e) {
                e.printStackTrace();
            }
        }
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
     * Returna una llave generado offline localmente para firmar IOUs.
     * @return
     */
    public io.minka.api.model.Keeper getKeeperForOfflineSigning(){

        Keeper keeperGenerated;
        keeperGenerated = new Keeper();
        KeyPairHolder sourcekeyPairHolder = new KeyPairHolder();
        keeperGenerated.setPublic(sourcekeyPairHolder.getPublicKey());
        keeperGenerated.setSecret(sourcekeyPairHolder.getSecretSeed());
        keeperGenerated.setScheme(sourcekeyPairHolder.getScheme());
        return keeperGenerated;
    }

    /**
     * Solicita una pareja de llave privada y pública al Web service de TINAPI o
     * la genera localmente para el flag offline.
     * @return un objeto con las llaves (privada y pública)
     */
    public io.minka.api.model.Keeper getKeeper() throws ExceptionResponseTinApi {
        refreshToken();

        io.minka.api.handler.KeeperApi api = new io.minka.api.handler.KeeperApi(apiClient);

        try {
            Keeper keeperGenerated;
            keeperGenerated = api.obtenerKeeper();
            return keeperGenerated;
        } catch (io.minka.api.handler.ApiException e) {

            String responseBody = e.getResponseBody();

            if (e.getCode() == Constants.FORBIDDEN){
                ErrorForbidden errorForbidden = new Gson().fromJson(responseBody, ErrorForbidden.class);
                throw new ExceptionResponseTinApi(e.getCode(), errorForbidden.getError());
            } else {
                e.printStackTrace();
                throw new ExceptionResponseTinApi(e.getCode(), "Error inesperado");
            }
        }
    }

    public ListLinks getLinks(String source, String target, String type) throws ExceptionResponseTinApi {
        refreshToken();
        io.minka.api.handler.LinksApi api = new io.minka.api.handler.LinksApi(apiClient);
        try {
            Object response = api.getLink(source,target,type);
            Gson gson = (new GsonBuilder()).create();
            return new Gson().fromJson(gson.toJson(response), ListLinks.class);
        } catch (io.minka.api.handler.ApiException e) {
            throw new ExceptionResponseTinApi(e.getCode(), e.getMessage());
        }
    }

    public LinkItem getLink(String source, String target) throws ExceptionResponseTinApi {
        refreshToken();
        io.minka.api.handler.LinksApi api = new io.minka.api.handler.LinksApi(apiClient);
        try {
            Object response = api.getLink(source,target, null);
            Gson gson = (new GsonBuilder()).create();
            return new Gson().fromJson(gson.toJson(response), LinkItem.class);
        } catch (io.minka.api.handler.ApiException e) {
            throw new ExceptionResponseTinApi(e.getCode(), e.getMessage());
        }
    }
    public LinkItem createLink(String source, String target, io.minka.api.model.CreateLinkRequest.TypeEnum typeLink) throws ExceptionResponseTinApi {
        refreshToken();
        io.minka.api.handler.LinksApi api = new io.minka.api.handler.LinksApi(apiClient);

        io.minka.api.model.CreateLinkRequest req = new io.minka.api.model.CreateLinkRequest();
        req.setSource(source);
        req.setTarget(target);
        req.setType(typeLink);
        try {
            return api.createLink(req);
        } catch (io.minka.api.handler.ApiException e) {
            throw new ExceptionResponseTinApi(e.getCode(), e.getMessage());

        }
    }


    public io.minka.api.model.WalletResponse createWallet(WalletRequest walletRequest)
            throws WalletCreationException {
        refreshToken();

        io.minka.api.handler.WalletApi walletApi;
        walletApi = new io.minka.api.handler.WalletApi(apiClient);
        try {
            return walletApi.createWallet(walletRequest);
        } catch (io.minka.api.handler.ApiException e) {
                throw new WalletCreationException(Constants.UNEXPECTED_ERROR,Constants.UNEXPECTED_ERROR_MESSAGE);
        }
    }

    public io.minka.api.model.GetWalletResponse getWallet(String handle)
            throws ExceptionResponseTinApi {
        refreshToken();
        io.minka.api.handler.WalletApi api = new io.minka.api.handler.WalletApi(apiClient);
        try {
            return api.getWalletByAlias( handle);
        } catch (io.minka.api.handler.ApiException e) {
            throw new ExceptionResponseTinApi(e.getCode(), e.getMessage());
        }
    }


    public io.minka.api.model.SignerResponse createSigner(SignerRequestLabels labels) throws io.minka.api.handler.ApiException {
        refreshToken();
        io.minka.api.model.SignerRequest signerRequest = new io.minka.api.model.SignerRequest();
        signerRequest.setLabels(labels);
        io.minka.api.handler.SignerApi signerApi = new io.minka.api.handler.SignerApi(apiClient);
        return signerApi.createSigner(signerRequest);
    }

    public io.minka.api.model.SignerResponse createSignerOfflineSigning(SignerRequestLabels labels, io.minka.api.model.PublicKeys publicKey) throws io.minka.api.handler.ApiException {
        refreshToken();
        io.minka.api.handler.SignerApi api = new io.minka.api.handler.SignerApi(apiClient);

        io.minka.api.model.SignerRequest signerRequest = new io.minka.api.model.SignerRequest();
        signerRequest.setLabels( labels);
        signerRequest.addKeeperItem(publicKey);

        return api.createSigner(signerRequest);
    }


    public io.minka.api.model.WalletUpdateResponse updateWallet(String handle, io.minka.api.model.WalletUpdateRequest createUpdateWalletReq) throws ExceptionResponseTinApi {
        refreshToken();
        io.minka.api.handler.WalletApi api = new io.minka.api.handler.WalletApi(apiClient);

        try {
            return api.updateWallet(handle,  createUpdateWalletReq);
        } catch (io.minka.api.handler.ApiException e) {
            String responseBody = e.getResponseBody();
            if (e.getCode() == Constants.BAD_REQUEST){
                WalletUpdateResponse response= new Gson().fromJson(responseBody, WalletUpdateResponse.class);
                throw new ExceptionResponseTinApi(response.getError().getCode(),response.getError().getMessage());
            }else{
                System.out.println( e.getCode() );
                System.out.println( e.getResponseBody() );
                throw new ExceptionResponseTinApi(Constants.UNEXPECTED_ERROR ,Constants.UNEXPECTED_ERROR_MESSAGE);
            }
        }
    }

     public io.minka.api.model.BalanceResponse getBalance(String bankName, String currency)  {
        refreshToken();
        io.minka.api.handler.WalletApi walletApi;
        walletApi = new io.minka.api.handler.WalletApi(apiClient);

        io.minka.api.model.BalanceResponse balance = null;
        try {
            balance = walletApi.getBalance( bankName, currency);
        } catch (io.minka.api.handler.ApiException e) {
            e.printStackTrace();
        }
        return balance;
    }

    public io.minka.api.model.CreateActionResponse createAction(io.minka.api.model.CreateActionRequest actionReq )
            throws io.minka.api.handler.ApiException {
        refreshToken();
        io.minka.api.handler.ActionApi actionApi = new io.minka.api.handler.ActionApi(apiClient);
        return actionApi.createAction(actionReq);
    }

    public CreateActionResponse signActionOffline(String actionId, OfflineSigningKeys keys) throws io.minka.api.handler.ApiException {

        refreshToken();
        io.minka.api.handler.ActionApi api = new io.minka.api.handler.ActionApi(apiClient);

        GetActionResponse actionByActionId = api.getActionByActionId(actionId);

        IouSigned iouSigned  = new IouSigned();//TODO
        return api.signOffline(actionId, iouSigned);
    }


    public CreateTransferResponse signAction(String actionId) throws ApiException {
        refreshToken();
        ActionApi actionApi = new ActionApi(apiClient);
        ActionSignedLabels actionLabels = new ActionSignedLabels(); //TODO
        return actionApi.signAction(actionId, actionLabels);
    }

    public GetActionResponse getAction(String hashValue) throws ApiException {
        refreshToken();
        ActionApi actionApi = new ActionApi(apiClient);
        return actionApi.getActionByActionId(hashValue);
    }


public CreateTransferResponse acceptTransfer(AcceptTransferRequest  req,
                                             String actionRequestId) throws io.minka.api.handler.ApiException {
    refreshToken();
    TransferApi transferApi = new TransferApi(apiClient);
    return transferApi.acceptP2Ptranfer(actionRequestId, req);
}


public CreateTransferResponse rejectTransfer(RejectTransferRequest req , String actionId) throws ExceptionResponseTinApi, io.minka.api.handler.ApiException {
    refreshToken();
    TransferApi transferApi = new TransferApi(apiClient);

    return transferApi.rejectP2Ptranfer(actionId, req);
}


    public List<GetActionResponse> getActionPendings(String alias, AliasType aliasType, ActionType action) {
        refreshToken();
        io.minka.api.handler.ActionApi tempoapi= new io.minka.api.handler.ActionApi(apiClient);

        try {
            if (aliasType.getValue().equals(AliasType.SOURCE.getValue())){

                return tempoapi.getPending( action.getValue(), null, alias);
            }else {

                return tempoapi.getPending(action.getValue(), alias, null);
            }
        } catch (io.minka.api.handler.ApiException e) {
            e.printStackTrace();
        }
        return null;
    }


    public CreateTransferResponse createTinTransfer(CreateTransferRequest tintransfer )
            throws io.minka.api.handler.ApiException {

        refreshToken();
        io.minka.api.handler.TransferApi api = new TransferApi(apiClient);

        System.out.println("api.getApiClient().getBasePath()");
        System.out.println(api.getApiClient().getBasePath());


        return api.createTinTransfer(tintransfer);
    }
    public WalletListResponse getWallets(int pagenum, int pagesize) throws io.minka.api.handler.ApiException {
        refreshToken();
        io.minka.api.handler.WalletApi api = new io.minka.api.handler.WalletApi(apiClient);
        WalletListResponse temporal = api.getWallets(pagenum, pagesize);

        return temporal;
    }

    public SignerListResponse getSigners(int pagenum, int pagesize) throws io.minka.api.handler.ApiException {
        refreshToken();
        io.minka.api.handler.SignerApi api = new io.minka.api.handler.SignerApi(apiClient);
        return api.getSigners(pagenum, pagesize);
    }

    public io.minka.api.model.SignerResponse deleteSigner(String signerAddress) throws io.minka.api.handler.ApiException {
        io.minka.api.handler.SignerApi api = new io.minka.api.handler.SignerApi(apiClient);
        return api.deleteSignerByAddress(signerAddress);
    }




    public io.minka.api.model.GenericResponse updateSigner(String signerAddress, io.minka.api.model.SignerRequest updateSignerReq) throws io.minka.api.handler.ApiException {
        io.minka.api.handler.SignerApi api = new io.minka.api.handler.SignerApi(apiClient);
        return api.updateSigner(signerAddress, updateSignerReq);
    }

    public io.minka.api.model.SignerResponse getSignerByAddress(String wAddress) throws io.minka.api.handler.ApiException {
        io.minka.api.handler.SignerApi api = new io.minka.api.handler.SignerApi(apiClient);
        return api.getSignerByAddress(wAddress);
    }
    public io.minka.api.model.GenericResponse updateAction(String actionid, UpdateActionRequest req) throws io.minka.api.handler.ApiException {
        io.minka.api.handler.ActionApi api = new io.minka.api.handler.ActionApi(apiClient);


        return api.updateActionLabels(actionid, req);
    }
    public io.minka.api.model.GenericResponse deleteAction(String actionid) throws io.minka.api.handler.ApiException {
        io.minka.api.handler.ActionApi api = new io.minka.api.handler.ActionApi(apiClient);

        return null;
    }



    public io.minka.api.model.GetWalletResponse deleteWalletByAlias(String aliasHandle) throws io.minka.api.handler.ApiException {
        io.minka.api.handler.WalletApi api = new io.minka.api.handler.WalletApi(apiClient);

        return null;
    }

    public io.minka.api.model.GetWalletResponse getWalletByAlias(String aliasHandle)
            throws io.minka.api.handler.ApiException {
        refreshToken();
        io.minka.api.handler.WalletApi api;
        api = new io.minka.api.handler.WalletApi(apiClient);

        return api.getWalletByAlias(aliasHandle);
    }

    public void updateSigner() {
        io.minka.api.handler.SignerApi api = new io.minka.api.handler.SignerApi(apiClient);

    }

    public GetTransfersResponse getTransfer(String type, String  target, String  source) throws io.minka.api.handler.ApiException {
        io.minka.api.handler.ActionApi api = new io.minka.api.handler.ActionApi(apiClient);
        return api.getTransfer(type, target, source,null);
    }

    public GetTransfersResponse getActions() throws io.minka.api.handler.ApiException {
        refreshToken();
        io.minka.api.handler.ActionApi api = new io.minka.api.handler.ActionApi(apiClient);
        return api.getTransfer(null, null, null,null);
    }


    public TokenResponse getToken() throws io.minka.api.handler.ApiException {
        ApiClient apiClientToken = new ApiClient();

        if (proxy != null){
            apiClientToken.getHttpClient().setProxy(proxy);
        }
        apiClientToken.setUsername(clientId);
        apiClientToken.setPassword(secret);
        apiClientToken.setBasePath("https://" + domain + ".minka.io");
        TokenApi api = new TokenApi(apiClientToken);
        return api.getToken("client_credentials", clientId, secret);
    }

    public void setOauth2On() {
        oauth2 = true;
    }

    public void setOauth2Off() {
        oauth2 = false;
    }
}
