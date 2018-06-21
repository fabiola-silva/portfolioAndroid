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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btMonitoria,btVoluntariado,btTrabalhos,btProjetos,btAtividades_Faculdade;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        sp = getSharedPreferences("dados_usuario",MODE_PRIVATE);

        if(!sp.getBoolean("logado",false)){
            Intent intent = new Intent(MainActivity.this,loginActivity.class);
            startActivity(intent);
            finish();
        }

        btMonitoria = findViewById(R.id.bt_monitoria);
        btVoluntariado = findViewById(R.id.bt_voluntariado);
        btTrabalhos = findViewById(R.id.bt_trabalhos);
        btProjetos = findViewById(R.id.bt_projetos);
        btAtividades_Faculdade = findViewById(R.id.bt_atividades_faculdade);


        btMonitoria.setOnClickListener(this);

        btVoluntariado.setOnClickListener(this);

        btTrabalhos.setOnClickListener(this);

        btProjetos.setOnClickListener(this);

        btAtividades_Faculdade.setOnClickListener(this);

        }


    private void carregaMonotoria(){
        Intent i = new Intent(MainActivity.this, Monitoria2Activity.class);
        startActivity(i);
    }

    private void carregaVoluntariado(){
        Intent i = new Intent(MainActivity.this, Voluntariado2Activity.class);

        startActivity(i);
    }


    private void carregaTrabalhos(){
        Intent i = new Intent(MainActivity.this, TrabalhosActivity.class);

        startActivity(i);
    }

    private void carregaProjetos(){
        Intent i = new Intent(MainActivity.this, ProjetosActivity.class);

        startActivity(i);
    }

    private void carregaAtividade_Faculdade(){
        Intent i = new Intent(MainActivity.this, Atividades_FaculdadeActivity.class);

        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_monitoria:
                carregaMonotoria();
                break;
            case R.id.bt_voluntariado:
                carregaVoluntariado();
                break;
            case R.id.bt_trabalhos:
                carregaTrabalhos();
                break;
            case R.id.bt_projetos:
                carregaProjetos();
                break;
            case R.id.bt_atividades_faculdade:
                carregaAtividade_Faculdade();
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

            Intent intent = new Intent(MainActivity.this,loginActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }





}
