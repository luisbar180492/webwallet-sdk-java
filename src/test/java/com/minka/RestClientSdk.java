package com.minka;

import com.minka.api.handler.*;
import com.minka.api.model.*;
import com.minka.api.model.Signer;
import com.minka.wallet.primitives.utils.Claim;
import com.sun.java.browser.plugin2.DOM;
import org.junit.Ignore;
import org.junit.Test;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestClientSdk {

    static String DOMAIN_ACH = "achtin-tst.minka.io";
    static String CLOUD_URL = "https://achtin-tst.minka.io/v1";
    static String API_KEY = "5b481fc2ae177010e197026b9778ac575c36480a918674e7a76d8037";
//    static String ANDRES_URL = "http://192.168.120.173:8080/v1";

    @Ignore
    @Test
    public void obtenerKeeper(){
        KeeperApi api = new KeeperApi();

        System.out.println(api.getApiClient().getBasePath());
        try {
            String apikey = "5b481fc2ae177010e197026b39c58cdb000f4c3897e841714e82c84c";
            Keeper keeper = api.obtenerKeeper(apikey);

            System.out.println(keeper.toString());
        } catch (ApiException e) {
            System.out.println("EXCEPTION");

            e.printStackTrace();
        }
    }

    @Test
    public void createSigner(){
        SignerApi api = new SignerApi();
        String apikey = "5b481fc2ae177010e197026b39c58cdb000f4c3897e841714e82c84c";

        api.getApiClient().setBasePath(CLOUD_URL);
        System.out.println(api.getApiClient().getBasePath());

        try {
            SignerRequest signerReq = new SignerRequest();
            List<PublicKeys> keepers = new ArrayList<>();
            PublicKeys llave = new PublicKeys();
            llave.setPublic("041726541843d5c1924ad7dbfde8307cce6d96e9dfdab61759fb155b54581dc08c4e5575262a150e23ceffee70ce166000766bc477b61a8e8ea95be5c0b37b66ef");
            keepers.add(llave);
            signerReq.setKeeper(keepers);
            Map<String, Object> labelsSigner = new HashMap<>();
            labelsSigner.put("bankAccountNumber",new Integer(2121211));
            labelsSigner.put("typeAccount", "savings");
            signerReq.setLabels(labelsSigner);
            System.out.println(signerReq);

            SignerResponse signer = api.createSigner(signerReq, apikey);

            System.out.println(signer);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getSigners(){

        SignerApi api = new SignerApi();
        System.out.println(api.getApiClient().getBasePath());
        try {
            String apikey = "5b481fc2ae177010e197026b39c58cdb000f4c3897e841714e82c84c";
            SignerListResponse signers = api.getSigner(apikey);
            int size = signers.size();
            System.out.println("size");
            System.out.println(size);

            for (int i = 0; i < size; i++) {
                SignerResponse signerResponse = signers.get(i);
                System.out.println(signerResponse);

            }

        } catch (ApiException e) {
            System.out.println("EXCEPTION");

            e.printStackTrace();
        }
    }

    @Ignore
    @Test
    public void createWallet(){

        WalletApi walletApi = new WalletApi();
        String apikey = "5b481fc2ae177010e197026b39c58cdb000f4c3897e841714e82c84c";

        walletApi.getApiClient().setBasePath(CLOUD_URL);
        System.out.println("wallet url");

        System.out.println(walletApi.getApiClient().getBasePath());

        WalletRequest walletRe = new WalletRequest();
        String handle = "$abc";
        walletRe.setHandle(handle);
        Map<String, Object> walletLabels = new HashMap<>();
        walletLabels.put("email", "aranibarIvan@mgali.com");
        walletLabels.put("phoneNumber", new Integer(21721821));
        walletRe.setLabels(walletLabels);
        try {
            WalletResponse wallet = walletApi.createWallet(apikey, walletRe);
            System.out.println("wallet created\n");
            System.out.println(wallet);

            KeeperApi keeperApi = new KeeperApi();
            keeperApi.getApiClient().setBasePath(CLOUD_URL);
            Keeper keeper = keeperApi.obtenerKeeper(apikey);
            System.out.println("keeper created\n");
            System.out.println(keeper);

            SignerApi signerApi = new SignerApi();
            signerApi.getApiClient().setBasePath(CLOUD_URL);
            SignerRequest signerREq= new SignerRequest();
            Map<String, Object> labelsSigner = new HashMap<>();
            labelsSigner.put("bankAccountNumber",new Integer(21212113));
            labelsSigner.put("typeAccount", "savings");
            signerREq.setLabels(labelsSigner);
            List<PublicKeys> llaves = new ArrayList<>();
            PublicKeys aPublickKey = new PublicKeys();
            aPublickKey.setPublic(keeper.getPublic());
            llaves.add(aPublickKey);
            signerREq.setKeeper(llaves);

            SignerResponse signer = signerApi.createSigner(signerREq, apikey);
            System.out.println("Signer created\n");
            System.out.println(signer);


            System.out.println("UPDATE BASE BATH\n");

            System.out.println(walletApi.getApiClient().getBasePath());

            WalletUpdateRequest updateWalletReq = new WalletUpdateRequest();
            updateWalletReq.setDefault(keeper.getPublic());
            List<String> listaSigners = new ArrayList<>();
            listaSigners.add(keeper.getPublic());
            updateWalletReq.setSigner(listaSigners);
            WalletUpdateResponse walletUpdateResponse = walletApi.updateWallet(apikey, handle, updateWalletReq);

            System.out.println(walletUpdateResponse);

        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateWallet(){

        //CREATE WALLET

        WalletApi api = new WalletApi();
        String apikey = "5b481fc2ae177010e197026b39c58cdb000f4c3897e841714e82c84c";

        System.out.println(api.getApiClient().getBasePath());

        WalletUpdateRequest updateWaletReq = new WalletUpdateRequest();
//        updateWaletReq.setDefault();
//
//        api.updateWallet(apikey, "@tina1", updateWaletReq);
//        WalletRequest walletRe = new WalletRequest();
//        walletRe.setHandle("$absds");
//        Map<String, Object> labels = new HashMap<>();
//        labels.put("email", "aranibarIvan@mgali.com");
//        labels.put("phonEnumbier", 21721821);
//        walletRe.setLabels(labels);
//        try {
//            WalletResponse wallet = api.createWallet(apikey, walletRe);
//
//            System.out.println(wallet);
//        } catch (ApiException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void createKlemer() throws ApiException {
        CreateClaimResponse response = getCreateClaimResponse();

        System.out.println(response);
    }

    private CreateClaimResponse getCreateClaimResponse() throws ApiException {
        KlemerApi klemerApi = new KlemerApi();

        klemerApi.getApiClient().setBasePath(CLOUD_URL);
        CreateClaimRequest claim = new CreateClaimRequest();

        claim.setSource("$abcd8");
        claim.setTarget("$abcd7");
        claim.setSymbol("$abcd8");
        claim.setAmount("100");
        return klemerApi.createClaim(API_KEY, claim);
    }

    @Test
    public void signIouLocally() throws ApiException {
        CreateClaimResponse createClaimResponse = getCreateClaimResponse();
        Claims claims = createClaimResponse.getClaims();
        Claim claim = new Claim()
                .setDomain(DOMAIN_ACH)
                .setAmount(claims.getAmount())
                .setExpiry(claims.getExpiry())
                .setSource(claims.getSource())
                .setSymbol(claims.getSymbol())
                .setTarget(claims.getTarget());



    }

}
