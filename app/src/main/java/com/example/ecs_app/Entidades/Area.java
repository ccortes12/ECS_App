package com.example.ecs_app.Entidades;

public class Area {

    private int estado;
    private String codArea;
    private String desArea;
    private int timeStamp;
    private String nombre;

    public Area() {
    }

    public Area(int estado, String codArea, String desArea, int timeStamp, String nombre) {
        this.estado = estado;
        this.codArea = codArea;
        this.desArea = desArea;
        this.timeStamp = timeStamp;
        this.nombre = nombre;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
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

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
