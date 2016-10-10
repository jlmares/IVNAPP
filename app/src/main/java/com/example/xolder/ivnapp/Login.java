package com.example.xolder.ivnapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class Login extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    final String TAG = this.getClass().getName();

    Button btnLogin;
    EditText etUsername, etPassword;
    TextView registerLink;
    CheckBox cbRemember;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    boolean checkFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        etUsername = (EditText) findViewById(R.id.etUser);
        etPassword = (EditText) findViewById(R.id.etPassword);
        cbRemember = (CheckBox) findViewById(R.id.cbRemember);
        checkFlag = cbRemember.isChecked();
        //registerLink = (TextView) findViewById(R.id.LinkRegister);
        Log.d(TAG, "chechFlag: " + checkFlag);

        cbRemember.setOnCheckedChangeListener(this);
        btnLogin.setOnClickListener(this);
        //registerLink.setOnClickListener(this);

        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        editor = pref.edit();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:

                Thread nt = new Thread() {
                    String rest;
                    EditText etUser = (EditText) findViewById(R.id.etUser);
                    EditText etPassword = (EditText) findViewById(R.id.etPassword);

                    @Override
                    public void run() {
                        // Metodos de SOAP definidos para su ejecucion
                        String NAMESPACE = "http://demo.org/";
                        //String URL = "http://192.168.0.104/sum/WebService.asmx";
                        String URL = "http://192.168.0.100/sum/WebService.asmx";
                        String METHOD_NAME = "Login";
                        String SOAP_ACTION = "http://demo.org/Login";


                        // se define de donde obtener las variables y valores
                        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                        request.addProperty("id_usuario", Integer.parseInt(etUser.getText().toString()));
                        request.addProperty("contrasena", etPassword.getText().toString());

                        // se define la version del SOAP
                        //SoapObject respuesta = new SoapObject(NAMESPACE, METHOD_NAME);
                        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                        // se define que se usa .NET
                        envelope.dotNet = true;

                        // se manda las variables
                        envelope.setOutputSoapObject(request);

                        // se define a donde se mandara las variables
                        HttpTransportSE transport = new HttpTransportSE(URL);

                        try {
                            // se define que acciones se mandaran a llamar y atrapan excepciones
                            transport.call(SOAP_ACTION, envelope);
                            // se toma el valor a retornar
                            SoapPrimitive resultado_xml = (SoapPrimitive) envelope.getResponse();
                            //Log.e("VALOR DE RESPUESTA: ", resultado_xml.toString());
                            // convertimos el resultado en una cadena
                            rest = resultado_xml.toString();

                            if (rest.equals("Bienvenido.")){
                                Intent intent = new Intent(Login.this, Home.class);
                                intent.putExtra("username", etUsername.getText().toString());
                                startActivity(intent);
                                finish();
                            }


                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Login.this, rest, Toast.LENGTH_LONG).show();
                            }


                        });

                    }


                };
                // se ejecuta el hilo
                nt.start();

            break;
            /*case R.id.LinkRegister:
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                finish();
                break;*/
        }


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        checkFlag = isChecked;
        Log.d(TAG, "chechFlag: " + checkFlag);
    }

}
