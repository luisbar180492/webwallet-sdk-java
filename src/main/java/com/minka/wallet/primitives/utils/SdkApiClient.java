package com.minka.wallet.primitives.utils;

import com.google.gson.Gson;
import com.minka.ExceptionResponseTinApi;
import com.minka.api.handler.*;
import com.minka.api.handler.ActionApi;
import com.minka.api.handler.ApiException;
import com.minka.api.handler.LinksApi;
import com.minka.api.handler.SignerApi;
import com.minka.api.handler.WalletApi;
import com.minka.api.handler.WalletTempoApi;
import com.minka.api.model.BalanceResponse;
import com.minka.api.model.CreateActionRequest;
import com.minka.api.model.CreateActionResponse;
import com.minka.api.model.CreateLinkRequest;
import com.minka.api.model.ErrorForbidden;
import com.minka.api.model.ErrorResponse;
import com.minka.api.model.GenericResponse;
import com.minka.api.model.GetLinkResponse;
import com.minka.api.model.GetWalletResponse;
import com.minka.api.model.LabelsStatusRequest;
import com.minka.api.model.PendingActionResponse;
import com.minka.api.model.SignerRequest;
import com.minka.api.model.SignerResponse;
import com.minka.api.model.SmsRequest;
import com.minka.api.model.WalletRequest;
import com.minka.api.model.WalletResponse;
import com.minka.api.model.WalletUpdateRequest;
import com.minka.api.model.WalletUpdateResponse;
import com.minka.utils.ActionType;
import com.minka.utils.AliasType;
import com.minka.utils.Constants;
import io.minka.api.handler.*;
import io.minka.api.handler.ApiClient;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

/***
 * Cliente para la integración con el servicio WEB de TINAPI de MINKA
 */
public class SdkApiClient {

    private final ApiClient apiClient;
    private String url;
    private String apiKey;
    private String secret;
    private String clientId;
    private int timeout;
    private String bankLimitAccountWallet;
    private String bankLimitAccountSigner;

