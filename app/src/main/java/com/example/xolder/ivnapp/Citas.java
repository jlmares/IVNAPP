package com.example.xolder.ivnapp;

import android.graphics.drawable.Drawable;

/**
 * Created by XOLDER on 16/05/2016.
 */
public class Citas{

    private String alumno;
    private String hora;
    private Drawable imagen;

    public Citas() {
        super();
    }

    public Citas(String alumno, String hora, Drawable imagen) {
        super();
        this.alumno = alumno;
        this.hora = hora;
        this.imagen = imagen;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getAlumno() {
        return alumno;
    }

    public void setAlumno(String alumno) {
        this.alumno = alumno;
    }

    public Drawable getImagen() {
        return imagen;
    }

    public void setImagen(Drawable imagen) {
        this.imagen = imagen;
    }
}
