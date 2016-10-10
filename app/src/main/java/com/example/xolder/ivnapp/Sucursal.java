package com.example.xolder.ivnapp;

/**
 * Created by XOLDER on 26/04/2016.
 */
public class Sucursal {

    private int id_franquicia;
    private String nombre;

    public Sucursal(){}

    public Sucursal(int id_franquicia, String nombre) {
        this.id_franquicia = id_franquicia;
        this.nombre = nombre;
    }

    public int getId_franquicia() {
        return id_franquicia;
    }

    public void setId_franquicia(int id_franquicia) {
        this.id_franquicia = id_franquicia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
