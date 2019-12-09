package com.tcc.easymeal.model;

import com.google.firebase.database.DatabaseReference;
import com.tcc.easymeal.config.ConfiguracaoFirebase;

import java.io.Serializable;

public class Comerciante implements Serializable {

    private String uid;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String senha;
    private Avaliacao avaliacao;



    public Comerciante() {
    }

    public void salvar(){
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference reference = database.child("comerciante").child(getUid());

        reference.setValue(this);
    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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
