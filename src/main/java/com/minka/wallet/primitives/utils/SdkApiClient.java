package com.minka.wallet.primitives.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

/***
 * Cliente para la integración con el servicio WEB de TINAPI de MINKA
 */
public class SdkApiClient {

    private final CustomApiClient apiClient;
    private final String domain;
    private String url;
    private String apiKey;
    private String secret;
    private String clientId;
    private int timeout;
    private boolean oauth2;
    private Proxy proxy;
    private Gson gson;

    public SdkApiClient(String domain, String apiKey, String urlBase) {
        this.domain = domain;
        this.url = urlBase;
        this.apiKey = apiKey;
        apiClient = new CustomApiClient();
        apiClient.setApiKey(apiKey);
        gson = (new GsonBuilder()).create();
        apiClient.setBasePath(url);

        if (timeout > 0) {
            apiClient.setConnectTimeout(timeout);
        }
    }

    @Deprecated
    public SdkApiClient(String domain, String apiKey) {
        this.domain = domain;
        this.url = "https://" + domain + ".minka.io/v1";
        this.apiKey = apiKey;
        apiClient = new CustomApiClient();
        apiClient.setApiKey(apiKey);
        apiClient.setBasePath(url);

        if (timeout > 0) {
            apiClient.setConnectTimeout(timeout);
        }
    }

    public SdkApiClient setProxy(String host, int port) {
        SocketAddress socketAddress = new InetSocketAddress(host, port);
        this.proxy = new Proxy(Proxy.Type.HTTP, socketAddress);
        this.apiClient.getHttpClient().setProxy(proxy);
        return this;
    }

    private void refreshToken() throws ApiException {
        TokenResponse token;
        token = getToken();
        io.minka.api.handler.auth.OAuth oAuth2ClientCredentials;
        oAuth2ClientCredentials = (io.minka.api.handler.auth.OAuth)
                apiClient.getAuthentication("oAuth2ClientCredentials");
        oAuth2ClientCredentials.setAccessToken(token.getAccessToken());

        updateHeaders();
    }

    private void updateHeaders() {
        ApiKeyAuth xApiKey = (ApiKeyAuth) apiClient.getAuthentication("ApiKeyAuth");
        xApiKey.setApiKey(apiKey);

        ApiKeyAuth xNonce = (ApiKeyAuth) apiClient.getAuthentication("XNonce");
        xNonce.setApiKey(generateUUID());
    }

    /**
     * @param secret
     * @return
     */
    public SdkApiClient setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    /**
     * @param clientId
     * @return
     */
    public SdkApiClient setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public SdkApiClient setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * Returna una llave generado offline localmente para firmar IOUs.
     *
     * @return
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
     *
     * @return un objeto con las llaves (privada y pública)
     */
    public io.minka.api.model.Keeper getKeeper() throws ExceptionResponseTinApi {

        io.minka.api.handler.KeeperApi api = new io.minka.api.handler.KeeperApi(apiClient);

        try {
            Keeper keeperGenerated;
            keeperGenerated = api.obtenerKeeper();
            return keeperGenerated;
        } catch (io.minka.api.handler.ApiException e) {
            boolean didTokenExpired = e.getCode() == Constants.FORBIDDEN;
            if (didTokenExpired) {
                try {
                    refreshToken();
                    return api.obtenerKeeper();
                } catch (ApiException e1) {
                    throw new ExceptionResponseTinApi(e.getCode(), e.getMessage());
                }
            } else {
                throw new ExceptionResponseTinApi(e.getCode(), e.getMessage());
            }
        }
    }

    public ListLinks getLinks(String source, String target, String type) throws ExceptionResponseTinApi {
        io.minka.api.handler.LinksApi api = new io.minka.api.handler.LinksApi(apiClient);
        try {
            Links response = api.getLink(source, target, type);
            List<LinkItem> entities = response.getEntities();
            ListLinks result = new ListLinks();
            result.addAll(entities);
            return result;
        } catch (io.minka.api.handler.ApiException e) {
            boolean didTokenExpired = e.getCode() == Constants.FORBIDDEN;
            if (didTokenExpired) {
                try {
                    refreshToken();
                    Links response = api.getLink(source, target, type);
                    List<LinkItem> entities = response.getEntities();
                    ListLinks result = new ListLinks();
                    result.addAll(entities);
                    return result;
                } catch (ApiException e1) {
                    throw new ExceptionResponseTinApi(e.getCode(), e.getMessage());
                }
            } else {
                throw new ExceptionResponseTinApi(e.getCode(), e.getMessage());
            }
        }
    }

