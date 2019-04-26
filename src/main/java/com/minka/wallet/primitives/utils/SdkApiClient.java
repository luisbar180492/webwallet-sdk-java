package com.minka.wallet.primitives.utils;

import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import javax.net.ssl.SSLContextSpi;
import sun.security.jca.GetInstance;
import sun.security.jca.ProviderList;
import sun.security.jca.Providers;

import com.google.gson.Gson;
import com.minka.CustomApiClient;
import com.minka.ExceptionResponseTinApi;
import com.minka.IouUtil;
import com.minka.KeyPairHolder;
import com.minka.utils.ActionType;
import com.minka.utils.AliasType;
import com.minka.utils.Constants;
import io.minka.api.handler.*;
import io.minka.api.handler.auth.ApiKeyAuth;
import io.minka.api.model.*;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/***
 * Cliente para la integración con el servicio WEB de TINAPI de ACH TIN
 */
public class SdkApiClient {

    private Logger logger = Logger.getLogger(SdkApiClient.class.getName());

    private io.minka.api.handler.DefaultApi api;
    private final CustomApiClient apiClient;
    private final String domain;
    private String url;
    private String apiKey;
    private String secret;
    private String clientId;
    private int timeout;
    private Proxy proxy;

    public SdkApiClient(String domain, String apiKey, String urlBase) throws ApiException {
        try {
            forceTLS1dot2version();
        } catch (NoSuchAlgorithmException e) {
            throw new ApiException("TLS version 1.2 no pudo ser inicializada");
        }

        this.domain = domain;
        this.url = urlBase;
        this.apiKey = apiKey;
        apiClient = new CustomApiClient();

        apiClient.setApiKey(apiKey);
        apiClient.setBasePath(url);
        apiClient.setOauthUrl(this.url.substring(0, this.url.length() - 3));
        api = new io.minka.api.handler.DefaultApi(apiClient);

        if (timeout > 0) {
            apiClient.setConnectTimeout(timeout);
        }
    }

    private void forceTLS1dot2version() throws NoSuchAlgorithmException {
        ProviderList providerList = Providers.getProviderList();
        GetInstance.Instance instance = GetInstance.getInstance("SSLContext", SSLContextSpi.class, "TLS");
        for (Provider provider : providerList.providers())
        {
            if (provider == instance.provider)
            {
                provider.put("Alg.Alias.SSLContext.TLS", "TLSv1.2");
            }
        }
    }

    public SdkApiClient setProxy(String host, int port) {
        SocketAddress socketAddress = new InetSocketAddress(host, port);
        this.proxy = new Proxy(Proxy.Type.HTTP, socketAddress);
        this.apiClient.getHttpClient().setProxy(proxy);
        return this;
    }

    private void updateHeaders() {
        ApiKeyAuth xApiKey = (ApiKeyAuth) apiClient.getAuthentication("ApiKeyAuth");
        xApiKey.setApiKey(apiKey);

        ApiKeyAuth xNonce = (ApiKeyAuth) apiClient.getAuthentication("XNonce");
        xNonce.setApiKey(generateUUID());

        ApiKeyAuth apiKeyTin = (ApiKeyAuth) apiClient.getAuthentication("ApiKeyAuth");
        apiKeyTin.setApiKey(apiKey);
    }

    /**
     * @param secret
     * @return
     */
    public SdkApiClient setSecret(String secret) {
        this.secret = secret;
        apiClient.setSecret(secret);
        return this;
    }

    /**
     * @param clientId
     * @return
     */
    public SdkApiClient setClientId(String clientId) {
        this.clientId = clientId;
        apiClient.setClientId(clientId);
        return this;
    }

