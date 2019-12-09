package com.tcc.easymeal.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import com.tcc.easymeal.R;
import com.tcc.easymeal.model.Horario;

import java.util.ArrayList;
import java.util.List;

public class CadastrarHorarioActivity extends AppCompatActivity {



    private CalendarView calendarView;
    private FloatingActionButton btnAdicionarHorario;
    private Snackbar snackbar;
    private List<Horario> horarios = new ArrayList<>();
    private String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_cadastrar_horario);
        inicializarComponenetes();
        super.onCreate(savedInstanceState);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                if(year !=0 && month !=0 && dayOfMonth!=0)
                {
                    date = (dayOfMonth + "/"+ month + "/" + year);
                }
                else
                {
                    exibirMensagem("Erro, por favor selecione uma data válida");
                }


            }
        });

        btnAdicionarHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent pt2 = new Intent(CadastrarHorarioActivity.this, CadastrarHorarioPt2Activity.class);
                pt2.putExtra("date", date);
                startActivity(pt2);

            }
        });

    }



    private void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT)
                .show();
    }

    private void inicializarComponenetes(){

        btnAdicionarHorario = findViewById(R.id.btnFloatNextPT1);
        calendarView = findViewById(R.id.calendarView);
    }
}
