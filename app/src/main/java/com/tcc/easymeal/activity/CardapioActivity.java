package com.tcc.easymeal.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tcc.easymeal.R;
import com.tcc.easymeal.model.Cardapio;
import com.tcc.easymeal.model.Loja;

public class CardapioActivity extends AppCompatActivity {

    private TextInputEditText inputNomeProduto;
    private TextInputEditText inputPreçoProduto;
    private TextInputEditText inputItensProduto;
    private Button btnSalvarPrato;
    private Loja loja = new Loja();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);
        inicializarComponentes();



        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            loja = (Loja) bundle.getSerializable("loja");
        }






        btnSalvarPrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Cardapio cardapio = new Cardapio();
                String nome = inputNomeProduto.getText().toString();
                String preco = inputPreçoProduto.getText().toString();
                String itens = inputItensProduto.getText().toString();

                if(!nome.isEmpty()){
                    if(!preco.isEmpty()){
                        if(!itens.isEmpty()){

                            cardapio.setNome(nome);
                            cardapio.setPreço(preco);
                            cardapio.setItens(itens);
                            loja.setCardapio(cardapio);
                            loja.salvar();

                        }else {
                            Toast.makeText(CardapioActivity.this, "Preencha o Campo itens ", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(CardapioActivity.this, "Preencha o Campo Preço", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(CardapioActivity.this, "Preencha o Campo Nome", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private void inicializarComponentes(){

        inputNomeProduto = findViewById(R.id.inputNomeProduto);
        inputPreçoProduto = findViewById(R.id.inputPreçoProduto);
        inputItensProduto = findViewById(R.id.inputItensProduto);
        btnSalvarPrato = findViewById(R.id.btnSalvarPrato);
    }
}
