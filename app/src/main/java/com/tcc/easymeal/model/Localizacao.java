package com.tcc.easymeal.model;

import com.google.firebase.database.DatabaseReference;
import com.tcc.easymeal.config.ConfiguracaoFirebase;

public class Localizacao {

    private Double latitude;
    private Double longitude;
    private Comerciante comerciante;


    public Localizacao() {
    }
    public void salvar(){
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference reference =  database.child("localizacao").child(comerciante.getCpf());

        reference.setValue(this);
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
