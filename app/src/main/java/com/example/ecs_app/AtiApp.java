package com.example.ecs_app;

import android.app.Application;
import com.example.ecs_app.Entidades.Area;
import com.example.ecs_app.Entidades.Minera;
import java.util.ArrayList;

public class AtiApp extends Application {

    private String fecha;
    private int turno;
    private int rutUsuario;
    private ArrayList<Minera> listaMineras;
    private ArrayList<Area> listaAreas;
    private String lastCorrelativo;
    private String[] listaTurnos = {"","1","2","3"};
    private String[] bodegasEmbarque = {"","1","2","3","4","5","6","7","8","9","10","11","12"};

    public String[] getBodegasEmbarque() {
        return bodegasEmbarque;
    }

    public String getLastCorrelativo() {
        return lastCorrelativo;
    }

    public void setLastCorrelativo(String lastCorrelativo) {
        this.lastCorrelativo = lastCorrelativo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    public int getRutUsuario() {
        return rutUsuario;
    }

    public void setRutUsuario(int rutUsuario) {
        this.rutUsuario = rutUsuario;
    }

    public ArrayList<Minera> getListaMineras() {
        return listaMineras;
    }

    public void setListaMineras(ArrayList<Minera> listaMineras) {
        this.listaMineras = listaMineras;
    }

    public ArrayList<Area> getListaAreas() {
        return listaAreas;
    }

    public void setListaAreas(ArrayList<Area> listaAreas) {
        this.listaAreas = listaAreas;
    }

    public String[] getListaTurnos() {
        return listaTurnos;
    }
}
