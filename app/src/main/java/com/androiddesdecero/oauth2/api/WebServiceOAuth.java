package com.androiddesdecero.oauth2.api;

import com.androiddesdecero.oauth2.shared_pref.TokenManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by albertopalomarrobledo on 26/12/18.
 */

public class WebServiceOAuth {

    private static final String BASE_URL = "http://10.0.2.2:8071/";
    private static HttpLoggingInterceptor loggingInterceptor;
    private Retrofit retrofit;
    private OkHttpClient.Builder httpClientBuilder;
    private static WebServiceOAuth instance;

    private WebServiceOAuth(){
        httpClientBuilder = new OkHttpClient.Builder();
        loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized WebServiceOAuth getInstance(){
        if(instance == null){
            instance = new WebServiceOAuth();
        }
        return instance;
    }

    public <S> S createService(Class<S> serviceClass){
        return retrofit.create(serviceClass);
    }

    public <S> S createServiceWithOAuth2(Class<S> serviceClass, final TokenManager tokenManager){
        final OkHttpClient newClient = httpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request requestOriginal = chain.request();
                Request.Builder builder = requestOriginal.newBuilder();

                if(tokenManager.getToken().getAccessToken() != null){
                    builder.addHeader("Authorization", "Bearer " + tokenManager.getToken().getAccessToken());
                }

                Request request = builder.build();
                return chain.proceed(request);
            }
        }).authenticator(CustomAuthenticator.getInstance(tokenManager)).build();

        return retrofit.newBuilder().client(newClient).build().create(serviceClass);
    }
}