    public SdkApiClient setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * Returna una llave generado offline localmente para firmar IOUs.
     *
     * @return un Keeper con la llave pública, privada y el scheme (tipo de firma)
     */
    public io.minka.api.model.Keeper getKeeperForOfflineSigning() {

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
     * @deprecated Se recomienda usar el método getKeeperForOfflineSigning para generar las llaves localmente.
     * @return un objeto con las llaves (privada y pública)
     */
    @Deprecated
    public io.minka.api.model.Keeper getKeeper() throws ApiException {
        return api.obtenerKeeper();
    }

    public ListLinks getLinks(String source, String target, String type) throws ExceptionResponseTinApi {
        try {
            Links response = api.getLink(source, target, type);
            List<LinkItem> entities = response.getEntities();
            ListLinks result = new ListLinks();
            result.addAll(entities);
            return result;
        } catch (io.minka.api.handler.ApiException e) {
            throw new ExceptionResponseTinApi(e.getCode(), e.getMessage());
        }
    }

    public LinkItem getLink(String source, String target) throws ExceptionResponseTinApi {
        try {
            Links response = api.getLink(source, target, null);
            if (response.getEntities().size() == 1){
                return response.getEntities().get(0);
            } else {
                throw new ExceptionResponseTinApi(112, "No existe link de confianza");
            }
        } catch (io.minka.api.handler.ApiException e) {
            throw new ExceptionResponseTinApi(e.getCode(), e.getMessage());
        }
    }

    public LinkItem deleteLink(String linkId) throws ApiException {
        
        return api.deleteLink(linkId);
    }
    public LinkItem createLink(String source, String target, io.minka.api.model.CreateLinkRequest.TypeEnum typeLink) throws ExceptionResponseTinApi {
        
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
        
        try {
            return api.createWallet(walletRequest);
        } catch (io.minka.api.handler.ApiException e) {
            System.out.println(e.getResponseBody());
            throw new WalletCreationException(Constants.UNEXPECTED_ERROR, Constants.UNEXPECTED_ERROR_MESSAGE);
        }
    }

    public io.minka.api.model.GetWalletResponse getWallet(String handle)
            throws ExceptionResponseTinApi {
        
        try {
            return api.getWalletByAlias(handle);
        } catch (io.minka.api.handler.ApiException e) {
            throw new ExceptionResponseTinApi(e.getCode(), e.getMessage());
        }
    }


    public io.minka.api.model.SignerResponse createSigner(SignerRequestLabels labels) throws io.minka.api.handler.ApiException {
        
        io.minka.api.model.SignerRequest signerRequest = new io.minka.api.model.SignerRequest();
        signerRequest.setLabels(labels);
        return api.createSigner(signerRequest);
    }

    public io.minka.api.model.SignerResponse createSignerOfflineSigning(SignerRequestLabels labels, io.minka.api.model.PublicKeys publicKey) throws io.minka.api.handler.ApiException {
        
        io.minka.api.model.SignerRequest signerRequest = new io.minka.api.model.SignerRequest();
        signerRequest.setLabels(labels);
        signerRequest.addKeeperItem(publicKey);

        return api.createSigner(signerRequest);
    }


    public io.minka.api.model.WalletUpdateResponse updateWallet(String handle, io.minka.api.model.WalletUpdateRequest createUpdateWalletReq) throws ExceptionResponseTinApi {
        
        try {
            return api.updateWallet(handle, createUpdateWalletReq);
        } catch (io.minka.api.handler.ApiException e) {
            String responseBody = e.getResponseBody();
            if (e.getCode() == Constants.BAD_REQUEST) {
                WalletUpdateResponse response = new Gson().fromJson(responseBody, WalletUpdateResponse.class);
                throw new ExceptionResponseTinApi(response.getError().getCode(), response.getError().getMessage());
            } else {
                throw new ExceptionResponseTinApi(Constants.UNEXPECTED_ERROR, Constants.UNEXPECTED_ERROR_MESSAGE);
            }
        }
    }

    public io.minka.api.model.BalanceResponse getBalance(String bankName, String currency) {
        
        io.minka.api.model.BalanceResponse balance = null;
        try {
            balance = api.getBalance(bankName, currency);
        } catch (io.minka.api.handler.ApiException e) {
            e.printStackTrace();
        }
        return balance;
    }

    public io.minka.api.model.CreateActionResponse createAction(io.minka.api.model.CreateActionRequest actionReq)
            throws io.minka.api.handler.ApiException {
        
        return api.createAction(actionReq);
    }

    public CreateTransferResponse continueTransaction(String actionId, ActionSigned actionSigned) throws ApiException {
        

        return api.continueP2Ptranfer(actionId, actionSigned);
    }

    public ActionSigned signActionOffline(String actionId, OfflineSigningKeys keys) throws io.minka.api.handler.ApiException{
        

        GetActionResponse actionPending = api.getActionByActionId(actionId);

        IouSigned iouSigned;
        try {
            iouSigned = IouUtil.generateIou(actionPending, domain, keys);

            return api.signOffline(actionId, iouSigned);
        } catch (Exception e) {

            throw new ApiException(e.getMessage());
        }

    }


    public ActionSigned signAction(String actionId, ActionSignedLabels actionLabels) throws ApiException {
        
        return api.signAction(actionId, actionLabels);
    }

    public GetActionResponse getAction(String hashValue) throws ApiException {
        
        return api.getActionByActionId(hashValue);
    }


    public CreateTransferResponse acceptTransfer(AcceptTransferRequest req,
                                                 String actionRequestId) throws ExceptionResponseTinApi {
        
        try {
            return api.acceptP2Ptranfer(actionRequestId, req);
        } catch (ApiException e) {
            System.out.println(e.getResponseBody());
            System.out.println(e.getCode());
            System.out.println(e.getResponseHeaders());

            System.out.println(e.getResponseBody());
            ErrorGenerico errorGenerico = new Gson().fromJson(e.getResponseBody(), ErrorGenerico.class);
            throw new ExceptionResponseTinApi(errorGenerico.getCode(), e.getMessage());
        }
    }


    public CreateTransferResponse rejectTransfer(RejectTransferRequest req, String actionId) throws ExceptionResponseTinApi, io.minka.api.handler.ApiException {
        
        return api.rejectP2Ptranfer(actionId, req);
    }

    public GetTransfersResponse getActionsWithCustomQuery(String customQuery) throws ApiException {
        
        return api.getActions(customQuery);
    }

    public SignerListResponse getSignersWithCustomQuery(String customQuery) throws ApiException {
        
        return api.getSigners(customQuery);
    }

    public GetWalletResponse getWalletWithCustomQuery(String customQuery) throws ApiException {
        
        return api.getWalletByAlias(customQuery);
    }

    public List<GetActionResponse> getActionPendings(String alias, AliasType aliasType, ActionType action) throws ApiException {
        

        String customQuery = "?";
        if (aliasType.getValue().equals(AliasType.TARGET.getValue())){
            customQuery = customQuery + "target=" + alias;
        } else{
            customQuery = customQuery + "source=" + alias;
        }
        customQuery = customQuery + "&labels.type=" + action.getValue() + "&labels.status=PENDING";
        System.out.println(customQuery);
        return api.getActions(customQuery).getEntities();
    }

    public Transfers getTransferPendings(String type, String target, String source) throws io.minka.api.handler.ApiException {

        String customQuery = "?" + "target=" + target + "source=" + source + "&labels.type=" + type + "&labels.status=PENDING";;
        return  api.getTransfers(customQuery);
    }


    public CreateTransferResponse createTinTransfer(CreateTransferRequest tintransfer)
            throws io.minka.api.handler.ApiException {

        

        return api.createTinTransfer(tintransfer);
    }

    public WalletListResponse getWallets(int pagenum, int pagesize) throws io.minka.api.handler.ApiException {
        

        WalletsResponse walletBySigner = api.getWalletBySigner("?pagesize=" + pagesize + "&pagenum=" + pagenum);

        List<WalletResponse> entities = walletBySigner.getEntities();
        WalletListResponse walletResponses = new WalletListResponse();
        for (WalletResponse curr: entities) {
            walletResponses.add(curr);
        }
        return  walletResponses;
    }

    public SignerListResponse getSigners(int pagenum, int pagesize) throws io.minka.api.handler.ApiException {
        
        return api.getSigners("?pagesize=" + pagesize + "&pagenum=" + pagenum);
    }

    public io.minka.api.model.SignerResponse deleteSigner(String signerAddress) throws io.minka.api.handler.ApiException {
        
        return api.deleteSignerByAddress(signerAddress);
    }

    public GenericResponse updateSigner(String signerAddress, SignerRequest updateSignerReq) throws io.minka.api.handler.ApiException {
        
        return api.updateSignerCustom(signerAddress, updateSignerReq);
    }

    public SignerResponse updateSignerParameters(String signerAddress, SignerRequest updateSignerReq) throws io.minka.api.handler.ApiException {
        
        return api.updateSigner(signerAddress, updateSignerReq);
    }

    public io.minka.api.model.SignerResponse getSignerByAddress(String wAddress) throws io.minka.api.handler.ApiException {
        
        return api.getSignerByAddress(wAddress);
    }


    public GetActionResponse updateActionParameters(String actionid, UpdateActionRequest req)
            throws io.minka.api.handler.ApiException {
        

        return api.updateActionLabels(actionid, req);
    }

    public GetActionResponse updateAction(String actionid,
                                          UpdateActionRequest req)
            throws io.minka.api.handler.ApiException {
        

        return api.updateActionLabels(actionid, req);
    }


    public io.minka.api.model.GetWalletResponse getWalletByAlias(String aliasHandle)
            throws io.minka.api.handler.ApiException {
        
        return api.getWalletByAlias(aliasHandle);
    }


    @Deprecated
    public GetTransfersResponse getActions() throws io.minka.api.handler.ApiException {
        
        return api.getActions("?");
    }


    public TokenResponse getToken() throws io.minka.api.handler.ApiException {
        ApiClient apiClientToken = new ApiClient();

        if (proxy != null) {
            apiClientToken.getHttpClient().setProxy(proxy);
        }
        apiClientToken.setUsername(clientId);
        apiClientToken.setPassword(secret);

        apiClientToken.setBasePath(this.url.substring(0, this.url.length() - 3));
        TokenApi api = new TokenApi(apiClientToken);
        return api.getToken("client_credentials", clientId, secret);
    }

    public String generateUUID() {
        return String.valueOf((new Date()).getTime()) + "-" + UUID.randomUUID().toString();
    }

    public Conciliation getConciliation(String customQuery) throws ApiException {
        
        return api.getConciliation(customQuery);
    }

    public GetAnalyticsResponse getAnalytics(String customQuery) throws ApiException {
        
        return api.getTransfersAnalytics(customQuery);
    }

    public Transfers getTransfersWithCustomQuery(String query) throws ApiException {
        
        return api.getTransfers(query);
    }
}
