package com.tcc.easymeal.helper;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tcc.easymeal.activity.ComercianteActivity;
import com.tcc.easymeal.activity.PedidosActivity;
import com.tcc.easymeal.config.ConfiguracaoFirebase;
import com.tcc.easymeal.model.Comerciante;


/**
 * Created by jamiltondamasceno
 */

public class UsuarioFirebase {

    public static Comerciante comerciante;

    public static FirebaseUser getUsuarioAtual(){
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseInstance();
        return usuario.getCurrentUser();
    }

    public static Comerciante getDadosUsuarioLogado(){

        FirebaseUser firebaseUser = getUsuarioAtual();

        Comerciante comerciante = new Comerciante();
        comerciante.setUid( firebaseUser.getUid() );
        comerciante.setEmail( firebaseUser.getEmail() );
        comerciante.setNome( firebaseUser.getDisplayName());

        return comerciante;

    }

    public static boolean atualizarNomeUsuario(String nome){

        try {

            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName( nome )
                    .build();
            user.updateProfile( profile ).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if( !task.isSuccessful() ){
                        Log.d("Perfil", "Erro ao atualizar nome de perfil.");
                    }
                }
            });

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }



    public static void redirecionaUsuarioLogado(final Activity activity){

        FirebaseUser user = getUsuarioAtual();
        if(user != null ){
            Log.d("resultado", "onDataChange: " + getIdentificadorUsuario());
            DatabaseReference usuariosRef = ConfiguracaoFirebase.getFirebaseDatabase()
                    .child("usuarios")
                    .child( getIdentificadorUsuario() );
            usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("resultado", "onDataChange: " + dataSnapshot.toString() );
                    comerciante = dataSnapshot.getValue( Comerciante.class );


                        Intent i = new Intent(activity, ComercianteActivity.class);
                        activity.startActivity(i);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    public static String getIdentificadorUsuario(){
        return getUsuarioAtual().getUid();
    }

}

