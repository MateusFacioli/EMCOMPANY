package com.tcc.easymeal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tcc.easymeal.R;

public class LoginActivity extends AppCompatActivity {

    //layout
    private TextInputEditText inputLoginUsuario;
    private TextInputEditText inputLoginSenha;
    private Button btnLogar;


    //firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        getSupportActionBar().hide();
        inicializarComponentes();


        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logarComFirebase(inputLoginUsuario.getText().toString(), inputLoginSenha.getText().toString());
            }
        });


    }

    private void logarComFirebase(String email, String senha){
        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TagLoginCerto", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent mapa = new Intent(LoginActivity.this, ComercianteActivity.class);
                            startActivity(mapa);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TagLoginErrado", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            

                        }

                        // ...
                    }
                });
    }

    private void inicializarComponentes(){
        inputLoginUsuario = findViewById(R.id.inputLoginUsuario);
        inputLoginSenha = findViewById(R.id.inputLoginSenha);
        btnLogar = findViewById(R.id.btnLogar);
        mAuth = FirebaseAuth.getInstance();

    }
}
