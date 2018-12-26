package com.androiddesdecero.oauth2.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.androiddesdecero.oauth2.R;
import com.androiddesdecero.oauth2.api.WebServiceOAuth;
import com.androiddesdecero.oauth2.api.WebServiceOAuthApi;
import com.androiddesdecero.oauth2.model.MovimientoBancario;
import com.androiddesdecero.oauth2.model.Token;
import com.androiddesdecero.oauth2.model.User;
import com.androiddesdecero.oauth2.shared_pref.TokenManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.androiddesdecero.oauth2.shared_pref.TokenManager.SHARED_PREFERENCES;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btObtenerToken;
    private Button btCrearUsuario;
    private Button btVerTodosUsuarios;
    private Button btVerTodosLosMovimientosBancarios;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpView();
    }

    private void setUpView(){
        tokenManager = TokenManager.getIntance(getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE));
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btObtenerToken = findViewById(R.id.btObtenerToken);
        btObtenerToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerToken();
            }
        });


        btCrearUsuario = findViewById(R.id.btCrearUsuario);
        btCrearUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearUsuario();
            }
        });


        btVerTodosUsuarios = findViewById(R.id.btVerTodosUsuarios);
        btVerTodosUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verTodosUsuarios();
            }
        });


        btVerTodosLosMovimientosBancarios = findViewById(R.id.btVerTodosLosMovimientosBancarios);
        btVerTodosLosMovimientosBancarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verMovimientosBancarios();
            }
        });
    }

    private void verMovimientosBancarios(){
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

    private void obtenerToken(){
        String authHeader = "Basic " + Base64.encodeToString(("androidApp:123").getBytes(), Base64.NO_WRAP);
        Call<Token> call = WebServiceOAuth
                .getInstance()
                .createService(WebServiceOAuthApi.class)
                .obtenerToken(
                        authHeader,
                        etUsername.getText().toString(),
                        etPassword.getText().toString(),
                        "password"
                );

        call.enqueue(new Callback<Token>() {
            Token token = new Token();
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if(response.code()==200){
                    Log.d("TAG1", "Access Token: " + response.body().getAccessToken()
                    +" Refresh Token: " + response.body().getRefreshToken());

                    token = response.body();
                    tokenManager.saveToken(token);
                    //TODO start new Activity
                    startActivity(new Intent(getApplicationContext(), LogeadoActivity.class));
                }else{
                    Log.d("TAG1", "Error");
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {

            }
        });
    }

    private void crearUsuario(){
        User user = new User();
        user.setPassword(etPassword.getText().toString());
        user.setUsername(etUsername.getText().toString());
        Call<Void> call = WebServiceOAuth
                .getInstance()
                .createService(WebServiceOAuthApi.class)
                .crearUsuario(user);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 201){
                    Log.d("TAG1", "Creado Usuario Correctamente");
                }else{
                    Log.d("TAG1", "Error");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void verTodosUsuarios(){
        Call<List<User>> call = WebServiceOAuth
                .getInstance()
                .createService(WebServiceOAuthApi.class)
                .obtenerUsuarios();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.code() == 200){
                    for(int i=0; i<response.body().size(); i++){
                        Log.d("TAG1", "Username: " + response.body().get(i).getUsername());
                    }
                }else{
                    Log.d("TAG1", "Error");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }


}
