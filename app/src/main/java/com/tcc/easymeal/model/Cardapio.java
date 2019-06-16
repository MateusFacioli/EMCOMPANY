package com.tcc.easymeal.model;

import com.google.firebase.database.DatabaseReference;
import com.tcc.easymeal.config.ConfiguracaoFirebase;
import com.tcc.easymeal.helper.UsuarioFirebase;

public class Cardapio {

    private String nome;
    private Double preco;
    private String descricao;
    private String imgUrl;
    private String idProduto;





    public Cardapio() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference produtoRef = firebaseRef
                .child("produtos");
        setIdProduto( produtoRef.push().getKey() );
    }

    public void salvar(){
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference reference = database
                .child("cardapio")
                .child(UsuarioFirebase.getDadosUsuarioLogado().getUid())
                .child(getIdProduto());


        reference.setValue(this);
    }
    public void remover(){
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference reference = database
                .child("cardapio")
                .child(UsuarioFirebase.getDadosUsuarioLogado().getUid())
                .child(getIdProduto());

        reference.removeValue();
    }



    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String itens) {
        this.descricao = itens;
    }
}
