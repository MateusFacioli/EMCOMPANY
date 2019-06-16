package com.tcc.easymeal.model;

import com.google.firebase.database.DatabaseReference;

import com.tcc.easymeal.config.ConfiguracaoFirebase;

public class Cliente {

    private String uid;
    private String nome;
    private String email;
    private String telefone;
    private String senha;

    public Cliente() {
    }



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
