package com.minka;

import com.minka.api.handler.*;
import com.minka.api.model.*;
import com.minka.api.model.Signer;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestClientSdk {

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

        System.out.println(api.getApiClient().getBasePath());

        try {
            SignerRequest signerReq = new SignerRequest();
            List<PublicKeys> keepers = new ArrayList<>();
            PublicKeys llave = new PublicKeys();
            llave.setPublic("041726541843d5c1924ad7dbfde8307cce6d96e9dfdab61759fb155b54581dc08c4e5575262a150e23ceffee70ce166000766bc477b61a8e8ea95be5c0b37b66ef");
            keepers.add(llave);
            signerReq.setKeeper(keepers);
            EmptyObject emptyObject = new EmptyObject();
            signerReq.setLabels(emptyObject);
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

    @Test
    public void createWallet(){

        WalletApi api = new WalletApi();
        String apikey = "5b481fc2ae177010e197026b39c58cdb000f4c3897e841714e82c84c";

        System.out.println(api.getApiClient().getBasePath());

        WalletRequest walletRe = new WalletRequest();
        walletRe.setHandle("$absds");
        Map<String, Object> labels = new HashMap<>();
        labels.put("email", "aranibarIvan@mgali.com");
        labels.put("phonEnumbier", 21721821);
        walletRe.setLabels(labels);
        try {
            WalletResponse wallet = api.createWallet(apikey, walletRe);

            System.out.println(wallet);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}
