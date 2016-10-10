package com.example.xolder.ivnapp;

/**
 * Created by XOLDER on 06/05/2016.
 */
public class Medios {

    private int id;
    private String nombre;

    public Medios() {
    }

    public Medios(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
