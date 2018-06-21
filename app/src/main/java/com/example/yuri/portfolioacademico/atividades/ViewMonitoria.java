package com.example.yuri.portfolioacademico.atividades;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yuri.portfolioacademico.R;
import com.example.yuri.portfolioacademico.utils.Conexao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewMonitoria extends AppCompatActivity {
    private EditText etDescricao,etFeedback;
    private Button btEditavel;
    private Conexao conexao;
    private SharedPreferences sp;
    private Bundle bundle;
    private LinearLayout llConteudoEditavel, llConteudoVisivel;
    private TextView tvDescricao,tvFeedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_monitoria);
        Toolbar toolbar = findViewById(R.id.toolbar);

        btEditavel = findViewById(R.id.bt_editavel);
        etDescricao =  findViewById(R.id.et_descricao);
        etFeedback = findViewById(R.id.et_feedback);
        tvDescricao = findViewById(R.id.tv_descricao);
        tvFeedback= findViewById(R.id.tv_feedback);
        llConteudoEditavel = findViewById(R.id.ll_conteudo_editavel);
        llConteudoVisivel = findViewById(R.id.ll_conteudo_visivel);

        conexao = new Conexao(this);
        sp = getSharedPreferences("dados_usuario",MODE_PRIVATE);


        bundle = getIntent().getExtras();

        if(bundle.getString("tipo").equals("cadastrar")){
            toolbar.setTitle(R.string.cadastrar);
            llConteudoEditavel.setVisibility(View.VISIBLE);
        }else{
            if(bundle.getString("tipo").equals("alterar")){
                toolbar.setTitle(R.string.alterar);
                llConteudoEditavel.setVisibility(View.VISIBLE);
                etDescricao.setText(bundle.getString("descricao"));
                etFeedback.setText(bundle.getString("feedback"));
                btEditavel.setText(R.string.alterar);
            }else{
                toolbar.setTitle(R.string.conteudo);
                llConteudoVisivel.setVisibility(View.VISIBLE);
                tvDescricao.setText(bundle.getString("descricao"));
                tvFeedback.setText(bundle.getString("feedback"));
            }
        }

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btEditavel.setOnClickListener(new View.OnClickListener() {//click do botão editavel
            @Override
            public void onClick(View v) {
                String descricao = etDescricao.getText().toString();
                String feedback = etFeedback.getText().toString();

                if(!descricao.trim().isEmpty() && !feedback.trim().isEmpty()){
                    if(bundle.getString("tipo").equals("cadastrar")){
                        cadastar(descricao, feedback, sp.getString("id_usuario","0"));
                    }else{
                        alterar(descricao, feedback, sp.getString("id_usuario","0"),bundle.getString("id_monitoria"));
                    }
                }else{
                    Toast.makeText(ViewMonitoria.this, R.string.preencha_tudo, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void cadastar(final String descricao, final String feedback, final String idUsuario){
        if(conexao.estaConectado()){
            conexao.dialogoCarregamento(this,getString(R.string.processando));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.url_base) + getString(R.string.url_cadastrar_monitoria),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            conexao.progressDialog.dismiss();
                            try {
                                JSONObject jo = new JSONObject(response);
                                if(jo.getString("status").equals("sucesso")){
                                    Toast.makeText(ViewMonitoria.this, jo.getString("mensagem"), Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{//se erro
                                    Toast.makeText(ViewMonitoria.this, jo.getString("mensagem"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(ViewMonitoria.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            conexao.progressDialog.dismiss();
                            Toast.makeText(ViewMonitoria.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> parametros =  new HashMap<>();
                    parametros.put("descricao",descricao);
                    parametros.put("feedback",feedback);
                    parametros.put("id_usuario",idUsuario);
                    return parametros;
                }
            };

            requestQueue.add(stringRequest);
        }else{
            Toast.makeText(this, R.string.sem_conexao, Toast.LENGTH_SHORT).show();
        }
    }

    private void alterar(final String descricao, final String feedback, final String idUsuario, final String idMonitoria){
        if(conexao.estaConectado()){
            conexao.dialogoCarregamento(this,getString(R.string.processando));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.url_base) + getString(R.string.url_alterar_monitoria),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            conexao.progressDialog.dismiss();
                            try {
                                JSONObject jo = new JSONObject(response);
                                if(jo.getString("status").equals("sucesso")){
                                    Toast.makeText(ViewMonitoria.this, jo.getString("mensagem"), Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{//se erro
                                    Toast.makeText(ViewMonitoria.this, jo.getString("mensagem"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(ViewMonitoria.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            conexao.progressDialog.dismiss();
                            Toast.makeText(ViewMonitoria.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> parametros =  new HashMap<>();
                    parametros.put("descricao",descricao);
                    parametros.put("feedback",feedback);
                    parametros.put("id_usuario",idUsuario);
                    parametros.put("id_monitoria",idMonitoria);
                    return parametros;
                }
            };

            requestQueue.add(stringRequest);
        }else{
            Toast.makeText(this, R.string.sem_conexao, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){//infla o menu dessa tela
        getMenuInflater().inflate(R.menu.menu_monitoria,menu);
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


}