    public LinkItem getLink(String source, String target) throws ExceptionResponseTinApi {
        io.minka.api.handler.LinksApi api = new io.minka.api.handler.LinksApi(apiClient);
        try {
            return getLinkItem(source, target, api);
        } catch (io.minka.api.handler.ApiException e) {
            if(didTokenExpiredOrAbsent(e)){
                try {
                    refreshToken();
                    return getLinkItem(source, target, api);
                } catch (ApiException e1) {
                    throw new ExceptionResponseTinApi(e1.getCode(), e1.getMessage());
                }
            }else {
                throw new ExceptionResponseTinApi(e.getCode(), e.getMessage());
            }
        }
    }

    private boolean didTokenExpiredOrAbsent(ApiException e) {
        return Constants.FORBIDDEN == e.getCode();
    }

    private LinkItem getLinkItem(String source, String target, LinksApi api) throws ApiException, ExceptionResponseTinApi {
        Links response = api.getLink(source, target, null);
        if (response.getEntities().size() == 1){
            return response.getEntities().get(0);
        } else {
            throw new ExceptionResponseTinApi(112, "No existe link de confianza");
        }
    }

    public LinkItem deleteLink(String linkId) throws ApiException {
        io.minka.api.handler.LinksApi api = new io.minka.api.handler.LinksApi(apiClient);

        try {
            return api.deleteLink(linkId);
        } catch (ApiException e) {
            if (didTokenExpiredOrAbsent(e)){
                refreshToken();
                return api.deleteLink(linkId);
            } else {
                throw e;
            }
        }
    }
    public LinkItem createLink(String source, String target, io.minka.api.model.CreateLinkRequest.TypeEnum typeLink) throws ExceptionResponseTinApi {
        io.minka.api.handler.LinksApi api = new io.minka.api.handler.LinksApi(apiClient);

        io.minka.api.model.CreateLinkRequest req = new io.minka.api.model.CreateLinkRequest();
        req.setSource(source);
        req.setTarget(target);
        req.setType(typeLink);
        try {
            return api.createLink(req);
        } catch (ApiException e) {
            if (didTokenExpiredOrAbsent(e)){
                try {
                    refreshToken();
                    return api.createLink(req);
                } catch (ApiException e1) {
                    throw new ExceptionResponseTinApi(e1.getCode(), e1.getMessage());
                }
            }  else {
                throw new ExceptionResponseTinApi(e.getCode(), e.getMessage());
            }
        }
    }


    public io.minka.api.model.WalletResponse createWallet(WalletRequest walletRequest)
            throws WalletCreationException {

        io.minka.api.handler.WalletApi walletApi;
        walletApi = new io.minka.api.handler.WalletApi(apiClient);
        try {
            return walletApi.createWallet(walletRequest);
        } catch (io.minka.api.handler.ApiException e) {
            if (didTokenExpiredOrAbsent(e)){
                try {
                    refreshToken();
                    return walletApi.createWallet(walletRequest);
                } catch (ApiException e1) {
                    throw new WalletCreationException(Constants.UNEXPECTED_ERROR, e1.getMessage());
                }
            }else{
                throw new WalletCreationException(Constants.UNEXPECTED_ERROR, Constants.UNEXPECTED_ERROR_MESSAGE);
            }
        }
    }

    public io.minka.api.model.GetWalletResponse getWallet(String handle)
            throws ExceptionResponseTinApi {

        io.minka.api.handler.WalletApi api = new io.minka.api.handler.WalletApi(apiClient);
        try {
            return api.getWalletByAlias(handle);
        } catch (io.minka.api.handler.ApiException e) {
            if (didTokenExpiredOrAbsent(e)){
                try {
                    refreshToken();
                    return api.getWalletByAlias(handle);
                } catch (ApiException e1) {
                    throw new ExceptionResponseTinApi(e1.getCode(), e1.getMessage());
                }
            }else{
                throw new ExceptionResponseTinApi(e.getCode(), e.getMessage());
            }
        }
    }


    public io.minka.api.model.SignerResponse createSigner(SignerRequestLabels labels) throws ApiException {
        io.minka.api.model.SignerRequest signerRequest = new io.minka.api.model.SignerRequest();
        signerRequest.setLabels(labels);
        io.minka.api.handler.SignerApi signerApi = new io.minka.api.handler.SignerApi(apiClient);
        return createSignerInternal(signerRequest, signerApi);
    }

