package com.example.ecs_app.Entidades;

public class Contenedor {

    private String descEstado;
    private int annoOperacion;
    private int corOperacion;
    private String sigla;
    private int numero;
    private String digito;
    private String codShipper;
    private String descShipper;
    private String codPuerto;
    private String descPuerto;

    private int pesoNeto;
    private double zuncho;
    private String codMarca;
    private String descMarca;
    private String codCliente;

    private String chrConsolidado;


    public Contenedor(String descEstado, int annoOperacion, int corOperacion, String sigla, int numero, String digito, String codShipper, String descShipper, String codPuerto, String descPuerto) {
        this.descEstado = descEstado;
        this.annoOperacion = annoOperacion;
        this.corOperacion = corOperacion;
        this.sigla = sigla;
        this.numero = numero;
        this.digito = digito;
        this.codShipper = codShipper;
        this.descShipper = descShipper;
        this.codPuerto = codPuerto;
        this.descPuerto = descPuerto;
    }

    public Contenedor(){

    }

    public void setDescEstado(String descEstado) {
        this.descEstado = descEstado;
    }

    public void setAnnoOperacion(int annoOperacion) {
        this.annoOperacion = annoOperacion;
    }

    public void setCorOperacion(int corOperacion) {
        this.corOperacion = corOperacion;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setDigito(String digito) {
        this.digito = digito;
    }

    public void setCodShipper(String codShipper) {
        this.codShipper = codShipper;
    }

    public void setDescShipper(String descShipper) {
        this.descShipper = descShipper;
    }

    public void setCodPuerto(String codPuerto) {
        this.codPuerto = codPuerto;
    }

    public void setDescPuerto(String descPuerto) {
        this.descPuerto = descPuerto;
    }

    public String getDescEstado() {
        return descEstado;
    }

    public int getAnnoOperacion() {
        return annoOperacion;
    }

    public int getCorOperacion() {
        return corOperacion;
    }

    public String getSigla() {
        return sigla;
    }

    public int getNumero() {
        return numero;
    }

    public String getDigito() {
        return digito;
    }

    public String getCodShipper() {
        return codShipper;
    }

    public String getDescShipper() {
        return descShipper;
    }

    public String getCodPuerto() {
        return codPuerto;
    }

    public String getDescPuerto() {
        return descPuerto;
    }

    public int getPesoNeto() {
        return pesoNeto;
    }

    public void setPesoNeto(int pesoNeto) {
        this.pesoNeto = pesoNeto;
    }

    public double getZuncho() {
        return zuncho;
    }

    public void setZuncho(double zuncho) {
        this.zuncho = zuncho;
    }

    public String getCodMarca() {
        return codMarca;
    }

    public void setCodMarca(String codMarca) {
        this.codMarca = codMarca;
    }

    public String getDescMarca() {
        return descMarca;
    }

    public void setDescMarca(String descMarca) {
        this.descMarca = descMarca;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public String getChrConsolidado() {
        return chrConsolidado;
    }

    public void setChrConsolidado(String chrConsolidado) {
        this.chrConsolidado = chrConsolidado;
    }
}
