package com.example.ecs_app.Entidades;

public class Paquete {

    private int estado;
    private String idPaquete;
    private String lote;
    private double peso;
    private String mensaje;

    private String codigoPaquete;
    private int piezas;
    private double pesoBruto;
    private double pesoNeto;
    private String chrFlgChequeo;
    private String area;
    private String celda;

    private String descEstado;

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

    public String getCodigoPaquete() {
        return codigoPaquete;
    }

    public void setCodigoPaquete(String codigoPaquete) {
        this.codigoPaquete = codigoPaquete;
    }

    public int getPiezas() {
        return piezas;
    }

    public void setPiezas(int piezas) {
        this.piezas = piezas;
    }

    public double getPesoBruto() {
        return pesoBruto;
    }

    public void setPesoBruto(double pesoBruto) {
        this.pesoBruto = pesoBruto;
    }

    public double getPesoNeto() {
        return pesoNeto;
    }

    public void setPesoNeto(double pesoNeto) {
        this.pesoNeto = pesoNeto;
    }

    public String getChrFlgChequeo() {
        return chrFlgChequeo;
    }

    public void setChrFlgChequeo(String chrFlgChequeo) {
        this.chrFlgChequeo = chrFlgChequeo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCelda() {
        return celda;
    }

    public void setCelda(String celda) {
        this.celda = celda;
    }

    public String getDescEstado() {
        return descEstado;
    }

    public void setDescEstado(String descEstado) {
        this.descEstado = descEstado;
    }


}
