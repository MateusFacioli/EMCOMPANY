package com.tcc.easymeal.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tcc.easymeal.R;
import com.tcc.easymeal.model.Horario;

public class CadastrarHorarioPt2Activity extends AppCompatActivity {

    private Intent iCadastro;
    private Bundle bCadastro;
    private Horario horario = new Horario();

    private TimePicker timeInicio;
    private TimePicker timeFinal;

    private FloatingActionButton floatNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_horario_pt2);
        inicializarComponentes();
        configurarComponentes();

        timeInicio.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                if(hourOfDay != 0 && minute != 0){

                    horario.setTimeInicio(hourOfDay + ":" + minute);
                }
                else
                {
                    Toast.makeText( CadastrarHorarioPt2Activity.this,"Horário inicial inválido", Toast.LENGTH_SHORT)
                            .show();
                }



            }
        });

        timeFinal.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                if(hourOfDay != 0 && minute != 0){
                    horario.setTimeFinal(hourOfDay + ":" + minute);
                }
                else
                {
                    Toast.makeText( CadastrarHorarioPt2Activity.this,"Horário final inválido", Toast.LENGTH_SHORT)
                            .show();
                }




            }
        });

        floatNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CadastrarHorarioPt2Activity.this, horario.getDate()
                        + "/" + horario.getTimeInicio()
                        + "/" + horario.getTimeFinal(), Toast.LENGTH_SHORT).show();

                horario.salvar();
                Intent intent = new Intent(getApplicationContext(), Locais.class);
               // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //Intent mapa = new Intent(CadastrarHorarioPt2Activity.this, )

            }
        });
    }


    private void inicializarComponentes(){

        iCadastro = getIntent();
        bCadastro = iCadastro.getExtras();
        timeInicio = findViewById(R.id.timeInicio);
        timeFinal = findViewById(R.id.timeFinal);
        floatNext = findViewById(R.id.btnFloatNextHorario);


    }

    private void configurarComponentes(){
        if(bCadastro != null){
            String date;
            date = bCadastro.get("date").toString();
            horario.setDate(date);

        }

        timeInicio.setIs24HourView(true);
        timeFinal.setIs24HourView(true);
    }
}
