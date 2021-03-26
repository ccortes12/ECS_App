package com.example.ecs_app.Entidades;

public class Bodega {

    private int intEstado;
    private String strDescEstado;
    private String chrCodBodegaNave;
    private String vchDesBodegaNave;

    public Bodega() {
    }

    public Bodega(int intEstado, String strDescEstado, String chrCodBodegaNave, String vchDesBodegaNave) {
        this.intEstado = intEstado;
        this.strDescEstado = strDescEstado;
        this.chrCodBodegaNave = chrCodBodegaNave;
        this.vchDesBodegaNave = vchDesBodegaNave;
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

    public String getChrCodBodegaNave() {
        return chrCodBodegaNave;
    }

    public void setChrCodBodegaNave(String chrCodBodegaNave) {
        this.chrCodBodegaNave = chrCodBodegaNave;
    }

    public String getVchDesBodegaNave() {
        return vchDesBodegaNave;
    }

    public void setVchDesBodegaNave(String vchDesBodegaNave) {
        this.vchDesBodegaNave = vchDesBodegaNave;
    }
}
