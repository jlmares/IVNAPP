package com.example.xolder.ivnapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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

public class Agendar extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    JSONObject jsonobject, jsonobjectmedios;
    JSONArray jsonarray, jsonarraymedios;
    ProgressDialog mProgressDialog;
    ArrayList<String> worldlist, worldlistMedios;
    ArrayList<Sucursal> world;
    ArrayList<Medios> worldMedios;
    Spinner spinnerSucursal, spinnerMedio;
    EditText etNombre, etCelular, etFecha, etHora, etComentarios;
    Button btnAceptar;
    Context ctx=this;
    String usuario;
    SoapPrimitive response;
    private ArrayList<String> listcitas_sucursales = new ArrayList<String>();
    private ArrayList<String> listcitas_medios = new ArrayList<String>();
    int position_sucursal, position_medios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar);

        usuario = getIntent().getExtras().getString("username");
        spinnerSucursal = (Spinner) findViewById(R.id.spnSucursal);
        spinnerMedio = (Spinner) findViewById(R.id.spnMedio);
        btnAceptar = (Button) findViewById(R.id.btnAceptar);

        etNombre = (EditText) findViewById(R.id.etNombre);
        etCelular = (EditText) findViewById(R.id.etCelular);
        etFecha = (EditText) findViewById(R.id.etFecha);
        etHora = (EditText) findViewById(R.id.etHora);
        etComentarios = (EditText)findViewById(R.id.etComentarios);

        spinnerSucursal.setOnItemSelectedListener(this);
        spinnerMedio.setOnItemSelectedListener(this);
        btnAceptar.setOnClickListener(this);

        // Download JSON file AsyncTask
        new DownloadJSONsucursales().execute();
        new DownloadJSONmedios().execute();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // Download JSON file AsyncTask
    private class DownloadJSONsucursales extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Thread tr = new Thread() {

                @Override
                public void run() {
                    // Metodos de SOAP definidos para su ejecucion
                    String NAMESPACE = "http://demo.org/";
                    //String URL = "http://192.168.0.104/sum/WebService.asmx";
                    String URL = "http://192.168.0.100/sum/WebService.asmx";
                    String METHOD_NAME = "Sucursales";
                    String SOAP_ACTION = "http://demo.org/Sucursales";

                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                    // se define la version del SOAP
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

                            String datos = json_data.getString("nombre");

                            listcitas_sucursales.add(i, datos);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Spinner SucursalSpinner = (Spinner) findViewById(R.id.spnSucursal);

                                // Spinner adapter
                                SucursalSpinner
                                        .setAdapter(new ArrayAdapter<String>(Agendar.this,
                                                android.R.layout.simple_spinner_dropdown_item,
                                                listcitas_sucursales));

                                // Spinner on item click listener
                                SucursalSpinner
                                        .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                            @Override
                                            public void onItemSelected(AdapterView<?> arg0,
                                                                       View arg1, int position, long arg3) {
                                                //Toast.makeText(arg0.getContext(), "Seleccionado: " + position ,
                                                //       Toast.LENGTH_SHORT).show();
                                                // TODO Auto-generated method stub
                                                position_sucursal = position;
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> arg0) {
                                                // TODO Auto-generated method stub
                                            }
                                        });
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

    // Download JSON file AsyncTask
    private class DownloadJSONmedios extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Thread tr = new Thread() {

                @Override
                public void run() {
                    // Metodos de SOAP definidos para su ejecucion
                    String NAMESPACE = "http://demo.org/";
                    //String URL = "http://192.168.0.104/sum/WebService.asmx";
                    String URL = "http://192.168.0.100/sum/WebService.asmx";
                    String METHOD_NAME = "Medios";
                    String SOAP_ACTION = "http://demo.org/Medios";

                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                    // se define la version del SOAP
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

                            String datos = json_data.getString("medios");


                            listcitas_medios.add(i, datos);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Locate the spinner in activity_main.xml
                                Spinner MedioSpinner = (Spinner) findViewById(R.id.spnMedio);

                                // Spinner adapter
                                MedioSpinner
                                        .setAdapter(new ArrayAdapter<String>(Agendar.this,
                                                android.R.layout.simple_spinner_dropdown_item,
                                                listcitas_medios));

                                // Spinner on item click listener
                                MedioSpinner
                                        .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                            @Override
                                            public void onItemSelected(AdapterView<?> arg0,
                                                                       View arg1, int position, long arg3) {
                                                //Toast.makeText(arg0.getContext(), "Seleccionado: " + arg0.getItemAtPosition(position).toString(),
                                                //        Toast.LENGTH_SHORT).show();
                                                // TODO Auto-generated method stub
                                                position_medios = position;
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> arg0) {
                                                // TODO Auto-generated method stub
                                            }
                                        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {

        //Intent intent = new Intent(Agendar.this, Home.class);
        //startActivity(intent);
        //finish();

        Toast.makeText(Agendar.this, "PUTO EL QUE LO LEA", Toast.LENGTH_LONG).show();

        /*Thread tr = new Thread() {

            @Override
            public void run() {
                // Metodos de SOAP definidos para su ejecucion
                String NAMESPACE = "http://demo.org/";
                //String URL = "http://192.168.0.104/sum/WebService.asmx";
                String URL = "http://192.168.0.102/sum/WebService.asmx";
                String METHOD_NAME = "insert_cita_merca";
                String SOAP_ACTION = "http://demo.org/insert_cita_merca";

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                //Se mandan los parametros que necesita la funcion en el WEB SERVICES
                request.addProperty("id_usuario", usuario);
                request.addProperty("id_franquicia", position_sucursal);
                request.addProperty("fecha", etFecha.getText().toString());
                request.addProperty("hora", etHora.getText().toString());
                request.addProperty("alumno", etNombre.getText().toString());
                request.addProperty("alumno_telefono", etCelular.getText().toString());
                request.addProperty("comentarios", etComentarios.getText().toString());
                request.addProperty("id_medios", position_medios);

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


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Agendar.this, "CITA AGENDADA CON EXITO.", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (SoapFault soapFault) {
                    soapFault.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        tr.start();*/


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            Intent intent = new Intent(Agendar.this, Home.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


}
