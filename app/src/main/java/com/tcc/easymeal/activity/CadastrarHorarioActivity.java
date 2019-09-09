package com.tcc.easymeal.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.tcc.easymeal.R;
import com.tcc.easymeal.model.Horario;

import afu.org.checkerframework.checker.units.qual.Time;

public class CadastrarHorarioActivity extends AppCompatActivity {

    private TextView txtTime;
    private CalendarView calendarView;
    private Button btnAdicionarHorario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_horario);
        inicializarComponenetes();

        btnAdicionarHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Horario horario = new Horario();
                horario.setTime(txtTime.getText().toString());
                horario.setDate(calendarView.getDate());


            }
        });
    }

    private void inicializarComponenetes(){

        txtTime = findViewById(R.id.txtTime);
        btnAdicionarHorario = findViewById(R.id.btnHorarioAdicionar);
        calendarView = findViewById(R.id.calendarView);
    }
}
