package com.example.yuri.portfolioacademico.atividades;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.yuri.portfolioacademico.R;



public class Atividades_FaculdadeActivity extends AppCompatActivity {
    private Button btOficina,btMinicurso,btPalestra;
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividades__faculdade);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        sp = getSharedPreferences("dados_usuario",MODE_PRIVATE);

        if(!sp.getBoolean("logado",false)){
            Intent intent = new Intent(Atividades_FaculdadeActivity.this,loginActivity.class);
            startActivity(intent);
            finish();
        }

        btOficina = findViewById(R.id.bt_oficina);
        btMinicurso = findViewById(R.id.bt_minicurso);
        btPalestra = findViewById(R.id.bt_palestra);

        btOficina.setOnClickListener((View.OnClickListener) this);

        btMinicurso.setOnClickListener((View.OnClickListener) this);

        btPalestra.setOnClickListener((View.OnClickListener) this);


    }


    private void carregaOficina(){
        Intent i = new Intent(Atividades_FaculdadeActivity.this, Oficina2Activity.class);
        startActivity(i);
    }



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
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.menu_sair){
            SharedPreferences.Editor editor = sp.edit();

            editor.clear();
            editor.apply();
            editor.clear();

            Intent intent = new Intent(Atividades_FaculdadeActivity.this,loginActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }





}
