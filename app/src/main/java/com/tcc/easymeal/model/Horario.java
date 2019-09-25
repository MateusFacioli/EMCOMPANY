package com.tcc.easymeal.model;

import com.google.firebase.database.DatabaseReference;
import com.tcc.easymeal.config.ConfiguracaoFirebase;
import com.tcc.easymeal.helper.UsuarioFirebase;

public class Horario {

    private String date;
    private String timeInicio;
    private String timeFinal;
    private String latitude;
    private String longitude;

    private static Horario horario;

    public Horario(){


    }

    public void salvar(){
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference reference = database
                .child("comerciante")
                .child(UsuarioFirebase.getUsuarioAtual().getUid())
                .child("reserva");

        reference.setValue(this);

    }


    public static Horario getHorario() {
        return horario;
    }

    public static void setHorario(Horario horario) {
        Horario.horario = horario;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeInicio() {
        return timeInicio;
    }

    public void setTimeInicio(String timeInicio) {
        this.timeInicio = timeInicio;
    }

    public String getTimeFinal() {
        return timeFinal;
    }

    public void setTimeFinal(String timeFinal) {
        this.timeFinal = timeFinal;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
