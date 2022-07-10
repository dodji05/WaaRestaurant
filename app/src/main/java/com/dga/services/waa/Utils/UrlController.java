package com.dga.services.waa.Utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.dga.services.waa.Utils.connexion.AuthenticationInterceptor;
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UrlController {
    public static boolean loading = false;
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new OkHttpProfilerInterceptor())
            .connectTimeout(60, TimeUnit.MINUTES)
            .writeTimeout(60, TimeUnit.MINUTES)
            .readTimeout(60, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .callTimeout(60, TimeUnit.MINUTES)


            // .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();



    public static String IP_ADDRESS = "https://waa-restao.herokuapp.com";//Enter You Ip_Address here here


    // Please don't change the below code without proper knowledge
    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .addInterceptor(new OkHttpProfilerInterceptor())
            ;
    public static String Base_URL = IP_ADDRESS + "/api/";
    private static final Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(Base_URL)
                    .client(okHttpClient)

                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    public static <S> S createService(
            Class<S> serviceClass, String username, String password, Context context) {
        if (!TextUtils.isEmpty(username)
                && !TextUtils.isEmpty(password)) {
            String authToken = Credentials.basic(username, password);
            return createService(serviceClass, authToken, context);
        } else {
            return createService(serviceClass);
        }
    }


    public static <S> S createServiceNoTimeout(Class<S> serviceClass, String authToken, Context context) {
        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor = new AuthenticationInterceptor(authToken, context);
            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);
                httpClient.connectTimeout(0, TimeUnit.MINUTES);
                httpClient.readTimeout(0, TimeUnit.SECONDS);
                httpClient.writeTimeout(0, TimeUnit.SECONDS);
                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }
        return retrofit.create(serviceClass);
    }

    public static <S> S createServiceNoTimeoutUP(
            Class<S> serviceClass, String username, String password, Context context) {
        if (!TextUtils.isEmpty(username)
                && !TextUtils.isEmpty(password)) {
            String authToken = Credentials.basic(username, password);
            return createServiceNoTimeout(serviceClass, authToken, context);
        }

        return createServiceNoTimeoutUP(serviceClass, null, null, context);
    }

    public static <S> S createService(
            Class<S> serviceClass, final String authToken, Context context) {
        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(authToken, context);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);

                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }
        return retrofit.create(serviceClass);
    }

    public static Map<String, String> AddHeaders(Context context) {
//        Log.d("AddHeaders", "AddHeaders: ");
        Map<String, String> map = new HashMap<>();

//        map.put("custom-security", Custom_Security);
//        map.put("OparaDeals-Lang-Locale", Configurations.getLanguageCode());
        map.put("wa-Request-From", "android");
        map.put("Content-Type", "application/json");
        map.put("Cache-Control", "max-age=10");
        map.put("Access-Control-Allow-Headers", "Authorization, Content-Disposition, Content-MD5, Content-Type");
        Log.d("AddHeaders-1", "AddHeaders: "+map.toString());

        return map;


    }

    public static Map<String, String> UploadImageAddHeaders(Context context) {
        Map<String, String> map = new HashMap<>();

//        map.put("OparaDeals-Lang-Locale", Configurations.getLanguageCode());
        map.put("wa-Request-From", "android");
        map.put("Cache-Control", "max-age=10");

        return map;
    }
}
