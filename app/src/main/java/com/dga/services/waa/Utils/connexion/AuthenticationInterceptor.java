package com.dga.services.waa.Utils.connexion;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {
    Context context;
    private String authToken;

    public AuthenticationInterceptor(String token, Context context) {
        this.authToken = token;
        this.context = context;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder()
                .header("Authorization", authToken);
        // .header("Authorization", "Bearer " );

        Request request = builder.build();
        return chain.proceed(request);
    }
}
