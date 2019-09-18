package com.tcc.easymeal.adapter;


import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
                            //confirmar a exclusao
                        }
                    });

                }


            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    nome.setEnabled(true);
                    nome.setClickable(true);
                    valor.setClickable(true);
                    valor.setEnabled(true);
                    descricao.setEnabled(true);
                    descricao.setClickable(true);
                    imgProduto.setClickable(true);
                    imgProduto.setEnabled(true);
                    String nome2=nome.getText().toString();
                    String preco2 = valor.getText().toString();
                    String descricao2=descricao.getText().toString();

                    //nome.setEnabled(false);
                    //nome.setClickable(false);
                    //valor.setClickable(false);
                    //valor.setEnabled(false);
                    //descricao.setEnabled(false);
                    //descricao.setClickable(false);
                    //imgProduto.setClickable(false);
                    //imgProduto.setEnabled(false);
                    Cardapio produtoSelecionado = produtos.get(getAdapterPosition());
                    produtoSelecionado.setDescricao(descricao2);
                    produtoSelecionado.setNome(nome2);
                    //produtoSelecionado.setPreco(Double.parseDouble(preco2));
                   // produtoSelecionado.salvar();
                    //alterar e salvar
                }

            });

        }


    }


}
