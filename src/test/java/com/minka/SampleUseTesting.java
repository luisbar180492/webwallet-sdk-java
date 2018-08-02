package com.minka;

import com.minka.wallet.IOU;
import com.minka.wallet.MissingRequiredParameterIOUCreation;
import com.minka.wallet.primitives.KeyPair;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.Keeper;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SampleUseTesting {

    private KeyPair sourceKeyPair;
    private KeyPair targetKeyPair;
    private String sourceAddress;
    private String targetAddress;

//    @Ignore
    @Test
    public void fullExample() throws DecoderException, NoSuchAlgorithmException, MissingRequiredParameterIOUCreation {
//
//        System.out.println("Generate cryptographic keys\n");
//        sourceKeyPair = Sdk.Keypair.generate();
//        targetKeyPair = Sdk.Keypair.generate();
//
//        System.out.println(sourceKeyPair.toJson());
//        System.out.println(targetKeyPair.toJson());
//
//        System.out.println("\nGenerate wallet addresses from public keys\n");
//
//        sourceAddress = Sdk.Address.generate(sourceKeyPair.getPublic()).encode();
//        targetAddress = Sdk.Address.generate(targetKeyPair.getPublic()).encode();
//
//        System.out.println("\nCreate signer\n");
//
//        Signer sourceSigner = new Signer(sourceAddress, sourceKeyPair);
//
//        System.out.println("\nSOURCE address\n");
//        System.out.println(sourceAddress);
//        System.out.println("\nTARGET address\n");
//        System.out.println(targetAddress);
//
//        List<Signer> signers = new ArrayList<>();
//        signers.add(sourceSigner);
//
//        Date today = new Date();
//        Date tomorrow = DateUtils.addDays(today, 1);
//
//        Claim claim = new Claim()
//                .setDomain("wallet.example.com")
//                .setSource(sourceAddress)
//                .setTarget(targetAddress)
//                .setAmount("100")
//                .setSymbol(sourceAddress)
//                .setExpiry(IouUtil.convertToIsoFormat(tomorrow));
//
//        IOU iou = Sdk.IOU.write(claim).sign(signers);
//
//        System.out.println(iou.toPrettyJson()  );
//



//        DefaultApi api = new DefaultApi();
//        ApiClient apiclient = new ApiClient();
//        apiclient.setBasePath("https://test.sintesis.com.bo/SintesisIntegradoRest/integrado/");
//        api.setApiClient(apiclient);
//        SessionRequest sessionRequest = new SessionRequest();
//        sessionRequest.setOrigenTransaccion("APWS");
//        sessionRequest.setUsuario("iwac");
//        sessionRequest.setPassword("Ivan12345");
//        try {
//            WSInicio wsInicio = api.iniciarSesion(sessionRequest);
//
//            System.out.println(wsInicio.toString());
//
//        } catch (ApiException e) {
//            e.printStackTrace();
//        }

        DefaultApi api = new DefaultApi();
        ApiClient apiclient = new ApiClient();
        apiclient.setBasePath("https://achtin-tst.minka.io/v1/");
        api.setApiClient(apiclient);
        try {
            String apikey = "5b481fc2ae177010e197026b39c58cdb000f4c3897e841714e82c84c";
            Keeper keeper = api.obtenerKeeper(apikey);

            System.out.println(keeper.toString());
        } catch (ApiException e) {
            System.out.println("EXCEPTION");

            e.printStackTrace();
        }


    }


}
