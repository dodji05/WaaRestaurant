package com.dga.services.waa.Utils.connexion;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;

public interface RestService {
    @GET("restaurants")
    Call<ResponseBody> getRestaurants(
            @HeaderMap Map<String, String> headers
    );
}
