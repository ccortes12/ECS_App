package com.example.ecs_app.Entidades;

public class Sello {

    private int estado;
    private String mensaje;
    private String sello;

    public Sello(int estado, String mensaje, String sello) {
        this.estado = estado;
        this.mensaje = mensaje;
        this.sello = sello;
    }

    public Sello() {
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getSello() {
        return sello;
    }

    public void setSello(String sello) {
        this.sello = sello;
    }
}
