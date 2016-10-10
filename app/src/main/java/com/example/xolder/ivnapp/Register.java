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
import android.widget.EditText;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;

public class Register extends AppCompatActivity implements View.OnClickListener {

    final String TAG = this.getClass().getName();

    EditText etName, etAge, etUsername, etPassword;
    Button btnRegister;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etName = (EditText) findViewById(R.id.etName);
        etAge = (EditText) findViewById(R.id.etAge);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(this);

        preferences = getSharedPreferences("register.conf", Context.MODE_PRIVATE);
        editor = preferences.edit();

    }

    @Override
    public void onClick(View v) {

        HashMap data = new HashMap();
        data.put("txtName", etName.getText().toString());
        data.put("txtAge", etAge.getText().toString());
        data.put("txtUsername", etUsername.getText().toString());
        data.put("txtPassword", etPassword.getText().toString());


        PostResponseAsyncTask task = new PostResponseAsyncTask(Register.this, data, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.d(TAG, s);
                if (s.contains("Insert Register Successfully")) {
                    Toast.makeText(getApplicationContext(), "Usuario registrao con exito!.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Usuario no Registrado.", Toast.LENGTH_LONG).show();
                }
            }
        });

        task.execute("http://192.168.15.209/insert_user.php");
    }
}

