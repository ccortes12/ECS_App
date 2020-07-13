package com.example.ecs_app.Entidades;

public class Celda {

    private int intEstado;
    private String codCelda;
    private String desCelda;
    private String codArea;
    private String desArea;
    private int capacidadPaquetes;
    private String preAcopio;
    private int timeStamp;

    public Celda() {
    }

    public Celda(int intEstado, String codCelda, String desCelda, String codArea, String desArea, int capacidadPaquetes, String preAcopio, int timeStamp) {
        this.intEstado = intEstado;
        this.codCelda = codCelda;
        this.desCelda = desCelda;
        this.codArea = codArea;
        this.desArea = desArea;
        this.capacidadPaquetes = capacidadPaquetes;
        this.preAcopio = preAcopio;
        this.timeStamp = timeStamp;
    }

    public int getIntEstado() {
        return intEstado;
    }

    public void setIntEstado(int intEstado) {
        this.intEstado = intEstado;
    }

    public String getCodCelda() {
        return codCelda;
    }

    public void setCodCelda(String codCelda) {
        this.codCelda = codCelda;
    }

    public String getDesCelda() {
        return desCelda;
    }

    public void setDesCelda(String desCelda) {
        this.desCelda = desCelda;
    }

    public String getCodArea() {
        return codArea;
    }

    public void setCodArea(String codArea) {
        this.codArea = codArea;
    }

    public String getDesArea() {
        return desArea;
    }

    public void setDesArea(String desArea) {
        this.desArea = desArea;
    }

    public int getCapacidadPaquetes() {
        return capacidadPaquetes;
    }

    public void setCapacidadPaquetes(int capacidadPaquetes) {
        this.capacidadPaquetes = capacidadPaquetes;
    }

    public String getPreAcopio() {
        return preAcopio;
    }

    public void setPreAcopio(String preAcopio) {
        this.preAcopio = preAcopio;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }
}
