package com.example.yuri.portfolioacademico;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{//implementei a classe de click
    private Button btMonitoria,btVoluntariado,btTrabalhos,btProjetos,btAtividades_Faculdade;//cria os botões que vai precisar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btMonitoria = findViewById(R.id.bt_monitoria);//faz o link do java com o xml, para o java saber a qual elemnto você está referenciando
        btVoluntariado = findViewById(R.id.bt_voluntariado);
        btTrabalhos = findViewById(R.id.bt_trabalhos);
        btProjetos = findViewById(R.id.bt_projetos);
        btAtividades_Faculdade = findViewById(R.id.bt_atividades_faculdade);

        //o erro era que você estava separando por ponto, o certo é por igual
        //btVoluntariado.findViewById(R.id.bt_voluntariado);

        btMonitoria.setOnClickListener(this);//habilita click no botão

        btVoluntariado.setOnClickListener(this);

        btTrabalhos.setOnClickListener(this);

        btProjetos.setOnClickListener(this);

        btAtividades_Faculdade.setOnClickListener(this);

        }


    private void carregaMonotoria(){
        Intent i = new Intent(MainActivity.this, MonitoriaActivity.class);/** cria um objeto da classe intent,
         o primeiro parametro é a activity que você está, e o segundo é qual activity você quer carregar*/
        startActivity(i); //inicializa activity desejada
    }

    private void carregaVoluntariado(){
        Intent i = new Intent(MainActivity.this, VoluntariadoActivity.class);

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
        switch (v.getId()){//verifica o id do botão que foi clicado
            case R.id.bt_monitoria://se clicou em monitoria
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








}
