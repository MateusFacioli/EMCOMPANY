package com.tcc.easymeal.model;

import com.google.firebase.database.DatabaseReference;
import com.tcc.easymeal.config.ConfiguracaoFirebase;
import com.tcc.easymeal.helper.UsuarioFirebase;

public class Pedidos {



    private Comerciante comerciante;
    private Cliente cliente;
    private Loja produto;
    private String idpedido;
    private String status;
    private String urlImg;


    public Pedidos() {
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public Comerciante getComerciante() {
        return comerciante;
    }

    public void setComerciante(Comerciante comerciante) {
        this.comerciante = comerciante;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Loja getProduto() {
        return produto;
    }

    public void setProduto(Loja produto) {
        this.produto = produto;
    }

    public String getIdpedido() {
        return idpedido;
    }

    public void setIdpedido(String idpedido) {
        this.idpedido = idpedido;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
