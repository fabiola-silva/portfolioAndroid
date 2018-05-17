package com.example.yuri.portfolioacademico.atividades;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yuri.portfolioacademico.R;
import com.example.yuri.portfolioacademico.adaptadores.MonitoriaAdaptador;
import com.example.yuri.portfolioacademico.interfaces.RecyclerOnClick;
import com.example.yuri.portfolioacademico.modelos.Monitoria;
import com.example.yuri.portfolioacademico.utils.Conexao;
import com.example.yuri.portfolioacademico.utils.ConverteData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Monitoria2Activity extends AppCompatActivity implements RecyclerOnClick{
    private RecyclerView rvMonitoria;
    private MonitoriaAdaptador monitoriaAdaptador;
    private List<Monitoria> lista = new ArrayList<>();
    private Conexao conexao;
    private SwipeRefreshLayout srRecarregar;
    private LinearLayout llSemInternet,llVazio;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoria2);
        sp = getSharedPreferences("dados_usuario",MODE_PRIVATE);//salva dados locais do usuário

        Toolbar toolbar = findViewById(R.id.toolbar); //link com a toolbar no xml
        toolbar.setTitle(R.string.titulo_monitoria); //coloca o título na toolbar, é no arquivo string que se deve colocar todos os texto de uma aplicação

        setSupportActionBar(toolbar);//setando suporte de action bar

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //ativando a seta de voltar na toolbar

        rvMonitoria = findViewById(R.id.rv_monitoria);
        srRecarregar = findViewById(R.id.sr_recarregar);
        llSemInternet = findViewById(R.id.ll_sem_conexao);
        llVazio = findViewById(R.id.ll_vazio);

        conexao = new Conexao(this);

        //listarMonitoria();//chama método que busca dados

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Monitoria2Activity.this,ViewMonitoria.class);
                intent.putExtra("tipo","cadastrar");
                startActivity(intent);
            }
        });

        srRecarregar.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { //função do swipe
            @Override
            public void onRefresh() {
                listarMonitoria();
            }
        });


    }

    private void listarMonitoria(){
        srRecarregar.setRefreshing(!srRecarregar.isRefreshing());//se não estiver iniado, inicializa
        if(conexao.estaConectado()){//se tiver internet

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.url_base) + getString(R.string.url_listar_monitoria),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            srRecarregar.setRefreshing(false);
                            try {
                                JSONObject jo = new JSONObject(response);
                                if(jo.getString("status").equals("sucesso")){//se encontrar intens
                                    lista.clear();//limpa a lista antiga
                                    JSONArray dados = jo.getJSONArray("dados");
                                    for(int i = 0; i < dados.length(); i++){//percerre a lista de dados
                                        Monitoria monitoria = new Monitoria();
                                        JSONObject joAux = dados.getJSONObject(i);
                                        monitoria.setId(joAux.getString("id"));
                                        monitoria.setDescricao(joAux.getString("descricao"));
                                        monitoria.setFeedback(joAux.getString("feedback"));

                                        ConverteData cd = new ConverteData(joAux.getString("data_cadastro"));//chama função para converter data
                                        String data = cd.converter();
                                        monitoria.setDataCadastro(data);

                                        lista.add(monitoria);
                                    }

                                    rvMonitoria.setVisibility(View.VISIBLE);//mostra a lista
                                    llVazio.setVisibility(View.GONE);
                                    llSemInternet.setVisibility(View.GONE);

                                    preencheLista();//preenche a lsita

                                }else{
                                    rvMonitoria.setVisibility(View.GONE);
                                    llVazio.setVisibility(View.VISIBLE);//mostra a mensagem de vazio
                                    llSemInternet.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Monitoria2Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            srRecarregar.setRefreshing(false);
                            Toast.makeText(Monitoria2Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();//mostra mensagem de erro
                            rvMonitoria.setVisibility(View.VISIBLE);//mostra lista
                            llVazio.setVisibility(View.GONE);
                            llSemInternet.setVisibility(View.GONE);
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> parametros =  new HashMap<>();
                    parametros.put("id_usuario",sp.getString("id_usuario","0"));
                    return parametros;
                }
            };

            requestQueue.add(stringRequest);
        }else{ //se não tiver internet
            srRecarregar.setRefreshing(false);//para de carregar
            rvMonitoria.setVisibility(View.GONE);
            llVazio.setVisibility(View.GONE);
            llSemInternet.setVisibility(View.VISIBLE); //mostra mensagem de sem internet
        }
    }


    private void preencheLista(){
        monitoriaAdaptador = new MonitoriaAdaptador(this,lista);//Instancia adapter
        monitoriaAdaptador.setRecyclerOnClick(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvMonitoria.setAdapter(monitoriaAdaptador);
        rvMonitoria.setLayoutManager(linearLayoutManager);
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


    @Override
    public void onClick(View v, int position) {
        Monitoria monitoria = lista.get(position);
        int id = v.getId();//pega id do item clicado
        Intent intent = new Intent(Monitoria2Activity.this, ViewMonitoria.class);
        switch (id){
            case R.id.iv_editar://se clicou em editar
                intent.putExtra("tipo","alterar");
                intent.putExtra("descricao",monitoria.getDescricao());
                intent.putExtra("feedback",monitoria.getFeedback());
                intent.putExtra("id_monitoria",monitoria.getId());
                startActivity(intent);
                break;
            case R.id.iv_deletar://se clicou em deletar
                alertaDeletar(sp.getString("id_usuario","0"), monitoria.getId());
                break;
            default://se clicar em qualquer parte do card que não seja nos botões anteriores
                intent.putExtra("tipo","visualizar");
                intent.putExtra("descricao",monitoria.getDescricao());
                intent.putExtra("feedback",monitoria.getFeedback());
                startActivity(intent);
        }
    }

    private void alertaDeletar(final String idUsuario, final String idMonitoria){//cria o alerta  que pergunta se quer excluir

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.deseja_deletar))
                .setCancelable(true)
                .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//se escolher que deseja excluir
                        dialog.dismiss();
                        deletar(idUsuario, idMonitoria);//chama método que exclusão
                    }
                })
                .setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();//se escolher que não deseja excluir
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deletar(final String idUsuario, final String idMonitoria){
        if(conexao.estaConectado()){//se tiver internet
            conexao.dialogoCarregamento(this,getString(R.string.processando));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.url_base) + getString(R.string.url_deletar_monitoria),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            conexao.progressDialog.dismiss();
                            Log.i("vai", response);
                            try {
                                JSONObject jo = new JSONObject(response);
                                if(jo.getString("status").equals("sucesso")){//se encontrar intens
                                    Toast.makeText(Monitoria2Activity.this, jo.getString("mensagem"), Toast.LENGTH_SHORT).show();
                                    listarMonitoria();
                                   //monitoriaAdaptador.notifyDataSetChanged();//atualiza lista
                                }else{//se erro
                                    Toast.makeText(Monitoria2Activity.this, jo.getString("mensagem"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Monitoria2Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            conexao.progressDialog.dismiss();
                            Toast.makeText(Monitoria2Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();//mostra mensagem de erro
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> parametros =  new HashMap<>();
                    parametros.put("id_monitoria",idMonitoria);
                    parametros.put("id_usuario",idUsuario);

                    return parametros;
                }
            };

            requestQueue.add(stringRequest);
        }else{ //se não tiver internet
            Toast.makeText(this, R.string.sem_conexao, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        listarMonitoria();//lista monitoria sempre que on start é chamado
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }
}
