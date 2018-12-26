package com.androiddesdecero.oauth2.api;

import com.androiddesdecero.oauth2.model.MovimientoBancario;
import com.androiddesdecero.oauth2.model.Token;
import com.androiddesdecero.oauth2.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by albertopalomarrobledo on 26/12/18.
 */

public interface WebServiceOAuthApi {

    @GET("/api/users")
    Call<List<User>> obtenerUsuarios();

    @POST("/api/create_user")
    Call<Void> crearUsuario(@Body User user);

    @FormUrlEncoded
    @POST("/oauth/token")
    Call<Token> obtenerToken(
            @Header("Authorization") String authorization,
            @Field("username") String username,
            @Field("password") String password,
            @Field("grant_type") String grantType
    );

    @FormUrlEncoded
    @POST("/oauth/token")
    Call<Token> obtenerTokenconRefreshToken(
            @Header("Authorization") String authorization,
            @Field("refresh_token") String refreshToken,
            @Field("grant_type") String grantType
    );




    @GET("/api/oauth2/movimiento_bancario")
    Call<List<MovimientoBancario>> obtenerMovimientos(@Header("Authorization") String accessToken);

    @POST("/api/oauth2/movimiento_bancario_user")
    Call<List<MovimientoBancario>> obtenerMovimientosUser(@Body User user);
}
