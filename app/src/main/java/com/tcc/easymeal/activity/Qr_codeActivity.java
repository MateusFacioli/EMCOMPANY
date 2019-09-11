package com.tcc.easymeal.activity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.tcc.easymeal.R;


public class Qr_codeActivity extends AppCompatActivity {
   private Button scan;
   private ImageView qr_code;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code_reader);
        scan=findViewById(R.id.btn_scan);
        qr_code=findViewById(R.id.qr_code);

        final Activity activity = this;


        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);//just qrcode types =(
                integrator.setPrompt("EASYMEAL SCAN");
                integrator.setOrientationLocked(true);
                integrator.setBeepEnabled(true);
                integrator.setCameraId(0);//camera traseira
                integrator.initiateScan();

                snackbar =Snackbar.make(v,"Código lido com sucesso",Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result= IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if(result!=null)
        {
            if(result.getContents()!=null)
           {
               snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Log.d("meuLog","Lido com sucesso");
                }
            });
        } else
            {
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Log.d("meuLog","tudoErrado");
                    }
                });
            }

        }
        else
             {
              super.onActivityResult(requestCode, resultCode, data);
             }
    }

    private void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT)
                .show();
    }

}