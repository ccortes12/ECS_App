package com.example.ecs_app.Entidades;

public class Marca {

    private int estado;
    private String descEstado;
    private String codMarca;
    private String descMarca;

    public Marca(int estado, String descEstado, String codMarca, String descMarca) {
        this.estado = estado;
        this.descEstado = descEstado;
        this.codMarca = codMarca;
        this.descMarca = descMarca;
    }

    public Marca(){}

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getDescEstado() {
        return descEstado;
    }

    public void setDescEstado(String descEstado) {
        this.descEstado = descEstado;
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
}
