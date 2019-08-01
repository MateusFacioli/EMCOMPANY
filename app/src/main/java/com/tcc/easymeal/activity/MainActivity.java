package com.tcc.easymeal.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.tcc.easymeal.R;
import com.tcc.easymeal.helper.UsuarioFirebase;
import com.tcc.easymeal.model.Permissoes;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    //layout
    private Button btnLogin;
    private Button btnCadastrar;
    private TextView texto;

    Animation aparece;
    Animation some;

    //Firebase
    private FirebaseAuth mAuth;

    //code
    public static int APP_REQUEST_CODE = 1;
    private String [] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().hide();
        Permissoes.validarPermissoes(permissoes,this, 1);
        inicializarComponenetes();
        UsuarioFirebase.redirecionaUsuarioLogado(MainActivity.this);

        texto =findViewById(R.id.texto_apresentacao);

        some = new AlphaAnimation(1,0);

        some.setDuration(2000);


        some.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                texto.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                texto.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                texto.startAnimation(some);
            }
        },2000);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logar = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(logar);
            }
        });


        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneLogin(v);

            }
        });


    }

    public void phoneLogin(final View view) {
        final Intent intent = new Intent(MainActivity.this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);

    }


    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
                Log.d("ErrorAccount", "Error:" + loginResult.getError());
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();


                } else {
                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0,10));


                }

                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.

                // Success! Start your next activity...
                //goToMyLoggedInActivity();
                Intent cadastrar = new Intent(MainActivity.this, CadastrarActivity.class);
                startActivity(cadastrar);



            }

            // Surface the result to your user in an appropriate way.
            Toast.makeText(
                                this,
                                toastMessage,
                                Toast.LENGTH_LONG)
                                .show();
        }
    }



    private void inicializarComponenetes(){
        FirebaseApp.initializeApp(this);
        btnLogin = findViewById(R.id.btnLogar);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        mAuth = FirebaseAuth.getInstance();
        AccountKit.initialize(getApplicationContext());

    }
}
