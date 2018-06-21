package com.example.yuri.portfolioacademico.atividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.yuri.portfolioacademico.R;

public class Atividades_FaculdadeActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btOficina,btMinicurso,btPalestra;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividades__faculdade);


        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        sp = getSharedPreferences("dados_usuario",MODE_PRIVATE);


        btOficina = findViewById(R.id.bt_oficina);
        btMinicurso = findViewById(R.id.bt_minicurso);
        btPalestra = findViewById(R.id.bt_palestra);

        btOficina.setOnClickListener(this);

        btMinicurso.setOnClickListener(this);

        btPalestra.setOnClickListener(this);


    }


    private void carregaOficina(){
        Intent i = new Intent(Atividades_FaculdadeActivity.this, Oficina2Activity.class);

        startActivity(i);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_oficina:
                carregaOficina();
                break;

            default:
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_atividades_faculdade,menu);
        return true;
    }






}
