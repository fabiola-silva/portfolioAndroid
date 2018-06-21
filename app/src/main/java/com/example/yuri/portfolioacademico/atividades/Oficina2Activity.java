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
import com.example.yuri.portfolioacademico.adaptadores.VoluntariadoAdaptador;
import com.example.yuri.portfolioacademico.interfaces.RecyclerOnClick;
import com.example.yuri.portfolioacademico.modelos.Voluntariado;
import com.example.yuri.portfolioacademico.utils.Conexao;
import com.example.yuri.portfolioacademico.utils.ConverteData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Oficina2Activity extends AppCompatActivity implements RecyclerOnClick{
    private RecyclerView rvVoluntariado;
    private VoluntariadoAdaptador voluntariadoAdaptador;
    private List<Voluntariado> lista = new ArrayList<>();
    private Conexao conexao;
    private SwipeRefreshLayout srRecarregar;
    private LinearLayout llSemInternet,llVazio;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voluntariado2);
        sp = getSharedPreferences("dados_usuario",MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.titulo_oficina);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvVoluntariado = findViewById(R.id.rv_voluntariado);
        srRecarregar = findViewById(R.id.sr_recarregar);
        llSemInternet = findViewById(R.id.ll_sem_conexao);
        llVazio = findViewById(R.id.ll_vazio);

        conexao = new Conexao(this);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Oficina2Activity.this,ViewVoluntariado.class);
                intent.putExtra("tipo","cadastrar");
                startActivity(intent);
            }
        });

        srRecarregar.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { //função do swipe
            @Override
            public void onRefresh() {
                listarVoluntariado();
            }
        });


    }

    private void listarVoluntariado(){
        srRecarregar.setRefreshing(!srRecarregar.isRefreshing());
        if(conexao.estaConectado()){

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.url_base) + getString(R.string.url_listar_voluntariado),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            srRecarregar.setRefreshing(false);
                            try {
                                JSONObject jo = new JSONObject(response);
                                if(jo.getString("status").equals("sucesso")){
                                    lista.clear();
                                    JSONArray dados = jo.getJSONArray("dados");
                                    for(int i = 0; i < dados.length(); i++){
                                        Voluntariado voluntariado = new Voluntariado();
                                        JSONObject joAux = dados.getJSONObject(i);
                                        voluntariado.setId(joAux.getString("id"));
                                        voluntariado.setDescricao(joAux.getString("descricao"));
                                        voluntariado.setFeedback(joAux.getString("feedback"));

                                        ConverteData cd = new ConverteData(joAux.getString("data_cadastro"));
                                        String data = cd.converter();
                                        voluntariado.setDataCadastro(data);

                                        lista.add(voluntariado);
                                    }

                                    rvVoluntariado.setVisibility(View.VISIBLE);
                                    llVazio.setVisibility(View.GONE);
                                    llSemInternet.setVisibility(View.GONE);

                                    preencheLista();

                                }else{
                                    rvVoluntariado.setVisibility(View.GONE);
                                    llVazio.setVisibility(View.VISIBLE);
                                    llSemInternet.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Oficina2Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            srRecarregar.setRefreshing(false);
                            Toast.makeText(Oficina2Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            rvVoluntariado.setVisibility(View.VISIBLE);
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
        }else{
            srRecarregar.setRefreshing(false);
            rvVoluntariado.setVisibility(View.GONE);
            llVazio.setVisibility(View.GONE);
            llSemInternet.setVisibility(View.VISIBLE);
        }
    }


    private void preencheLista(){
        voluntariadoAdaptador = new VoluntariadoAdaptador(this,lista);
        voluntariadoAdaptador.setRecyclerOnClick(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvVoluntariado.setAdapter(voluntariadoAdaptador);
        rvVoluntariado.setLayoutManager(linearLayoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_voluntariado,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return true;
    }


    @Override
    public void onClick(View v, int position) {
        Voluntariado voluntariado = lista.get(position);
        int id = v.getId();
        Intent intent = new Intent(Oficina2Activity.this, ViewVoluntariado.class);
        switch (id){
            case R.id.iv_editar:
                intent.putExtra("tipo","alterar");
                intent.putExtra("descricao",voluntariado.getDescricao());
                intent.putExtra("feedback",voluntariado.getFeedback());
                intent.putExtra("id_voluntariado",voluntariado.getId());
                startActivity(intent);
                break;
            case R.id.iv_deletar:
                alertaDeletar(sp.getString("id_usuario","0"), voluntariado.getId());
                break;
            default:
                intent.putExtra("tipo","visualizar");
                intent.putExtra("descricao",voluntariado.getDescricao());
                intent.putExtra("feedback",voluntariado.getFeedback());
                startActivity(intent);
        }
    }

    private void alertaDeletar(final String idUsuario, final String idVoluntariado){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.deseja_deletar))
                .setCancelable(true)
                .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deletar(idUsuario, idVoluntariado);
                    }
                })
                .setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deletar(final String idUsuario, final String idVoluntariado){
        if(conexao.estaConectado()){
            conexao.dialogoCarregamento(this,getString(R.string.processando));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.url_base) + getString(R.string.url_deletar_voluntariado),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            conexao.progressDialog.dismiss();
                            Log.i("vai", response);
                            try {
                                JSONObject jo = new JSONObject(response);
                                if(jo.getString("status").equals("sucesso")){
                                    Toast.makeText(Oficina2Activity.this, jo.getString("mensagem"), Toast.LENGTH_SHORT).show();
                                    listarVoluntariado();
                                   //monitoriaAdaptador.notifyDataSetChanged();//atualiza lista
                                }else{
                                    Toast.makeText(Oficina2Activity.this, jo.getString("mensagem"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Oficina2Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            conexao.progressDialog.dismiss();
                            Toast.makeText(Oficina2Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> parametros =  new HashMap<>();
                    parametros.put("id_voluntariado",idVoluntariado);
                    parametros.put("id_usuario",idUsuario);

                    return parametros;
                }
            };

            requestQueue.add(stringRequest);
        }else{
            Toast.makeText(this, R.string.sem_conexao, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        listarVoluntariado();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }
}
