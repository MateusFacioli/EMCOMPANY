package com.tcc.easymeal.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tcc.easymeal.R;
import com.tcc.easymeal.config.ConfiguracaoFirebase;
import com.tcc.easymeal.helper.UsuarioFirebase;
import com.tcc.easymeal.model.Cardapio;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class EditarActivity extends AppCompatActivity {

    private Intent iEditar;
    private Bundle bEditar;
    private List<Cardapio> produtos = new ArrayList<>();
    private Cardapio cardapio = new Cardapio();
    private DatabaseReference mDatabase;
    private String nome;
    private String idProduto;
    private static final int SELECAO_GALERIA = 200;
    private static final int SELECAO_CAMERA = 100;

    private EditText editNomeProdutoEditar;
    private EditText editDescricaoEditar;
    private EditText  editPrecoEditar;
    private StorageReference storageReference;

    private ImageView imageProdutoEditar;
    private ImageButton imageButtonCameraEditar;
    private ImageButton imageButtonGaleriaEditar;
    private Button validarDadosEmpresaEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);
        inicializarComponentes();
        configurarComponentes();
        validarDadosEmpresaEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    editarProduto();


            }
        });

        imageButtonCameraEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                if( i.resolveActivity(getPackageManager()) != null ){
                    startActivityForResult(i, SELECAO_CAMERA);
                }

            }
        });


        imageButtonGaleriaEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                );
                if( i.resolveActivity(getPackageManager()) != null ){
                    startActivityForResult(i, SELECAO_GALERIA);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

            Bitmap imagen = null;

            try{
                switch (requestCode){
                    case SELECAO_CAMERA:
                        imagen = (Bitmap) data.getExtras().get("data");
                        break;

                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData ();
                        imagen = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imagen.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                byte[] dadosImagem = baos.toByteArray();
                if(imagen != null){
                    imageProdutoEditar.setImageBitmap(imagen);

                    DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
                    DatabaseReference produtoRef = firebaseRef
                            .child("produto")
                            .child(UsuarioFirebase.getDadosUsuarioLogado().getUid());
                    String idPedido =  produtoRef.push().getKey();

                    StorageReference imagemRef = storageReference
                            .child("cardapio")
                            .child(UsuarioFirebase.getUsuarioAtual().getUid())
                            .child(idPedido)
                            .child("produto.jpeg");



                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //exibirMensagem("Falha ao salvar a foto");

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                          //  exibirMensagem("Foto salva com sucesso");
                            cardapio.setImgUrl(taskSnapshot.getDownloadUrl().toString());

                        }
                    });

                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void editarProduto(){
        String preco = editPrecoEditar.getText().toString();
        cardapio.setNome(editNomeProdutoEditar.getText().toString());
        cardapio.setDescricao(editDescricaoEditar.getText().toString());
        cardapio.setPreco(Double.valueOf(preco));
       // cardapio.setIdProduto(idProduto);

        cardapio.salvar();

        finish();


    }


    private void inicializarComponentes(){
        iEditar = getIntent();
        bEditar = iEditar.getExtras();
        mDatabase = ConfiguracaoFirebase.getFirebase();

        editNomeProdutoEditar = findViewById(R.id.editNomeEmpresaEditar);
        editDescricaoEditar = findViewById(R.id.editDescricaoEditar);
        editPrecoEditar = findViewById(R.id.editPrecoEditar);
        imageProdutoEditar = findViewById(R.id.imageProdutoPedidoEditar);
        validarDadosEmpresaEditar = findViewById(R.id.validarDadosEmpresaEditar);
        imageButtonGaleriaEditar = findViewById(R.id.imageButtonGaleriaEditar);
        imageButtonCameraEditar = findViewById(R.id.imageButtonCameraEditar);
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();

    }

    private void configurarComponentes(){
        produtos.clear();
        produtos = (List<Cardapio>) iEditar.getSerializableExtra("produtos");
       // int size = produtos.size();
     //   String aux = String.valueOf(size);
       // Toast.makeText(this, aux, Toast.LENGTH_SHORT).show();

        if(bEditar != null){
            nome = bEditar.get("nome").toString();
        }

        for(int i =0; i< produtos.size(); i++){
            if(nome.equals(produtos.get(i).getNome())){
                idProduto = produtos.get(i).getIdProduto();
                cardapio.setImgUrl(produtos.get(i).getImgUrl());
                cardapio.setNome(produtos.get(i).getNome());
                cardapio.setDescricao(produtos.get(i).getDescricao());
                cardapio.setPreco(produtos.get(i).getPreco());
                cardapio.setIdProduto(idProduto);
                editNomeProdutoEditar.setText(nome);
                editDescricaoEditar.setText(produtos.get(i).getDescricao());
                editPrecoEditar.setText(produtos.get(i).getPreco().toString());

            }
        }


    }
}
