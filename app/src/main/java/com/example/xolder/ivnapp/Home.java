package com.example.xolder.ivnapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Home extends AppCompatActivity implements View.OnClickListener {
    /*--------INICIO VARIABLES PARA USUARIO ------------------------------------*/
    final String TAG = this.getClass().getName();

    String Name, Id;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    /*--------FIN VARIABLES PARA USUARIO ------------------------------------*/

    Button btnInsert;

    /*--------INICIO VARIABLES PARA SELECTOR DE FECHA ------------------------------------*/
    private int año;
    private int mes;
    private int dia;
    private int place;
    private Button btnFecha, btnMostrar;
    private EditText etFecha;
    private static final int TIPO_DIALOGO = 0;
    private static DatePickerDialog.OnDateSetListener selectorFecha;
    String usuario;
    JSONObject jsonobject;
    JSONArray jsonarray;
    private ProgressDialog dialogo;
    JSONArray data = null;
    private ArrayList<String> listcitas = new ArrayList<String>();
    ArrayList<Citas> cita = new ArrayList<Citas>();
    private List<String> lista_citas = null;
    private List<Citas> listas_citas = null;
    TextView check;
    ImageView imgAdd;


    SoapPrimitive response = null;

    //Declaracion de variables para serealziar y deserealizar
    //objetos y cadenas JSON
    Gson gson;
    String strJSON;

    /*--------INICIO VARIABLES PARA SELECTOR DE FECHA ------------------------------------*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        usuario = getIntent().getExtras().getString("username");
        etFecha = (EditText) findViewById(R.id.etFechaCampo);
        final Calendar calendar = Calendar.getInstance();
        año = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        imgAdd = (ImageView) findViewById(R.id.imgAdd);
        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        imgAdd.setOnClickListener(this);

        FloatingActionButton fabLogout = (FloatingActionButton) findViewById(R.id.fabLogout);
        fabLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = pref.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(Home.this, Login.class);
                startActivity(intent);
            }
        });

        mostrarFecha();

        selectorFecha = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                año = year;
                mes = monthOfYear;
                dia = dayOfMonth;
                mostrarFecha();
            }
        };


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgAdd:
                Intent intent = new Intent(Home.this, Agendar.class);
                intent.putExtra("username", usuario);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                return new DatePickerDialog(Home.this, selectorFecha, año, mes, dia);
        }
        return null;
    }

    public void mostrarCalendario(View v) {
        showDialog(TIPO_DIALOGO);

    }

    private void mostrarFecha() {
        String strfecha = año + "-" + (mes < 10 ? "0" + (mes + 1) : (mes + 1)) + "-" + (dia < 10 ? "0" + dia : dia);
        etFecha.setText(strfecha);
        new EjecutarCitas().execute();
    }

    private class EjecutarCitas extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Thread tr = new Thread() {

                @Override
                public void run() {
                    // Metodos de SOAP definidos para su ejecucion
                    String NAMESPACE = "http://demo.org/";
                    //String URL = "http://192.168.0.104/sum/WebService.asmx";
                    String URL = "http://192.168.0.100/sum/WebService.asmx";
                    String METHOD_NAME = "Datos";
                    String SOAP_ACTION = "http://demo.org/Datos";

                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                    //Se mandan los parametros que necesita la funcion en el WEB SERVICES
                    request.addProperty("id_usuario", usuario);
                    request.addProperty("fecha", etFecha.getText().toString());

                    // se define la version del SOAP
                    //SoapObject respuesta = new SoapObject(NAMESPACE, METHOD_NAME);
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    // se define que se usa .NET
                    envelope.dotNet = true;

                    // se manda las variables
                    envelope.setOutputSoapObject(request);

                    //envelope.addMapping(NAMESPACE, "Cliente", citas.getClass());
                    String respuesta;

                    // se define a donde se mandara las variables
                    HttpTransportSE transport = new HttpTransportSE(URL);

                    try

                    {
                        // se define que acciones se mandaran a llamar y atrapan excepciones
                        transport.call(SOAP_ACTION, envelope);
                        // se toma el valor a retornar
                        response = (SoapPrimitive) envelope.getResponse();

                        String jArr = response.toString();
                        JSONObject json = new JSONObject(jArr);
                        JSONArray jArray = json.getJSONArray("registros");
                        //lista_citas.clear();

                        System.out.println("*****JARRAY*****" + jArray.length());

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject json_data = jArray.getJSONObject(i);

                            Log.i("log_tag", "_id" + json_data.getInt("id_relacion") +
                                    ", mall_name" + json_data.getString("franquicia") +
                                    ", location" + json_data.getString("empleado")
                            );
                            /*String datos = "Sucursal: " + json_data.getString("franquicia") +
                                    "\nAlumno: " + json_data.getString("alumno") +
                                    "\nTelefono: " + json_data.getString("empleado_telefono") +
                                    "\nHora: " + json_data.getString("hora");*/

                            //cita.add();

                            //listcitas.add(i, String.valueOf(json_data));
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ListView lstView = (ListView) findViewById(R.id.listView);

                                AdapterCitas adapter = new AdapterCitas(Home.this, cita);
                                //ArrayAdapter<String> adapter = new ArrayAdapter<String>(Home.this, android.R.layout.simple_list_item_1, listcitas);
                                //Set the above adapter as the adapter of choice for our list
                                lstView.setAdapter(adapter);
                                Toast.makeText(Home.this, "QUE PASO AQUI", Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (SoapFault soapFault) {
                        soapFault.printStackTrace();
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            tr.start();
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
        }
    }

}