    private SignerResponse createSignerInternal(SignerRequest signerRequest, SignerApi signerApi) throws ApiException {
        try {
            return signerApi.createSigner(signerRequest);
        } catch (ApiException e) {
            if (didTokenExpiredOrAbsent(e)){
                refreshToken();
                return signerApi.createSigner(signerRequest);
            }else {
                throw e;
            }
        }
    }

    public io.minka.api.model.SignerResponse createSignerOfflineSigning(SignerRequestLabels labels, io.minka.api.model.PublicKeys publicKey) throws io.minka.api.handler.ApiException {
        io.minka.api.handler.SignerApi api = new io.minka.api.handler.SignerApi(apiClient);

        io.minka.api.model.SignerRequest signerRequest = new io.minka.api.model.SignerRequest();
        signerRequest.setLabels(labels);
        signerRequest.addKeeperItem(publicKey);
        return createSignerInternal(signerRequest, api);

    }

    public io.minka.api.model.WalletUpdateResponse updateWallet(String handle, io.minka.api.model.WalletUpdateRequest createUpdateWalletReq) throws ExceptionResponseTinApi {
        io.minka.api.handler.WalletApi api = new io.minka.api.handler.WalletApi(apiClient);
        try {
            return api.updateWallet(handle, createUpdateWalletReq);
        } catch (io.minka.api.handler.ApiException e) {
            String responseBody = e.getResponseBody();
            if (didTokenExpiredOrAbsent(e)){
                try {
                    refreshToken();
                    return api.updateWallet(handle, createUpdateWalletReq);
                } catch (ApiException e1) {
                    return handleExceptionUpdateWallet(e1, responseBody);
                }
            } else{
                return handleExceptionUpdateWallet(e, responseBody);
            }

        }
    }

    private WalletUpdateResponse handleExceptionUpdateWallet(ApiException e, String responseBody) throws ExceptionResponseTinApi {
        if (e.getCode() == Constants.BAD_REQUEST) {
            WalletUpdateResponse response = new Gson().fromJson(responseBody, WalletUpdateResponse.class);
            throw new ExceptionResponseTinApi(response.getError().getCode(), response.getError().getMessage());
        } else {
            throw new ExceptionResponseTinApi(Constants.UNEXPECTED_ERROR, Constants.UNEXPECTED_ERROR_MESSAGE);
        }
    }

    public io.minka.api.model.BalanceResponse getBalance(String bankName, String currency) throws ApiException {

        io.minka.api.handler.WalletApi walletApi;
        walletApi = new io.minka.api.handler.WalletApi(apiClient);
        try {
            return walletApi.getBalance(bankName, currency);
        } catch (ApiException e) {
            if (didTokenExpiredOrAbsent(e)){
                refreshToken();
                return walletApi.getBalance(bankName, currency);
            }else{
                throw e;
            }
        }
    }

    public io.minka.api.model.CreateActionResponse createAction(io.minka.api.model.CreateActionRequest actionReq)
            throws io.minka.api.handler.ApiException {

        io.minka.api.handler.ActionApi actionApi = new io.minka.api.handler.ActionApi(apiClient);
        try {
            return actionApi.createAction(actionReq);
        } catch (ApiException e) {
            if (didTokenExpiredOrAbsent(e)){
                refreshToken();
                return actionApi.createAction(actionReq);
            } else {
                throw e;
            }
        }
    }

    public CreateTransferResponse continueTransaction(String actionId, ActionSigned actionSigned) throws ApiException {
        refreshToken();
        TransferApi api = new TransferApi(apiClient);
        try {
            return api.continueP2Ptranfer(actionId, actionSigned);
        } catch (ApiException e) {
            if (didTokenExpiredOrAbsent(e)){
                refreshToken();
                return api.continueP2Ptranfer(actionId, actionSigned);
            } else {
                throw e;
            }
        }
    }

