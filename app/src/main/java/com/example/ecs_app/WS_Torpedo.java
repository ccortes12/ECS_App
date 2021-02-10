package com.example.ecs_app;

import com.example.ecs_app.Entidades.Area;
import com.example.ecs_app.Entidades.Minera;

import java.util.ArrayList;

public interface WS_Torpedo {

    String validarCredencial(String... strings);

    ArrayList<Area> ecs_listarAreas();

    ArrayList<Minera> ecs_listaMineras();

}
