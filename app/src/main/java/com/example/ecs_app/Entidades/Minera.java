package com.example.ecs_app.Entidades;

import java.io.Serializable;

public class Minera implements Serializable {

    private int intRutCliente;
    private String chrDvCliente;
    private String vchRazonSocial;
    private String vchNombreFantasia;
    private String vchGiro;
    private String vchDireccion;
    private String vchTelefonoFax;
    private String chrCodComuna;
    private long bigTimeStamp;
    private float intTonMin;
    private String cod_Shipper;
    private String chrTipoEmbarque;
    private int flgCantidadPiezas;
    private float dblTarifaAlmacenaje;
    private String colorPatioGrafico;
    private float dblTarTonMinimo;
    private float intTonMinTurno2;

    public Minera() {
    }

    public int getIntRutCliente() {
        return intRutCliente;
    }

    public void setIntRutCliente(int intRutCliente) {
        this.intRutCliente = intRutCliente;
    }

    public String getChrDvCliente() {
        return chrDvCliente;
    }

    public void setChrDvCliente(String chrDvCliente) {
        this.chrDvCliente = chrDvCliente;
    }

    public String getVchRazonSocial() {
        return vchRazonSocial;
    }

    public void setVchRazonSocial(String vchRazonSocial) {
        this.vchRazonSocial = vchRazonSocial;
    }

    public String getVchNombreFantasia() {
        return vchNombreFantasia;
    }

    public void setVchNombreFantasia(String vchNombreFantasia) {
        this.vchNombreFantasia = vchNombreFantasia;
    }

    public String getVchGiro() {
        return vchGiro;
    }

    public void setVchGiro(String vchGiro) {
        this.vchGiro = vchGiro;
    }

    public String getVchDireccion() {
        return vchDireccion;
    }

    public void setVchDireccion(String vchDireccion) {
        this.vchDireccion = vchDireccion;
    }

    public String getVchTelefonoFax() {
        return vchTelefonoFax;
    }

    public void setVchTelefonoFax(String vchTelefonoFax) {
        this.vchTelefonoFax = vchTelefonoFax;
    }

    public String getChrCodComuna() {
        return chrCodComuna;
    }

    public void setChrCodComuna(String chrCodComuna) {
        this.chrCodComuna = chrCodComuna;
    }

    public long getBigTimeStamp() {
        return bigTimeStamp;
    }

    public void setBigTimeStamp(long bigTimeStamp) {
        this.bigTimeStamp = bigTimeStamp;
    }

    public float getIntTonMin() {
        return intTonMin;
    }

    public void setIntTonMin(float intTonMin) {
        this.intTonMin = intTonMin;
    }

    public String getCod_Shipper() {
        return cod_Shipper;
    }

    public void setCod_Shipper(String cod_Shipper) {
        this.cod_Shipper = cod_Shipper;
    }

    public String getChrTipoEmbarque() {
        return chrTipoEmbarque;
    }

    public void setChrTipoEmbarque(String chrTipoEmbarque) {
        this.chrTipoEmbarque = chrTipoEmbarque;
    }

    public int getFlgCantidadPiezas() {
        return flgCantidadPiezas;
    }

    public void setFlgCantidadPiezas(int flgCantidadPiezas) {
        this.flgCantidadPiezas = flgCantidadPiezas;
    }

    public float getDblTarifaAlmacenaje() {
        return dblTarifaAlmacenaje;
    }

    public void setDblTarifaAlmacenaje(float dblTarifaAlmacenaje) {
        this.dblTarifaAlmacenaje = dblTarifaAlmacenaje;
    }

    public String getColorPatioGrafico() {
        return colorPatioGrafico;
    }

    public void setColorPatioGrafico(String colorPatioGrafico) {
        this.colorPatioGrafico = colorPatioGrafico;
    }

    public float getDblTarTonMinimo() {
        return dblTarTonMinimo;
    }

    public void setDblTarTonMinimo(float dblTarTonMinimo) {
        this.dblTarTonMinimo = dblTarTonMinimo;
    }

    public float getIntTonMinTurno2() {
        return intTonMinTurno2;
    }

    public void setIntTonMinTurno2(float intTonMinTurno2) {
        this.intTonMinTurno2 = intTonMinTurno2;
    }
}