    public ActionSigned signActionOffline(String actionId, OfflineSigningKeys keys) throws io.minka.api.handler.ApiException{

        io.minka.api.handler.ActionApi api = new io.minka.api.handler.ActionApi(apiClient);
        GetActionResponse actionPending = api.getActionByActionId(actionId);
        IouSigned iouSigned = null;

        try {
            iouSigned = IouUtil.generateIou(actionPending, domain, keys);
            return api.signOffline(actionId, iouSigned);
        } catch (ApiException e1) {
            if (didTokenExpiredOrAbsent(e1)){
                refreshToken();
                return api.signOffline(actionId, iouSigned);
            } else {
                throw new ApiException(e1.getMessage());
            }
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }


    public ActionSigned signAction(String actionId, ActionSignedLabels actionLabels) throws ApiException {
        ActionApi actionApi = new ActionApi(apiClient);

        try {
            return actionApi.signAction(actionId, actionLabels);
        } catch (ApiException e) {
            if (didTokenExpiredOrAbsent(e)){
                refreshToken();
                return actionApi.signAction(actionId, actionLabels);
            } else {
                throw e;
            }
        }
    }

    public GetActionResponse getAction(String hashValue) throws ApiException {

        ActionApi actionApi = new ActionApi(apiClient);

        try {
            return actionApi.getActionByActionId(hashValue);
        } catch (ApiException e) {
            if (didTokenExpiredOrAbsent(e)){
                refreshToken();
                return actionApi.getActionByActionId(hashValue);
            } else {
                throw e;
            }
        }
    }


    public CreateTransferResponse acceptTransfer(AcceptTransferRequest req,
                                                 String actionRequestId) throws ExceptionResponseTinApi {

        TransferApi transferApi = new TransferApi(apiClient);
        try {
            return transferApi.acceptP2Ptranfer(actionRequestId, req);
        } catch (ApiException e) {

            if (didTokenExpiredOrAbsent(e)){
                try {
                    refreshToken();
                    return transferApi.acceptP2Ptranfer(actionRequestId, req);
                } catch (ApiException e1) {
                    ErrorGenerico errorGenerico = new Gson().fromJson(e1.getResponseBody(), ErrorGenerico.class);
                    throw new ExceptionResponseTinApi(errorGenerico.getCode(), e1.getMessage());
                }
            } else {
                ErrorGenerico errorGenerico = new Gson().fromJson(e.getResponseBody(), ErrorGenerico.class);
                throw new ExceptionResponseTinApi(errorGenerico.getCode(), e.getMessage());
            }
        }
    }


    public CreateTransferResponse rejectTransfer(RejectTransferRequest req, String actionId) throws ExceptionResponseTinApi, io.minka.api.handler.ApiException {

        TransferApi transferApi = new TransferApi(apiClient);

        try{
            return transferApi.rejectP2Ptranfer(actionId, req);

        } catch (ApiException e){
            if (didTokenExpiredOrAbsent(e)){
                refreshToken();
                return transferApi.rejectP2Ptranfer(actionId, req);
            }else{
                throw e;
            }
        }
    }

    public GetTransfersResponse getActionsWithCustomQuery(String customQuery) throws ApiException {
        io.minka.api.handler.ActionApi tempoapi = new io.minka.api.handler.ActionApi(apiClient);
        try{
            return tempoapi.getActions(customQuery);
        } catch (ApiException e){
            if (didTokenExpiredOrAbsent(e)){
                refreshToken();
                return tempoapi.getActions(customQuery);
            }else{
                throw e;
            }
        }
    }

    public SignerListResponse getSignersWithCustomQuery(String customQuery) throws ApiException {
        io.minka.api.handler.SignerApi tempoapi = new io.minka.api.handler.SignerApi(apiClient);
        try{
            return tempoapi.getSigners(customQuery);
        } catch (ApiException e){
            if (didTokenExpiredOrAbsent(e)){
                refreshToken();
                return tempoapi.getSigners(customQuery);
            }else{
                throw e;
            }
        }
    }

    public GetWalletResponse getWalletWithCustomQuery(String customQuery) throws ApiException {
        io.minka.api.handler.WalletApi tempoapi = new io.minka.api.handler.WalletApi(apiClient);
        try{
            return tempoapi.getWalletByAlias(customQuery);
        } catch (ApiException e){
            if (didTokenExpiredOrAbsent(e)){
                refreshToken();
                return tempoapi.getWalletByAlias(customQuery);
            }else{
                throw e;
            }
        }
    }

    public List<GetActionResponse> getActionPendings(String alias, AliasType aliasType, ActionType action) throws ApiException {

        io.minka.api.handler.ActionApi tempoapi = new io.minka.api.handler.ActionApi(apiClient);

        String customQuery = "?";
        if (aliasType.getValue().equals(AliasType.TARGET.getValue())){
            customQuery = customQuery + "target=" + alias;
        } else{
            customQuery = customQuery + "source=" + alias;
        }
        customQuery = customQuery + "&labels.type=" + action.getValue() + "&labels.status=PENDING";
        try{
            return tempoapi.getActions(customQuery).getEntities();
        } catch (ApiException e){
            if (didTokenExpiredOrAbsent(e)){
                refreshToken();
                return tempoapi.getActions(customQuery).getEntities();
            }else{
                throw e;
            }
        }
    }

    public Transfers getTransferPendings(String type, String target, String source) throws io.minka.api.handler.ApiException {
        io.minka.api.handler.TransferApi api = new io.minka.api.handler.TransferApi(apiClient);

        String customQuery = "?" + "target=" + target + "source=" + source + "&labels.type=" + type + "&labels.status=PENDING";;
        return  api.getTransfers(customQuery);
    }


    public CreateTransferResponse createTinTransfer(CreateTransferRequest tintransfer)
            throws io.minka.api.handler.ApiException {

        io.minka.api.handler.TransferApi api = new TransferApi(apiClient);
        try{
            return api.createTinTransfer(tintransfer);
        } catch (ApiException e){
            if (didTokenExpiredOrAbsent(e)){
                refreshToken();
                return api.createTinTransfer(tintransfer);
            }else{
                throw e;
            }
        }
    }

    public WalletListResponse getWallets(int pagenum, int pagesize) throws io.minka.api.handler.ApiException {


        io.minka.api.handler.WalletApi api = new io.minka.api.handler.WalletApi(apiClient);

        WalletsResponse walletBySigner;
        try{
            walletBySigner = api.getWalletBySigner("?pagesize=" + pagesize + "&pagenum=" + pagenum);
        } catch (ApiException e){
            if (didTokenExpiredOrAbsent(e)){
                refreshToken();
                walletBySigner = api.getWalletBySigner("?pagesize=" + pagesize + "&pagenum=" + pagenum);
            }else{
                throw e;
            }
        }

        List<WalletResponse> entities = walletBySigner.getEntities();
        WalletListResponse walletResponses = new WalletListResponse();
        for (WalletResponse curr: entities) {
            walletResponses.add(curr);
        }
        return  walletResponses;
    }

    public SignerListResponse getSigners(int pagenum, int pagesize) throws io.minka.api.handler.ApiException {
        io.minka.api.handler.SignerApi api = new io.minka.api.handler.SignerApi(apiClient);
        try{
            return api.getSigners("?pagesize=" + pagesize + "&pagenum=" + pagenum);
        } catch (ApiException e){
            if (didTokenExpiredOrAbsent(e)){
                refreshToken();
                return api.getSigners("?pagesize=" + pagesize + "&pagenum=" + pagenum);
            }else{
                throw e;
            }
        }
    }

    public io.minka.api.model.SignerResponse deleteSigner(String signerAddress) throws io.minka.api.handler.ApiException {

        io.minka.api.handler.SignerApi api = new io.minka.api.handler.SignerApi(apiClient);
        try{
            return api.deleteSignerByAddress(signerAddress);
        } catch (ApiException e){
            if (didTokenExpiredOrAbsent(e)){
                refreshToken();
                return api.deleteSignerByAddress(signerAddress);
            }else{
                throw e;
            }
        }
    }

    public GenericResponse updateSigner(String signerAddress, SignerRequest updateSignerReq) throws io.minka.api.handler.ApiException {
        DefaultApi api = new DefaultApi(apiClient);
        try{
            return api.updateSignerCustom(signerAddress, updateSignerReq);
        } catch (ApiException e){
            if (didTokenExpiredOrAbsent(e)){
                refreshToken();
                return api.updateSignerCustom(signerAddress, updateSignerReq);
            }else{
                throw e;
            }
        }
    }

    public SignerResponse updateSignerParameters(String signerAddress, SignerRequest updateSignerReq) throws io.minka.api.handler.ApiException {

        io.minka.api.handler.SignerApi api = new io.minka.api.handler.SignerApi(apiClient);
        try{
            return api.updateSigner(signerAddress, updateSignerReq);

        } catch (ApiException e){
            if (didTokenExpiredOrAbsent(e)){
                refreshToken();
                return api.updateSigner(signerAddress, updateSignerReq);
            }else{
                throw e;
            }
        }

    }

    public io.minka.api.model.SignerResponse getSignerByAddress(String wAddress) throws io.minka.api.handler.ApiException {

        io.minka.api.handler.SignerApi api = new io.minka.api.handler.SignerApi(apiClient);
        try{
            return api.getSignerByAddress(wAddress);
        } catch (ApiException e){
            if (didTokenExpiredOrAbsent(e)){
                refreshToken();
                return api.getSignerByAddress(wAddress);
            }else{
                throw e;
            }
        }
    }


    public GetActionResponse updateActionParameters(String actionid, UpdateActionRequest req)
            throws io.minka.api.handler.ApiException {

        io.minka.api.handler.ActionApi api = new io.minka.api.handler.ActionApi(apiClient);
        try{
            return api.updateActionLabels(actionid, req);

        } catch (ApiException e){
            if (didTokenExpiredOrAbsent(e)){
                refreshToken();
                return api.updateActionLabels(actionid, req);

            }else{
                throw e;
            }
        }
    }

    public GenericResponse updateAction(String actionid,
                                        UpdateActionRequest req)
            throws io.minka.api.handler.ApiException {
        io.minka.api.handler.DefaultApi api = new io.minka.api.handler.DefaultApi(apiClient);

        try{
            return api.updateActionLabels2(actionid, req);

        } catch (ApiException e){
            if (didTokenExpiredOrAbsent(e)){
                refreshToken();
                return api.updateActionLabels2(actionid, req);
            }else{
                throw e;
            }
        }

    }


    public io.minka.api.model.GetWalletResponse getWalletByAlias(String aliasHandle)
            throws io.minka.api.handler.ApiException {

        io.minka.api.handler.WalletApi api;
        api = new io.minka.api.handler.WalletApi(apiClient);
        try{
            return api.getWalletByAlias(aliasHandle);


        } catch (ApiException e){
            if (didTokenExpiredOrAbsent(e)){
                refreshToken();
                return api.getWalletByAlias(aliasHandle);

            }else{
                throw e;
            }
        }
    }


    public GetTransfersResponse getActions() throws io.minka.api.handler.ApiException {

        io.minka.api.handler.ActionApi api = new io.minka.api.handler.ActionApi(apiClient);
        try{
            return api.getActions("?");


        } catch (ApiException e){
            if (didTokenExpiredOrAbsent(e)){
                refreshToken();
                return api.getActions("?");
            }else{
                throw e;
            }
        }

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

    /**
     * It is compulsory to use oauth2
     */
    @Deprecated
    public void setOauth2On() {
        oauth2 = true;
    }
    /**
     * It is compulsory to use oauth2
     */
    @Deprecated
    public void setOauth2Off() {
        oauth2 = false;
    }

    public String generateUUID() {
        return String.valueOf((new Date()).getTime()) + "-" + UUID.randomUUID().toString();
    }

    public Conciliation getConciliation(String customQuery) throws ApiException {


        io.minka.api.handler.ConciliationApi api = new io.minka.api.handler.ConciliationApi(apiClient);
        try{
            return api.getConciliation(customQuery);

        } catch (ApiException e){
            if (didTokenExpiredOrAbsent(e)){
                refreshToken();
                return api.getConciliation(customQuery);

            }else{
                throw e;
            }
        }
    }

    public GetAnalyticsResponse getAnalytics(String customQuery) throws ApiException {

        io.minka.api.handler.TransferApi api = new io.minka.api.handler.TransferApi(apiClient);

        try{
            return api.getTransfersAnalytics(customQuery);
        } catch (ApiException e){
            if (didTokenExpiredOrAbsent(e)){
                refreshToken();
                return api.getTransfersAnalytics(customQuery);
            }else{
                throw e;
            }
        }

    }

    public Transfers getTransfersWithCustomQuery(String query) throws ApiException {
        refreshToken();
        io.minka.api.handler.TransferApi api = new io.minka.api.handler.TransferApi(apiClient);
        return api.getTransfers(query);
    }

    public GetVendorsResponse verifyPayment(String vendorId, String invoiceId, String customQuery) throws ApiException {
        ShdApi api = new ShdApi(apiClient);
        try{
            return api.getVendors(vendorId, invoiceId, customQuery);

        } catch (ApiException e){
            if (didTokenExpiredOrAbsent(e)){
                refreshToken();
                return api.getVendors(vendorId, invoiceId, customQuery);
            }else{
                throw e;
            }
        }
    }
}
