package com.minka.sdk;

import com.minka.ExceptionResponseTinApi;
import com.minka.api.model.CreateLinkRequest;
import com.minka.api.model.ErrorResponse;
import com.minka.api.model.GetLinkResponse;
import com.minka.wallet.primitives.utils.SdkApiClient;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class LinksTesting {

    SdkApiClient sdkApiClient;

    @Before
    public void prepare(){

        sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN,
                TestingConstants.API_KEY);

        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);

    }

    @Ignore
    @Test
    public void shouldCreateLink(){

        String source = "$auhxlvsouudtiailkmr";
        String target = "$yuhenntyycupgbubbha";
        try {
            ErrorResponse link = sdkApiClient.createLink(source, target, CreateLinkRequest.TypeEnum.TRUST);
            System.out.println(link);
        } catch (ExceptionResponseTinApi exceptionResponseTinApi) {
            exceptionResponseTinApi.printStackTrace();
        }
    }

    @Test
    public void shouldGetLink(){

        String source = "$auhxlvsouudtiailkmr";
        String target = "$yuhenntyycupgbubbha";
        try {
            GetLinkResponse link = sdkApiClient.getLink(source, target);
            System.out.println(link);
        } catch (ExceptionResponseTinApi exceptionResponseTinApi) {
            exceptionResponseTinApi.printStackTrace();
        }
    }
}
