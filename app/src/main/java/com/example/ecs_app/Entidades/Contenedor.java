package com.example.ecs_app.Entidades;

import java.io.Serializable;
import java.util.ArrayList;

public class Contenedor  implements Serializable {

    //CONTAINER
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
    //CONTAINER Para consolidacion

    private int isoCode;
    private double tara;

    private String codCliente;
    private String descCliente;
    private String codMarca;
    private String descMarca;

    private int zuncho;
    private int gross;
    private int pesoNeto;
    //CONTAINER consolidado

    private String chrConsolidado;
    private int corCFS;

    //Deben ser añadidos en las ventanas TRF01 & TRF02
    private String sello;
    private String codBarra;


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

    public double getPesoNeto() {
        return pesoNeto;
    }

    public void setPesoNeto(int pesoNeto) {
        this.pesoNeto = pesoNeto;
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

    public int getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(int isoCode) {
        this.isoCode = isoCode;
    }

    public String getDescCliente() {
        return descCliente;
    }

    public void setDescCliente(String descCliente) {
        this.descCliente = descCliente;
    }

    public String getSello() {
        return sello;
    }

    public void setSello(String sello) {
        this.sello = sello;
    }

    public int getCorCFSEntrega() {
        return corCFS;
    }

    public void setCorCFSEntrega(int corEntrega) {
        this.corCFS = corEntrega;
    }

    public double getTara() {
        return tara;
    }

    public void setTara(double tara) {
        this.tara = tara;
    }

    public int getZuncho() {
        return zuncho;
    }

    public void setZuncho(int zuncho) {
        this.zuncho = zuncho;
    }

    public int getGross() {
        return gross;
    }

    public void setGross(int gross) {
        this.gross = gross;
    }

    public int getCorCFS() {
        return corCFS;
    }

    public void setCorCFS(int corCFS) {
        this.corCFS = corCFS;
    }

    public String getCodBarra() {
        return codBarra;
    }

    public void setCodBarra(String codBarra) {
        this.codBarra = codBarra;
    }
}
