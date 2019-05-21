package com.tcc.easymeal.model;

import com.google.firebase.database.DatabaseReference;
import com.tcc.easymeal.config.ConfiguracaoFirebase;
import com.tcc.easymeal.helper.UsuarioFirebase;

import java.io.Serializable;

public class Loja implements Serializable {

    private Double latitude;
    private Double longitude;
    private Comerciante comerciante;
    private Cardapio cardapio;


    public Loja() {
    }

    public void salvar(){

                DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
                DatabaseReference reference = database.child("loja").child(UsuarioFirebase.getDadosUsuarioLogado().getUid());

                reference.setValue(this);

            }

    public Cardapio getCardapio() {
        return cardapio;
    }

    public void setCardapio(Cardapio cardapio) {
        this.cardapio = cardapio;
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

    public Comerciante getComerciante() {
        return comerciante;
    }

    public void setComerciante(Comerciante comerciante) {
        this.comerciante = comerciante;
    }
}
