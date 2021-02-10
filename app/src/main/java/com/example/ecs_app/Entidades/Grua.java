package com.example.ecs_app.Entidades;

public class Grua {

    private int estado;
    private int codGrua;
    private String descGrua;

    public Grua() {
    }

    public Grua(int estado, int codGrua, String descGrua) {
        this.estado = estado;
        this.codGrua = codGrua;
        this.descGrua = descGrua;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getCodGrua() {
        return codGrua;
    }

    public void setCodGrua(int codGrua) {
        this.codGrua = codGrua;
    }

    public String getDescGrua() {
        return descGrua;
    }

    public void setDescGrua(String descGrua) {
        this.descGrua = descGrua;
    }


}
