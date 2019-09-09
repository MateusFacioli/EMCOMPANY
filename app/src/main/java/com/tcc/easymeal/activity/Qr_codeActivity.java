package com.tcc.easymeal.activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.tcc.easymeal.R;


public class Qr_codeActivity extends AppCompatActivity {
   private Button scan;
   private ImageView qr_code;
    private TextInputEditText information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code_reader);
        scan=findViewById(R.id.btn_scan);
        qr_code=findViewById(R.id.qr_code);
        information=findViewById(R.id.texto_qrcode);

        //final Activity activity = this;

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             String text= information.getText().toString().trim();
             if(text!=null){
                 MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                 try {
                     BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,500,500);
                     BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
                     Bitmap bitmap=barcodeEncoder.createBitmap(bitMatrix);
                     qr_code.setImageBitmap(bitmap);
                 }
                 catch (WriterException e)
                 {
                     e.printStackTrace();
                 }


             }


            }
        });

    }

    private void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT)
                .show();
    }

}