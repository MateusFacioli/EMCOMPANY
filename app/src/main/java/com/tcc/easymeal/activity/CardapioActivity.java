package com.tcc.easymeal.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tcc.easymeal.R;
import com.tcc.easymeal.config.ConfiguracaoFirebase;
import com.tcc.easymeal.helper.UsuarioFirebase;
import com.tcc.easymeal.model.Cardapio;


import java.io.ByteArrayOutputStream;

public class CardapioActivity extends AppCompatActivity {

    private EditText editNomeProduto,editDescricao,
            editPreco;

    private ImageView imageProduto;
    private FirebaseAuth autenticacao;
    private static final int SELECAO_GALERIA = 200;
    private static final int SELECAO_CAMERA = 100;
    private StorageReference storageReference;
    private DatabaseReference firebaseRef;
    private String idUsuarioLogado;
    private String urlImagemSelecionada;
    private Button validarDadosEmpresa;
    private Toolbar toolbar;
    private Cardapio cardapio = new Cardapio();

    private ImageButton imageButtonCamera;
    private ImageButton imageButtonGaleria;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);
        inicializarComponentes();

        View v = getLayoutInflater().inflate(R.layout.toolbar, null);
        toolbar = v.findViewById(R.id.toolbarCardapio);

        //toolbar.setTitle("Cadastro");
        //setSupportActionBar(toolbar);
        //getSupportActionBar().hide();


        inicializarComponentes();

        validarDadosEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validarDadosProduto(v);
            }
        });

        imageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                if( i.resolveActivity(getPackageManager()) != null ){
                    startActivityForResult(i, SELECAO_CAMERA);
                }

            }
        });


        imageButtonGaleria.setOnClickListener(new View.OnClickListener() {
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
                    imageProduto.setImageBitmap(imagen);

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
                            exibirMensagem("Falha ao salvar a foto");

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            exibirMensagem("Foto salva com sucesso");
                            urlImagemSelecionada = taskSnapshot.getDownloadUrl().toString();

                        }
                    });

                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }



    public void validarDadosProduto(View view){

        //Valida se os campos foram preenchidos
        String nome = editNomeProduto.getText().toString();
        String descricao = editDescricao.getText().toString();
        String preco = editPreco.getText().toString();
        Double precoD = Double.valueOf(preco);


        if (!nome.isEmpty()) {
            if (!descricao.isEmpty()) {
                if (!preco.isEmpty()) {
                    if(precoD > 0) {

//git
                        cardapio.setNome(nome);
                        cardapio.setDescricao(descricao);
                        cardapio.setPreco(Double.parseDouble(preco));
                        cardapio.setImgUrl(urlImagemSelecionada);
                        cardapio.salvar();
                        finish();
                        exibirMensagem("Produto Salvo com sucesso");
                    }else {
                        exibirMensagem("Digite um valor maior que zero");
                    }


                } else {
                    exibirMensagem("Digite um Preço para o produto");
                }
            } else {
                exibirMensagem("Digite uma desfrição para o produto");
            }
        } else {
            exibirMensagem("Digite o nome do seu produto ");
        }

    }

    private void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT)
                .show();
    }


  /**  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode == RESULT_OK){
            Bitmap imagem = null;

            try {

                switch (requestCode) {
                    case SELECAO_GALERIA:
                        Uri localImagem = data.getData();
                        imagem = MediaStore.Images
                                .Media
                                .getBitmap(
                                        getContentResolver(),
                                        localImagem
                                );
                        break;
                }

                if( imagem != null){

                    imageProduto.setImageBitmap( imagem );

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("Comerciante")
                            .child("Cardapio")
                            .child(idUsuarioLogado + "jpeg");

                    UploadTask uploadTask = imagemRef.putBytes( dadosImagem );
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CardapioActivity.this,
                                    "Erro ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            urlImagemSelecionada = taskSnapshot.getDownloadUrl().toString();
                            Toast.makeText(CardapioActivity.this,
                                    "Sucesso ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show();

                        }
                    });

                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }
  **/

    private void inicializarComponentes(){
        editNomeProduto = findViewById(R.id.editNomeEmpresa);
        editDescricao = findViewById(R.id.editDescricao);
        editPreco = findViewById(R.id.editPreco);
        imageProduto = findViewById(R.id.imageProdutoPedido);
        validarDadosEmpresa = findViewById(R.id.validarDadosEmpresa);
        imageButtonGaleria = findViewById(R.id.imageButtonGaleria);
        imageButtonCamera = findViewById(R.id.imageButtonCamera);
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();

    }
}
