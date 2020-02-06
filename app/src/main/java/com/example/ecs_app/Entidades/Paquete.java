package com.example.ecs_app.Entidades;

public class Paquete {

    private int estado;
    private String idPaquete;
    private String lote;
    private double peso;
    private String mensaje;


    public Paquete() {
    }

    public Paquete(int estado, String idPaquete, String lote, double peso, String mensaje) {
        this.estado = estado;
        this.idPaquete = idPaquete;
        this.lote = lote;
        this.peso = peso;
        this.mensaje = mensaje;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(String idPaquete) {
        this.idPaquete = idPaquete;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
