package com.example.ecs_app.Entidades;

public class Gruero {

    private int estado;
    private int codOperador;
    private String DesOperador;

    public Gruero() {
    }

    public Gruero(int estado, int codOperador, String desOperador) {
        this.estado = estado;
        this.codOperador = codOperador;
        DesOperador = desOperador;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getCodOperador() {
        return codOperador;
    }

    public void setCodOperador(int codOperador) {
        this.codOperador = codOperador;
    }

    public String getDesOperador() {
        return DesOperador;
    }

    public void setDesOperador(String desOperador) {
        DesOperador = desOperador;
    }
}
