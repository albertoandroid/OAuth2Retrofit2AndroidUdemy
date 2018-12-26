package com.androiddesdecero.oauth2.api;

import android.support.annotation.Nullable;
import android.util.Base64;

import com.androiddesdecero.oauth2.model.Token;
import com.androiddesdecero.oauth2.shared_pref.TokenManager;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;

/**
 * Created by albertopalomarrobledo on 26/12/18.
 */

public class CustomAuthenticator implements Authenticator {

    private TokenManager tokenManager;
    private static CustomAuthenticator INSTANCE;

    private CustomAuthenticator(TokenManager tokenManager){
        this.tokenManager = tokenManager;
    }

    public static synchronized CustomAuthenticator getInstance(TokenManager tokenManager){
        if(INSTANCE == null){
            INSTANCE = new CustomAuthenticator(tokenManager);
        }
        return INSTANCE;
    }

    /*
    El metodo authenticate se ejecuta cuando nuestro token de Acceso ha caducado y queremos obtener un nuevo token de Acceso con nuestro token de refresco.
    En caso que el token de refresco haya caducado nos deslogamos.
    */
    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, Response response) throws IOException {
        String authHeader = "Basic " + Base64.encodeToString(("androidApp:123").getBytes(),Base64.NO_WRAP);

        Token token = tokenManager.getToken();

        Call<Token> call = WebServiceOAuth
                .getInstance()
                .createService(WebServiceOAuthApi.class)
                .obtenerTokenconRefreshToken(
                        authHeader,
                        token.getRefreshToken(),
                        "refresh_token"
                );

        retrofit2.Response<Token> response1 = call.execute();
        if(response1.isSuccessful()){
            /*
            Si la respuesta es correcta, actualizamos el token de Acceso.
             */
            Token newToken = response1.body();
            tokenManager.saveToken(newToken);
            return response.request().newBuilder().header("Authorization", "Bearer " + response1.body().getAccessToken()).build();
        }else{
            /*
            Si la respuesta no es sucessfull, quiere decir que el token de refresco ha caducado
            y por lo tanto tendremos que deslogearnos de la aplicacion
            y pedir al usuario que vuelva a introducir sus credenciales.
             */
            return null;
        }
    }
}
