package com.minka.sdk;

import com.minka.ExceptionResponseTinApi;
import com.minka.wallet.primitives.utils.SdkApiClient;
import io.minka.api.handler.ApiException;
import io.minka.api.model.LinkItem;
import io.minka.api.model.ListLinks;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class LinksTesting {

    SdkApiClient sdkApiClient;

    @Before
    public void prepare() throws ApiException {

        sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN_TESTING,
                TestingConstants.API_KEY,TestingConstants.TESTING_BASE);

        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);
        if (TestingConstants.proxy){
            sdkApiClient.setProxy(TestingConstants.PROXY_HOST, TestingConstants.PROXY_PORT);
        }
    }

    @Ignore
    @Test
    public void shouldCreateLink(){

        String source = "$5731445678";
        String target = "$573207246903";
        try {
            LinkItem link = sdkApiClient.createLink(source, target,
                    io.minka.api.model.CreateLinkRequest.TypeEnum.TRUST);
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
            LinkItem link = sdkApiClient.getLink(source, target);
            System.out.println(link);
        } catch (ExceptionResponseTinApi exceptionResponseTinApi) {
            exceptionResponseTinApi.printStackTrace();
        }
    }

    @Ignore
    @Test
    public void shouldGetAllLinks(){
        try {
            ListLinks links = sdkApiClient.getLinks(null,  "$targetPhone", "TRUST");
            System.out.println(links.size());
            for (LinkItem currLink:links) {
                System.out.println(currLink);
            }
        } catch (ExceptionResponseTinApi exceptionResponseTinApi) {
            exceptionResponseTinApi.printStackTrace();
        }
    }
}
