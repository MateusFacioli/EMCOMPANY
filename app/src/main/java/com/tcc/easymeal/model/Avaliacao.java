package com.tcc.easymeal.model;

import java.io.Serializable;

public class Avaliacao implements Serializable {

    private String comentario;
    private Double avaliacao;

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }
}
