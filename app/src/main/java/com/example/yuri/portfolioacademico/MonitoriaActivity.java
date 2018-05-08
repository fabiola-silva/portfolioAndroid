package com.example.yuri.portfolioacademico;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MonitoriaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoria);

        Toolbar toolbar = findViewById(R.id.toolbar); //link com a toolbar no xml
        toolbar.setTitle(R.string.titulo_monitoria); //coloca o título na toolbar, é no arquivo string que se deve colocar todos os texto de uma aplicação

        setSupportActionBar(toolbar);//setando suporte de action bar

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //ativando a seta de voltar na toolbar


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){//infla o menu dessa tela
        getMenuInflater().inflate(R.menu.menu_monitoria,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == android.R.id.home){ //verifica se clicou na seta de voltar
            finish();
        }
        return true;
    }

}
