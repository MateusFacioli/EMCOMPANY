package com.tcc.easymeal.adapter;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.snapshot.Index;
import com.squareup.picasso.Picasso;
import com.tcc.easymeal.R;
import com.tcc.easymeal.model.Cardapio;

import java.util.List;

public class AdapterProduto extends RecyclerView.Adapter<AdapterProduto.MyViewHolder>{

    private List<Cardapio> produtos;


    public AdapterProduto(List<Cardapio> produtos) {
        this.produtos = produtos;

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
        TextView nome;
        TextView descricao;
        TextView valor;
        Button delete;
        Button edit;

        public MyViewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.txtNomeProdutoPedido);
            descricao = itemView.findViewById(R.id.txtDescricaoPedido);
            valor = itemView.findViewById(R.id.txtPrecoPedido);
            imgProduto = itemView.findViewById(R.id.imageProdutoPedido);
            delete   =itemView.findViewById(R.id.delete);
            edit =itemView.findViewById(R.id.edit);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar snackbar;
                    snackbar =Snackbar.make(v,"Quer realmente deletar esse produto?",Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();

                    snackbar.setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                       Log.d("meuLog","Clicou na acao remover");
                        }
                    });
                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar snackbar;
                    snackbar =Snackbar.make(v,"Quer realmente editar esse produto?",Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();

                    snackbar.setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("meuLog","Clicou na acao para editar");
                        }
                    });

                }
            });

        }
    }
}
