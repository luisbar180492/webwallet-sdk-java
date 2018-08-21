package com.minka.wallet.primitives.utils;

import com.google.gson.Gson;
import com.minka.ExceptionResponseTinApi;
import com.minka.api.handler.*;
import com.minka.api.model.*;
import com.minka.utils.Constants;
import java.util.HashMap;

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
            //TODO: notify bank
            return (String) genericResponse_download.get("action_id");
        } catch (ApiException e) {
            System.out.println("e.getResponseBody()");
            System.out.println(e.getResponseBody());
            return null;
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
            //TODO: notify bank
            return (String) genericResponse_send.get("action_id");
        } catch (ApiException e) {
            System.out.println("e.getResponseBody()");
            System.out.println(e.getResponseBody());
            return null;
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

    public void notifyBank(String solicitanteaddress, String actionid) throws ExceptionResponseTinApi {
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
            notifyBank(addressForNotification, actionId);
        } catch (ApiException e) {
            System.out.println(e.getResponseBody());
            throw new ExceptionResponseTinApi(e.getCode(), e.getMessage());
        }
    }

}
