package com.example.yuri.portfolioacademico;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{//implementei a classe de click
    private Button btMonitoria; //cria os botões que vai precisar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btMonitoria = findViewById(R.id.bt_monitoria);//faz o link do java com o xml, para o java saber a qual elemnto você está referenciando

        btMonitoria.setOnClickListener(this);//habilita click no botão

    }

    private void carregaMonotoria(){
        Intent i = new Intent(MainActivity.this, MonitoriaActivity.class);/** cria um objeto da classe intent,
         o primeiro parametro é a activity que você está, e o segundo é qual activity você quer carregar*/
        startActivity(i); //inicializa activity desejada
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){//verifica o id do botão que foi clicado
            case R.id.bt_monitoria://se clicou em monitoria
                carregaMonotoria();
                break;
            default:
        }
    }
}
