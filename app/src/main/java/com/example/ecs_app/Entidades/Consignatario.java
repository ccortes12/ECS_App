package com.example.ecs_app.Entidades;

public class Consignatario {

    private int estado;
    private String descEstado;
    private String codCliente;
    private String descCliente;

    public Consignatario(int estado, String descEstado, String codCliente, String descCliente) {
        this.estado = estado;
        this.descEstado = descEstado;
        this.codCliente = codCliente;
        this.descCliente = descCliente;
    }

    public Consignatario(){}

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getDescEstado() {
        return descEstado;
    }

    public void setDescEstado(String descEstado) {
        this.descEstado = descEstado;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public String getDescCliente() {
        return descCliente;
    }

    public void setDescCliente(String descCliente) {
        this.descCliente = descCliente;
    }
}
