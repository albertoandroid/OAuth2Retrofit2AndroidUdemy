package com.androiddesdecero.oauth2.ui;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.androiddesdecero.oauth2.R;
import com.androiddesdecero.oauth2.api.WebServiceOAuth;
import com.androiddesdecero.oauth2.api.WebServiceOAuthApi;
import com.androiddesdecero.oauth2.model.MovimientoBancario;
import com.androiddesdecero.oauth2.model.Token;
import com.androiddesdecero.oauth2.model.User;
import com.androiddesdecero.oauth2.shared_pref.TokenManager;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.androiddesdecero.oauth2.shared_pref.TokenManager.SHARED_PREFERENCES;

public class LogeadoActivity extends AppCompatActivity {

    private Button btVerTodosLosMovimientosBancarios;
    private Button btVerTodosLosMovimientosBancariosUser;
    private TokenManager tokenManager;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logeado);
        setUpView();
        activity = this;
    }

    private void setUpView(){
        tokenManager = TokenManager.getIntance(getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE));

        btVerTodosLosMovimientosBancarios = findViewById(R.id.btVerTodosLosMovimientosBancarios);
        btVerTodosLosMovimientosBancarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verTodosMovimientos();
            }
        });


        btVerTodosLosMovimientosBancariosUser = findViewById(R.id.btVerTodosLosMovimientosBancariosUser);

        btVerTodosLosMovimientosBancariosUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verTodosMovimientosUser();
            }
        });
    }

    private void verTodosMovimientos(){
        Call<List<MovimientoBancario>> call = WebServiceOAuth
                .getInstance()
                .createService(WebServiceOAuthApi.class)
                .obtenerMovimientos("Bearer " + tokenManager.getToken().getAccessToken());

        call.enqueue(new Callback<List<MovimientoBancario>>() {
            @Override
            public void onResponse(Call<List<MovimientoBancario>> call, Response<List<MovimientoBancario>> response) {
                if(response.code()==200){
                    for(int i=0; i<response.body().size(); i++){
                        Log.d("TAG1", "UserID: " + response.body().get(i).getUserID() +
                                " Importe: " + response.body().get(i).getImporte() +
                                " Nombre: " +response.body().get(i).getName());
                    }
                }else {
                    Log.d("TAG1", "Error");
                }
            }

            @Override
            public void onFailure(Call<List<MovimientoBancario>> call, Throwable t) {

            }
        });
    }

    private void verTodosMovimientosUser(){
        User user = new User();
        user.setId(3l);
        Call<List<MovimientoBancario>> call = WebServiceOAuth
                .getInstance()
                .createServiceWithOAuth2(WebServiceOAuthApi.class, tokenManager)
                .obtenerMovimientosUser(user);

        call.enqueue(new Callback<List<MovimientoBancario>>() {
            @Override
            public void onResponse(Call<List<MovimientoBancario>> call, Response<List<MovimientoBancario>> response) {
                if(response.code() == 200){
                    for(int i=00; i<response.body().size(); i++){
                        Log.d("TAG1",
                                "UserID: " + response.body().get(i).getUserID() +
                                " Importe: " + response.body().get(i).getImporte() +
                                " Nombre: " +response.body().get(i).getName());
                    }
                }else if(response.code() == 404){
                    Log.d("TAG1", "No hay movimientos");
                }else if(response.code() == 401){
                    Token newToken = new Token();
                    newToken.setRefreshToken("");
                    newToken.setRefreshToken("");
                    tokenManager.saveToken(newToken);
                    try{
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Log.d("TAG1", "Invalid Access Token: " + jsonObject.getString("error"));
                    } catch (Exception e){
                        Log.d("TAG1", "Invalid Access Token: " + e.getMessage());
                    }

                    activity.finish();
                } else{
                    Log.d("TAG1", "Error");
                }
            }

            @Override
            public void onFailure(Call<List<MovimientoBancario>> call, Throwable t) {

            }
        });
    }
}