    /**
     *
     * @param domain
     * @param apiKey
     */
    public SdkApiClient(String domain, String apiKey) {
        this.url = "https://" + domain + ".minka.io/v1";
        this.apiKey = apiKey;
        apiClient = new ApiClient();
        apiClient.setApiKey(apiKey);
        apiClient.setBasePath(url);
        if (timeout > 0){
            apiClient.setConnectTimeout(timeout);
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

    public SdkApiClient setBankLimitParams(String bankLimitAccountWallet,
                                            String bankLimitAccountSigner){
        this.bankLimitAccountWallet = bankLimitAccountWallet;
        this.bankLimitAccountSigner = bankLimitAccountSigner;
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
    public io.minka.api.model.Keeper getKeeper() throws ExceptionResponseTinApi {

        io.minka.api.handler.KeeperApi api = new io.minka.api.handler.KeeperApi(apiClient);

        try {
            return api.obtenerKeeper();
        } catch (io.minka.api.handler.ApiException e) {

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

public String confirmTransferRequest(String handleSourceAddress, 
                                String actionRequestId
                                ){
    try {
            GenericResponse actionResponse = getAction(actionRequestId);
            //get amount data from action
            String amount =  (String) actionResponse.get("amount");
            //create upload action with amount from bank to target address 
            CreateActionRequest req = new CreateActionRequest();
            Map<String, Object> labels = new HashMap<>();
            labels.put("type", "DOWNLOAD");
            req.setLabels(labels);
            req.setAmount(amount);
            req.setSource(handleSourceAddress);
            req.setSymbol("$tin");
            req.setTarget(this.bankLimitAccountSigner);
            CreateActionResponse action = null;
            action = createAction(req);
            String action_id =  (String) action.get("action_id");
            System.out.println(action_id);
            //sign upload action with amount from bank to target address 
            GenericResponse genericResponse_download = signAction(action_id);
            //TODO: notify bank status endpoint source
//            notifyStatusToBank(handleSourceAddress, action_id);
            return (String) genericResponse_download.get("action_id");
        } catch (ApiException e) {
            System.out.println("e.getResponseBody()");
            System.out.println(e.getResponseBody());
            return null;
//        } catch (ExceptionResponseTinApi exceptionResponseTinApi) {
//            return null;
        }
}    
    
public String acceptTransferRequest(String handleTargetAddress, 
                                String actionRequestId){
        //read the action 
        try {
            GenericResponse actionResponse = getAction(actionRequestId);
            //get amount data from action
            String amount =  (String) actionResponse.get("amount");
            //create upload action with amount from bank to target address 
            CreateActionRequest req = new CreateActionRequest();
            Map<String, Object> labels = new HashMap<>();
            labels.put("type", "UPLOAD");
            req.setLabels(labels);
            req.setAmount(amount);
            req.setSource(this.bankLimitAccountSigner);
            req.setSymbol("$tin");
            req.setTarget(handleTargetAddress);
            CreateActionResponse action = null;
            action = createAction(req);
            String action_id =  (String) action.get("action_id");
            System.out.println(action_id);
            //sign upload action with amount from bank to target address 
            GenericResponse genericResponse_upload = signAction(action_id);
            System.out.println(genericResponse_upload);
            GenericResponse genericResponse_send = signAction(actionRequestId);
            //TODO: notify bank with credit download endpoint
//            notifyStatusToBank(handleTargetAddress, action_id);
            return (String) genericResponse_send.get("action_id");
        } catch (ApiException e) {
            System.out.println("e.getResponseBody()");
            System.out.println(e.getResponseBody());
            return null;
//        } catch (ExceptionResponseTinApi exceptionResponseTinApi) {
//            exceptionResponseTinApi.printStackTrace();
//            return null;
        }
}

public String createTransferRequest(String handleTarget, 
                                String handleSourceAddress,
                                String amount,
                                String smsMessage){
        CreateActionRequest req = new CreateActionRequest();
        Map<String, Object> labels = new HashMap<>();
        labels.put("type", "REQUEST");
        req.setLabels(labels);
        req.setAmount(amount);
        req.setSource(handleTarget);
        req.setSymbol("$tin");
        req.setTarget(handleSourceAddress);
        System.out.println(req);

        CreateActionResponse action = null;
        try {
            action = createAction(req);
            String action_id = (String) action.get("action_id");
            System.out.println(action_id);
            ErrorResponse sendSms = sendSms(handleTarget, smsMessage);
            if (sendSms != null){
                if (sendSms.getError().getCode() == 0){
                    return (String) action.get("action_id");
                }                
            }
            return null;
        } catch (ApiException e) {
            System.out.println("e.getResponseBody()");
            System.out.println(e.getResponseBody());
//            e.printStackTrace();
            return null;
        }

   }

public String createNoTrustedTransfer(String handleTarget, 
                             String handleSourceAddress,
                             String amount,
                             String smsMessage){
        //read the action 
        try {
            //upload
            CreateActionRequest req_upload = new CreateActionRequest();
            Map<String, Object> labels_upload = new HashMap<>();
            labels_upload.put("type", "UPLOAD");
            req_upload.setLabels(labels_upload);
            req_upload.setAmount(amount);
            req_upload.setSource(this.bankLimitAccountSigner);
            req_upload.setSymbol("$tin");
            req_upload.setTarget(handleSourceAddress);
            CreateActionResponse action_upload = null;
            action_upload = createAction(req_upload);//call to aPi
            String action_id_upload =  (String) action_upload.get("action_id");
            System.out.println(action_id_upload);
            //sign upload action with amount from bank to target address 
            GenericResponse genericResponse_upload = signAction(action_id_upload);
            System.out.println(genericResponse_upload);
            //send
            CreateActionRequest req_send = new CreateActionRequest();
            Map<String, Object> labels_send = new HashMap<>();
            labels_send.put("type", "SEND");
            req_send.setLabels(labels_send);
            req_send.setAmount(amount);
            req_send.setSource(handleSourceAddress);
            req_send.setSymbol("$tin");
            req_send.setTarget(handleTarget);
            CreateActionResponse action_send = null;
            action_send = createAction(req_send);//call api
            String action_id_send =  (String) action_send.get("action_id");
            System.out.println(action_id_send);
            //GenericResponse genericResponse_send = signAction(action_id_send);
            //sms
            ErrorResponse sendSms = sendSms(handleTarget, smsMessage);
            if (sendSms != null){
                if (sendSms.getError().getCode() == 0){
                   //notify target user to download transfer
                    return (String) action_send.get("action_id");
                }                
            }
            return null;
        } catch (ApiException e) {
            System.out.println("e.getResponseBody()");
            System.out.println(e.getResponseBody());
            return null;
        }
   }

public String createTransfer(String handleTarget, 
                             String handleSourceAddress,
                             String amount,
                             String smsMessage){
        //read the action 
        try {
            //upload
            CreateActionRequest req_upload = new CreateActionRequest();
            Map<String, Object> labels_upload = new HashMap<>();
            labels_upload.put("type", "UPLOAD");
            req_upload.setLabels(labels_upload);
            req_upload.setAmount(amount);
            req_upload.setSource(this.bankLimitAccountSigner);
            req_upload.setSymbol("$tin");
            req_upload.setTarget(handleSourceAddress);
            CreateActionResponse action_upload = null;
            action_upload = createAction(req_upload);//call to aPi
            String action_id_upload =  (String) action_upload.get("action_id");
            System.out.println(action_id_upload);
            //sign upload action with amount from bank to target address 
            GenericResponse genericResponse_upload = signAction(action_id_upload);
            System.out.println(genericResponse_upload);
            //send
            CreateActionRequest req_send = new CreateActionRequest();
            Map<String, Object> labels_send = new HashMap<>();
            labels_send.put("type", "SEND");
            req_send.setLabels(labels_send);
            req_send.setAmount(amount);
            req_send.setSource(handleSourceAddress);
            req_send.setSymbol("$tin");
            req_send.setTarget(handleTarget);
            CreateActionResponse action_send = null;
            action_send = createAction(req_send);//call api
            String action_id_send =  (String) action_send.get("action_id");
            System.out.println(action_id_send);
            GenericResponse genericResponse_send = signAction(action_id_send);
            //sms
            ErrorResponse sendSms = sendSms(handleTarget, smsMessage);
            if (sendSms != null){
                if (sendSms.getError().getCode() == 0){
                   //TODO: notify bank with credit download endpoint
//                    notifyBankToDownload(handleSourceAddress, action_id_send );
                    return (String) genericResponse_send.get("action_id");
                }                
            }
            return null;
        } catch (ApiException e) {
            System.out.println("e.getResponseBody()");
            System.out.println(e.getResponseBody());
            return null;
//        } catch (ExceptionResponseTinApi exceptionResponseTinApi) {
//            exceptionResponseTinApi.printStackTrace();
//            return null;
        }
}

public String confirmTransfer(String handleTargetAddress, 
                                String actionRequestId
                                ){
    try {
            GenericResponse actionResponse = getAction(actionRequestId);
            //get amount data from action
            String amount =  (String) actionResponse.get("amount");
            //create upload action with amount from bank to target address 
            CreateActionRequest req = new CreateActionRequest();
            Map<String, Object> labels = new HashMap<>();
            labels.put("type", "DOWNLOAD");
            req.setLabels(labels);
            req.setAmount(amount);
            req.setSource(handleTargetAddress);
            req.setSymbol("$tin");
            req.setTarget(this.bankLimitAccountSigner);
            CreateActionResponse action = null;
            action = createAction(req);
            String action_id =  (String) action.get("action_id");
            System.out.println(action_id);
            //sign upload action with amount from bank to target address 
            GenericResponse genericResponse_download = signAction(action_id);
            //TODO: notify bank status endpoint source
            String action_id_final = (String) genericResponse_download.get("action_id");
//            notifyStatusToBank(handleTargetAddress, action_id_final);
            return action_id_final;
        } catch (ApiException e) {
            System.out.println("e.getResponseBody()");
            System.out.println(e.getResponseBody());
            return null;
//        } catch (ExceptionResponseTinApi exceptionResponseTinApi) {
//            exceptionResponseTinApi.printStackTrace();
//            return null;
        }
}    

    public ErrorResponse sendSms(String solicitadoAlias, String message){
        WalletApi walletApi = new WalletApi();
        walletApi.getApiClient().setBasePath(url);

        try {
            SmsRequest req = new SmsRequest();
            req.setMessage(message);
            return walletApi.sendSms(apiKey, solicitadoAlias , req);
        } catch (ApiException e) {
            return null;
        }
    }

    public void notifyBankToDownload(String solicitanteaddress, String actionid) throws ExceptionResponseTinApi {
        SignerApi signerApi = new SignerApi();
        signerApi.getApiClient().setBasePath(url);

        try {
            ErrorResponse errorResponse = signerApi.notifySourceBank(apiKey, solicitanteaddress, actionid);
            System.out.println(errorResponse);
        } catch (ApiException e) {
            System.out.println(e.getResponseBody());
            throw new ExceptionResponseTinApi(e.getCode(), e.getMessage());
        }
    }

    public void rejectTransferSend(String addressForNotification,String actionId) throws ExceptionResponseTinApi {
        ActionApi  api = new ActionApi();
        api.getApiClient().setBasePath(url);
        try {
            LabelsStatusRequest labels = new LabelsStatusRequest();
            Map<String, Object> maps = new HashMap<>();
            maps.put("status", "REJECTED");
            labels.setLabels(maps);
            GenericResponse genericResponse = api.updateActionLabels(apiKey, actionId, labels);
            System.out.println(genericResponse);
//            notifyBankToDownload(addressForNotification, actionId);
            //TODO notify status reject to solicitado
        } catch (ApiException e) {
            System.out.println(e.getResponseBody());
            throw new ExceptionResponseTinApi(e.getCode(), e.getMessage());
//        } catch (ExceptionResponseTinApi exceptionResponseTinApi) {
//            exceptionResponseTinApi.printStackTrace();
        }
    }

    public void rejectTransferRequest(String addressForNotification, String actionId) throws ExceptionResponseTinApi{
        ActionApi  api = new ActionApi();
        api.getApiClient().setBasePath(url);
        try {
            LabelsStatusRequest labels = new LabelsStatusRequest();
            Map<String, Object> maps = new HashMap<>();
            maps.put("status", "REJECTED");
            labels.setLabels(maps);
            GenericResponse genericResponse = api.updateActionLabels(apiKey, actionId, labels);
            System.out.println(genericResponse);
//            notifyStatusToBank(addressForNotification, actionId);
            //TODO notify status reject to solicitado
        } catch (ApiException e) {
            System.out.println(e.getResponseBody());
            throw new ExceptionResponseTinApi(e.getCode(), e.getMessage());
        }
    }

    public void notifyStatusToBank(String solicitanteAddress, String actionId) throws ExceptionResponseTinApi {
        SignerApi signerApi = new SignerApi();
        signerApi.getApiClient().setBasePath(url);

        try {
            ErrorResponse errorResponse = signerApi.notifyStatusToBank(apiKey,solicitanteAddress, actionId);
            System.out.println(errorResponse);
        } catch (ApiException e) {
            System.out.println(e.getResponseBody());
            throw new ExceptionResponseTinApi(e.getCode(), e.getMessage());
        }
    }



    public ErrorResponse createLink(String source, String target, CreateLinkRequest.TypeEnum typeLink) throws ExceptionResponseTinApi {
        LinksApi api = new LinksApi();
        api.getApiClient().setBasePath(url);

        CreateLinkRequest req = new CreateLinkRequest();
        req.setSource(source);
        req.setTarget(target);
        req.setType(typeLink);
        try {
            ErrorResponse link = api.createLink(apiKey, req);
            return link;
        } catch (ApiException e) {
            throw new ExceptionResponseTinApi(e.getCode(), e.getMessage());
        }
    }

    public GetLinkResponse getLink(String source, String target) throws ExceptionResponseTinApi {
        LinksApi api = new LinksApi();
        api.getApiClient().setBasePath(url);
        try {
            return api.getLink(apiKey, source, target);
        } catch (ApiException e) {
            throw new ExceptionResponseTinApi(e.getCode(), e.getMessage());
        }
    }


    public PendingActionResponse getActionPendings(String alias, AliasType aliasType, ActionType action) {
        ActionApi actionApi = new ActionApi();
        actionApi.getApiClient().setBasePath(url);
        try {
            if (aliasType.getValue().equals(AliasType.SOURCE.getValue())){
                return actionApi.getPending(apiKey, action.getValue(), null, alias);
            }else {
                return actionApi.getPending(apiKey, action.getValue(), alias, null);
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }



}
