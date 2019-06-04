package com.tcc.easymeal.model;

import com.google.firebase.database.DatabaseReference;
import com.tcc.easymeal.config.ConfiguracaoFirebase;
import com.tcc.easymeal.helper.UsuarioFirebase;

public class Localizacao {


    private Double latitude;
    private Double longitude;

    public Localizacao() {
    }

    public void salvar(){
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference reference = database
                .child("comerciante")
                .child(UsuarioFirebase
                        .getDadosUsuarioLogado()
                        .getUid())
                .child("localizacao");

        reference.setValue(this);
    }

    public void remover(){
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference reference = database
                .child("comerciante")
                .child(UsuarioFirebase
                        .getDadosUsuarioLogado()
                        .getUid())
                .child("localizacao");

        reference.removeValue();
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }


}
