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
    public void prepare(){

        sdkApiClient = new SdkApiClient(TestingConstants.DOMAIN_TESTING,
                TestingConstants.API_KEY,TestingConstants.DEV_BASE);

        sdkApiClient
                .setSecret(TestingConstants.SECRET)
                .setClientId(TestingConstants.CLIENT_ID);
    }

    @Ignore
    @Test
    public void shouldCreateLink(){

        String source = "$573201902143";
        String target = "wbgWL7RJD6jBi989uS3qCHzriGwHUPxTrB";
        try {
            LinkItem link = sdkApiClient.createLink(source, target,
                    io.minka.api.model.CreateLinkRequest.TypeEnum.TRUST);
            System.out.println(link);
        } catch (ExceptionResponseTinApi exceptionResponseTinApi) {
            exceptionResponseTinApi.printStackTrace();
        }
    }

    @Ignore
    @Test
    public void shouldDeleteLink(){
        String linkId = "7887610f-a173-4829-b8a9-3237150a0e9b";
        try {
            LinkItem linkItem = sdkApiClient.deleteLink(linkId);
            System.out.println(linkItem);
        } catch (ApiException e) {
            System.out.println(e.getResponseBody());
        }
    }

    @Test
    public void shouldGetLink(){

        String source = "$5731445678";
        String target = "wP8fxVGVxvycgorSzCFnd9AsZkhfpPHFsN";
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
            ListLinks links = sdkApiClient.getLinks(null,  "$573210160251", "TRUST");
            System.out.println(links.size());
            for (LinkItem currLink:links) {
                System.out.println(currLink);
            }
        } catch (ExceptionResponseTinApi exceptionResponseTinApi) {
            exceptionResponseTinApi.printStackTrace();
        }
    }
}
