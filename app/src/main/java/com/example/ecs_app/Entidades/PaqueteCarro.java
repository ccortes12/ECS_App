package com.example.ecs_app.Entidades;

public class PaqueteCarro {

    private int intEstado;
    private String strDescEstado;
    private int intIdRelacionCarro;
    private int intIdRelacionPaquete;
    private String chrNumeroCarro;
    private String sdtFechaRecepcion;
    private String chrCodigoPaquete;
    private String chrCodigoLote;
    private Double decPesoNeto;
    private Double decPesoBruto;
    private Double intCantidadPiezas;

    public PaqueteCarro() {
    }

    public PaqueteCarro(int intEstado, String strDescEstado, int intIdRelacionCarro, int intIdRelacionPaquete, String chrNumeroCarro, String sdtFechaRecepcion, String chrCodigoPaquete, String chrCodigoLote, Double decPesoNeto, Double decPesoBruto, Double intCantidadPiezas) {
        this.intEstado = intEstado;
        this.strDescEstado = strDescEstado;
        this.intIdRelacionCarro = intIdRelacionCarro;
        this.intIdRelacionPaquete = intIdRelacionPaquete;
        this.chrNumeroCarro = chrNumeroCarro;
        this.sdtFechaRecepcion = sdtFechaRecepcion;
        this.chrCodigoPaquete = chrCodigoPaquete;
        this.chrCodigoLote = chrCodigoLote;
        this.decPesoNeto = decPesoNeto;
        this.decPesoBruto = decPesoBruto;
        this.intCantidadPiezas = intCantidadPiezas;
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

    public int getIntIdRelacionCarro() {
        return intIdRelacionCarro;
    }

    public void setIntIdRelacionCarro(int intIdRelacionCarro) {
        this.intIdRelacionCarro = intIdRelacionCarro;
    }

    public int getIntIdRelacionPaquete() {
        return intIdRelacionPaquete;
    }

    public void setIntIdRelacionPaquete(int intIdRelacionPaquete) {
        this.intIdRelacionPaquete = intIdRelacionPaquete;
    }

    public String getChrNumeroCarro() {
        return chrNumeroCarro;
    }

    public void setChrNumeroCarro(String chrNumeroCarro) {
        this.chrNumeroCarro = chrNumeroCarro;
    }

    public String getSdtFechaRecepcion() {
        return sdtFechaRecepcion;
    }

    public void setSdtFechaRecepcion(String sdtFechaRecepcion) {
        this.sdtFechaRecepcion = sdtFechaRecepcion;
    }

    public String getChrCodigoPaquete() {
        return chrCodigoPaquete;
    }

    public void setChrCodigoPaquete(String chrCodigoPaquete) {
        this.chrCodigoPaquete = chrCodigoPaquete;
    }

    public String getChrCodigoLote() {
        return chrCodigoLote;
    }

    public void setChrCodigoLote(String chrCodigoLote) {
        this.chrCodigoLote = chrCodigoLote;
    }

    public Double getDecPesoNeto() {
        return decPesoNeto;
    }

    public void setDecPesoNeto(Double decPesoNeto) {
        this.decPesoNeto = decPesoNeto;
    }

    public Double getDecPesoBruto() {
        return decPesoBruto;
    }

    public void setDecPesoBruto(Double decPesoBruto) {
        this.decPesoBruto = decPesoBruto;
    }

    public Double getIntCantidadPiezas() {
        return intCantidadPiezas;
    }

    public void setIntCantidadPiezas(Double intCantidadPiezas) {
        this.intCantidadPiezas = intCantidadPiezas;
    }
}
