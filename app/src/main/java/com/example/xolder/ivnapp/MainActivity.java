package com.example.xolder.ivnapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    /*-------------- Variables para manejar los tiempos del Activity y el ProgressBar -----------------*/
    public static final int segundos = 3;
    public static final int milisegundos = segundos*1000;
    public static final int aux = 2;
    public ProgressBar progreso;
    /*-------------- Variables para manejar los tiempos del Activity y el ProgressBar -----------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* METODO PARA HACER UNA ACTIVITY FULL SCREEN*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        /* METODO PARA HACER UNA ACTIVITY FULL SCREEN*/

        /* Enlacamos el ProgressBar de java a la interfaz*/
        progreso = (ProgressBar) findViewById(R.id.progressBar);
        /* Enlacamos el ProgressBar de java a la interfaz*/

        /* Metodo para saber el numero maximo que estara  esperando el Activity para pasar al siguiente Activity*/
        progreso.setMax(maximoprogreso());
        /* Metodo para saber el numero maximo que estara  esperando el Activity para pasar al siguiente Activity*/

        /*Llamada al metodo inicial*/
        iniciar_actividad();
        /*Llamada al metodo inicial*/

    }

    /*Metodo inicial para el tiempo del ProgressBAr*/
    private void iniciar_actividad() {
        new CountDownTimer(milisegundos, 1000) {
            @Override
            public void onTick ( long millisUntilFinished){
                progreso.setProgress(establacerprogreso(millisUntilFinished));
            }

            @Override
            public void onFinish () {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }
    /*Metodo inicial para el tiempo del ProgressBAr*/

    public int establacerprogreso(long miliseconds) {
        return (int) ((milisegundos - miliseconds) / 1000);
    }

    public int maximoprogreso() {
        return segundos - aux;
    }
}
