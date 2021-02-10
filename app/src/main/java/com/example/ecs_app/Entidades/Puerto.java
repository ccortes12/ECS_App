package com.example.ecs_app.Entidades;

public class Puerto {

    private int intEstado;
    private String strDescEstado;
    private String strCodPuerto;
    private String strDesPuerto;

    public Puerto() {
    }

    public Puerto(int intEstado, String strDescEstado, String strCodPuerto, String strDesPuerto) {
        this.intEstado = intEstado;
        this.strDescEstado = strDescEstado;
        this.strCodPuerto = strCodPuerto;
        this.strDesPuerto = strDesPuerto;
    }

    public int getIntEstado() {
        return intEstado;
    }

    public void setIntEstado(int intEstado) {
        this.intEstado = intEstado;
    }

    public String getStrDescEstado() {
        return strDescEstado;
    }

    public void setStrDescEstado(String strDescEstado) {
        this.strDescEstado = strDescEstado;
    }

    public String getStrCodPuerto() {
        return strCodPuerto;
    }

    public void setStrCodPuerto(String strCodPuerto) {
        this.strCodPuerto = strCodPuerto;
    }

    public String getStrDesPuerto() {
        return strDesPuerto;
    }

    public void setStrDesPuerto(String strDesPuerto) {
        this.strDesPuerto = strDesPuerto;
    }
}
