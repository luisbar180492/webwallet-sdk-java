package com.minka;

import com.minka.api.handler.ApiClient;
import com.minka.api.handler.ApiException;
import com.minka.api.handler.DefaultApi;
import com.minka.api.model.Keeper;
import org.junit.Test;

public class RestClientSdk {

    @Test
    public void obtenerKeeper(){
        DefaultApi api = new DefaultApi();
        ApiClient apiclient = new ApiClient();
        api.setApiClient(apiclient);

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
}
