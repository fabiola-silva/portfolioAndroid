package com.example.yuri.portfolioacademico.atividades;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CadastroActivity extends AppCompatActivity {

    private EditText etNome, etMatricula, etSenha;
    private Button button;
    private Button btEditavel;
    private Conexao conexao;
    private SharedPreferences sp;
    private Bundle bundle;
    private LinearLayout llConteudoEditavel, llConteudoVisivel;
    private TextView tvNome, tvMatricula, tvSenha;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        Toolbar toolbar = findViewById(R.id.toolbar);


        btEditavel = findViewById(R.id.bt_editavel);
        etNome = findViewById(R.id.tv_nome);
        tvNome = findViewById(R.id.tv_nome);
        etSenha = findViewById(R.id.tv_senha);
        tvSenha = findViewById(R.id.tv_senha);
        etMatricula = findViewById(R.id.tv_matricula);
        tvMatricula = findViewById(R.id.tv_matricula);
        llConteudoEditavel = findViewById(R.id.ll_conteudo_editavel);
        llConteudoVisivel = findViewById(R.id.ll_conteudo_visivel);

        conexao = new Conexao(this);
        sp = getSharedPreferences("dados_usuario",MODE_PRIVATE);


        bundle = getIntent().getExtras();

        button = findViewById(R.id.bt_cadastro);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome = etNome.getText().toString();
                String matricula = etMatricula.getText().toString();
                String senha = etSenha.getText().toString();
                if(!nome.trim().isEmpty() && !matricula.trim().isEmpty() && !senha.trim().isEmpty()){
                    cadastro(nome, nome,matricula,senha);
                }else{
                    Toast.makeText(CadastroActivity.this, R.string.preencha_tudo, Toast.LENGTH_SHORT).show();
                }

            }

        });

        if(bundle.getString("tipo").equals("cadastrar")){
            toolbar.setTitle(R.string.cadastrar);
            llConteudoEditavel.setVisibility(View.VISIBLE);
        }else{
            if(bundle.getString("tipo").equals("alterar")){
                toolbar.setTitle(R.string.alterar);
                llConteudoEditavel.setVisibility(View.VISIBLE);
                etNome.setText(bundle.getString("Nome do usuario"));
                etMatricula.setText(bundle.getString("Matricula"));
                etSenha.setText(bundle.getString("Senha"));
                btEditavel.setText(R.string.alterar);
            }else{
                toolbar.setTitle(R.string.conteudo);
                llConteudoVisivel.setVisibility(View.VISIBLE);
                tvNome.setText(bundle.getString("Nome do usuario"));
                tvMatricula.setText(bundle.getString("Matricula"));
                tvSenha.setText(bundle.getString("Senha"));
            }
        }

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btEditavel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = etNome.getText().toString();
                String matricula = etMatricula.getText().toString();
                String senha = etSenha.getText().toString();


                if(!nome.trim().isEmpty() && !matricula.trim().isEmpty() && !senha.trim().isEmpty()){
                    if(bundle.getString("tipo").equals("cadastrar")){
                        cadastro(nome, matricula, senha, sp.getString("id_usuario","0"));
                    }else{
                        alterar(nome, matricula, senha, sp.getString("id_usuario","0"),bundle.getString("id"));
                    }
                }else{
                    Toast.makeText(CadastroActivity.this, R.string.preencha_tudo, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void Registrar (View view){
        Intent intent = new Intent(getApplicationContext(), CadastroActivity.class);
    }

    public void cadastro(String s, final String nome, final String matricula, final String senha){
        if(conexao.estaConectado()){
            conexao.dialogoCarregamento(this,getString(R.string.processando));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.url_base) + getString(R.string.url_cadastrar_usuario),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            conexao.progressDialog.dismiss();

                            try {
                                JSONObject jo = new JSONObject(response);
                                if(jo.getString("status").equals("sucesso")){
                                    Toast.makeText(CadastroActivity.this, jo.getString("mensagem"), Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{//se erro
                                    Toast.makeText(CadastroActivity.this, jo.getString("mensagem"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(CadastroActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            conexao.progressDialog.dismiss();
                            Toast.makeText(CadastroActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> parametros = new HashMap<>();
                    parametros.put("Nome do usuario",nome);
                    parametros.put("Matricula",matricula);
                    parametros.put("Senha",senha);

                    return parametros;
                }
            };

            requestQueue.add(stringRequest);

        }else{
            Toast.makeText(CadastroActivity.this, R.string.sem_conexao, Toast.LENGTH_SHORT).show();
        }
    }





    private void alterar(final String nome, final String matricula, final String senha, final String idUsuario, final String id){
        if(conexao.estaConectado()){
            conexao.dialogoCarregamento(this,getString(R.string.processando));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.url_base) + getString(R.string.url_alterar_usuario),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            conexao.progressDialog.dismiss();
                            try {
                                JSONObject jo = new JSONObject(response);
                                if(jo.getString("status").equals("sucesso")){
                                    Toast.makeText(CadastroActivity.this, jo.getString("mensagem"), Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    Toast.makeText(CadastroActivity.this, jo.getString("mensagem"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(CadastroActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            conexao.progressDialog.dismiss();
                            Toast.makeText(CadastroActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> parametros =  new HashMap<>();
                    parametros.put("Nome do usuario ",nome);
                    parametros.put("Matricula ",matricula);
                    parametros.put("Senha ",senha);
                    parametros.put("id_usuario",idUsuario);
                    parametros.put("id",id);
                    return parametros;
                }
            };

            requestQueue.add(stringRequest);
        }else{
            Toast.makeText(this, R.string.sem_conexao, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_cadastro,menu);
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
