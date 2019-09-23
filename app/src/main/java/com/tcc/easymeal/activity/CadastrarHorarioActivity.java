package com.tcc.easymeal.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.tcc.easymeal.R;
import com.tcc.easymeal.model.Cardapio;
import com.tcc.easymeal.model.Horario;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import afu.org.checkerframework.checker.units.qual.Time;

public class CadastrarHorarioActivity extends AppCompatActivity {

    private TextView txtTime;
    private TextView txtTime2;
    private CalendarView calendarView;
    private FloatingActionButton btnAdicionarHorario;
    private Snackbar snackbar;
    private List<Horario> horarios = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_cadastrar_horario);
        inicializarComponenetes();
        super.onCreate(savedInstanceState);

        btnAdicionarHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Horario horario1 = new Horario();
                Horario horario2 =new Horario();
                horario1.setTime(txtTime.getText().toString());
                horario2.setTime(txtTime2.getText().toString());
                horario1.setDate(calendarView.getDate());
                horario2.setDate(calendarView.getDate());
                String h1 =horario1.getTime();
                String h2 =horario2.getTime();
                Calendar dt = horario1.getDate();
                //horarios.add(horario1);
                //horarios.iterator(h1);
                //horarios.iterator(h2);
             String texto= "Deseja cadastrar esse horário  "+h1+" até "+h2+" nessa data "+dt+" ? ";
             exibirMensagem(texto);



            }
        });

    }
    private void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT)
                .show();
    }

    private void inicializarComponenetes(){

        txtTime = findViewById(R.id.txtTime);
        txtTime2 = findViewById(R.id.txtTime2);
        btnAdicionarHorario = findViewById(R.id.btnHorarioAdicionar);
        calendarView = findViewById(R.id.calendarView);
    }
}
