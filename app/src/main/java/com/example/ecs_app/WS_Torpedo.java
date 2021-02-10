package com.example.ecs_app;

import com.example.ecs_app.Entidades.Area;
import com.example.ecs_app.Entidades.Minera;
import com.example.ecs_app.Entidades.Paquete;
import com.example.ecs_app.Entidades.PaqueteManual;

import java.util.ArrayList;

public interface WS_Torpedo {

    String validarCredencial(String... strings);

    ArrayList<Area> ecs_listarAreas();

    ArrayList<Minera> ecs_listaMineras();

    Paquete ecs_BuscarPaquetesCB(String... strings);

    Paquete ecs_BuscarPaquete(String... strings);

    String ecs_RecepcionPaquete(String... strings);

    Integer ingresoGuiaManual(String... strings);

    String ingresoPaqueteManual(String... strings);

    ArrayList<PaqueteManual> ecs_BuscarPaquetesPorCarro(String... strings);

    String ecs_EliminarPaqueteManual(String... strings);

}
