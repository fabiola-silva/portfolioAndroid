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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{//implementei a classe de click
    private Button btMonitoria,btVoluntariado,btTrabalhos,btProjetos,btAtividades_Faculdade;//cria os botões que vai precisar
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar); //link com a toolbar no xml

        setSupportActionBar(toolbar);//setando suporte de action bar

        sp = getSharedPreferences("dados_usuario",MODE_PRIVATE);//salva dados locais do usuário

        if(!sp.getBoolean("logado",false)){//se não estiver logado
            Intent intent = new Intent(MainActivity.this,loginActivity.class);
            startActivity(intent); //carrega tela de login
            finish(); //fecha essa tela
        }

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
        Intent i = new Intent(MainActivity.this, Monitoria2Activity.class);/** cria um objeto da classe intent,
         o primeiro parametro é a activity que você está, e o segundo é qual activity você quer carregar*/
        startActivity(i); //inicializa activity desejada
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu){//infla o menu dessa tela
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.menu_sair){ //verifica se clicou na seta de voltar
            SharedPreferences.Editor editor = sp.edit();//prepara para editar o arquivos de dados locais do usuario

            editor.clear(); //limpa dados dos usuário fazendo que ele deixe de estar logado
            editor.apply();
            editor.clear();//salva as alterações

            Intent intent = new Intent(MainActivity.this,loginActivity.class);
            startActivity(intent); //carrega tela de login
            finish(); //fecha essa tela
        }
        return true;
    }





}
