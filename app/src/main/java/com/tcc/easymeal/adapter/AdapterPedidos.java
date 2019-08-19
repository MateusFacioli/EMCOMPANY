package com.tcc.easymeal.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.tcc.easymeal.R;
import com.tcc.easymeal.model.Pedidos;

import java.util.List;

public class AdapterPedidos extends RecyclerView.Adapter<AdapterPedidos.MyViewHoder> {

    private List<Pedidos> pedido;

    public AdapterPedidos(List<Pedidos> pedidos){
        this.pedido = pedidos; //tamanho zero aqui
    }

    @NonNull
    @Override
    public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pedidos, parent, false);
        return new MyViewHoder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoder holder, int i) {

        Pedidos pedidoR = pedido.get(i);
        holder.nomeProduto.setText(pedidoR.getProduto().getNome());
        holder.descricao.setText(pedidoR.getProduto().getDescricao());
        holder.valor.setText("R$     " +pedidoR.getProduto().getPreco().toString());

        //Carregar imagem
          String urlImagem = pedidoR.getProduto().getImgUrl();
          Picasso.get().load( urlImagem ).into( holder.imagemEmpresa );

    }

    @Override
    public int getItemCount() {
        return pedido.size();
    }

    public class MyViewHoder extends RecyclerView.ViewHolder{

        ImageView imagemEmpresa;
        TextView nomeProduto;
        TextView descricao;
        TextView valor;

        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            imagemEmpresa = itemView.findViewById(R.id.imageProdutoPedido);
            nomeProduto = itemView.findViewById(R.id.txtNomeProdutoPedido);
            descricao = itemView.findViewById(R.id.txtDescricaoPedido);
            valor = itemView.findViewById(R.id.txtPrecoPedido);
        }
    }
}
