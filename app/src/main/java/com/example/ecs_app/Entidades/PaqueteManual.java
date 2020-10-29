package com.example.ecs_app.Entidades;

public class PaqueteManual {

    private int intIdRelacionPaquete;
    private int codigoLote;
    private int codigoPaquete;
    private double pesoNeto;

    public PaqueteManual(int intIdRelacionPaquete, int codigoLote, int codigoPaquete, double pesoNeto) {
        this.intIdRelacionPaquete = intIdRelacionPaquete;
        this.codigoLote = codigoLote;
        this.codigoPaquete = codigoPaquete;
        this.pesoNeto = pesoNeto;
    }

    public PaqueteManual() {
    }

    public int getIntIdRelacionPaquete() {
        return intIdRelacionPaquete;
    }

    public void setIntIdRelacionPaquete(int intIdRelacionPaquete) {
        this.intIdRelacionPaquete = intIdRelacionPaquete;
    }

    public int getCodigoLote() {
        return codigoLote;
    }

    public void setCodigoLote(int codigoLote) {
        this.codigoLote = codigoLote;
    }

    public int getCodigoPaquete() {
        return codigoPaquete;
    }

    public void setCodigoPaquete(int codigoPaquete) {
        this.codigoPaquete = codigoPaquete;
    }

    public double getPesoNeto() {
        return pesoNeto;
    }

    public void setPesoNeto(double pesoNeto) {
        this.pesoNeto = pesoNeto;
    }
}
