package com.tcc.easymeal.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tcc.easymeal.R;
import com.tcc.easymeal.activity.EditarActivity;
import com.tcc.easymeal.activity.LoginActivity;
import com.tcc.easymeal.model.Cardapio;

import java.io.Serializable;
import java.util.List;

public class AdapterProduto extends RecyclerView.Adapter<AdapterProduto.MyViewHolder>{

    private List<Cardapio> produtos;
    private Context context;


    public AdapterProduto(List<Cardapio> produtos, Context context ) {
        this.produtos = produtos;
        this.context = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_produto, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        Cardapio produto = produtos.get(i);
        holder.nome.setText(produto.getNome());
        holder.descricao.setText(produto.getDescricao());
        holder.valor.setText("R$ " + produto.getPreco());


        //Carregar imagem
        String urlImagem = produto.getImgUrl();
        Picasso.get().load( urlImagem ).into( holder.imgProduto );
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduto;
        EditText nome;
        EditText descricao;
        EditText valor;
        Button delete;
        Button edit;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.txtNomeProdutoPedido);
            descricao = itemView.findViewById(R.id.txtDescricaoPedido);
            valor = itemView.findViewById(R.id.txtPrecoPedido);
            imgProduto = itemView.findViewById(R.id.imageProdutoPedido);
            delete   =itemView.findViewById(R.id.delete_button);
            edit =itemView.findViewById(R.id.edit_button);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar snackbar;
                    snackbar =Snackbar.make(v,"Quer realmente deletar esse produto?",Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();
                    snackbar.setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Cardapio produtoSelecionado = produtos.get(getAdapterPosition());
                            produtoSelecionado.remover();
                            Toast.makeText(delete.getContext(),"Produto excluído com sucesso",Toast.LENGTH_LONG).show();
                        }
                    });

                }


            });


            edit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Snackbar snackbar;
                    snackbar = Snackbar.make(v, "Quer realmente alterar esse produto?", Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();
                    snackbar.setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Cardapio produtoSelecionado = produtos.get(getAdapterPosition());
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("produtos", (Serializable) produtos);
                            Intent intent = new Intent(context, EditarActivity.class);
                            intent.putExtras(bundle);
                            intent.putExtra("nome",nome.getText().toString());
                            context.startActivity(intent);




                            // nao cai no if mudar o metodo
                            if(nome.isInEditMode()==true&& valor.isInEditMode()==true&& descricao.isInEditMode()==true
                             && imgProduto.isInEditMode()==true)
                            {

                                String nome2 = nome.getText().toString();
                                String preco2 = valor.getText().toString();
                                String descricao2 = descricao.getText().toString();

                                produtoSelecionado.setDescricao(descricao2);
                                produtoSelecionado.setNome(nome2);
                                //produtoSelecionado.setPreco(Double.parseDouble(preco2));

                            }else{

                                nome.setClickable(false);
                                nome.setEnabled(false);
                                valor.setEnabled(false);
                                valor.setClickable(false);
                                imgProduto.setEnabled(false);
                                imgProduto.setClickable(false);
                                descricao.setClickable(false);
                                descricao.setEnabled(false);
                                produtoSelecionado.salvar();


                            }
                            Toast.makeText(edit.getContext(),"Produto alterado com sucesso",Toast.LENGTH_LONG).show();

                        }


                    });


                        }

                    }
                    );

                }




    }


}
