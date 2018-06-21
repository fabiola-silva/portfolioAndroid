package com.example.yuri.portfolioacademico.atividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class loginActivity extends AppCompatActivity {
    private EditText etEmail,etSenha;
    private Button button;
    private Conexao conexao;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        conexao = new Conexao(this);

        sp = getSharedPreferences("dados_usuario",MODE_PRIVATE);

        etEmail = findViewById(R.id.et_email);
        etSenha = findViewById(R.id.et_senha);

        button = findViewById(R.id.bt_entrar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String senha = etSenha.getText().toString();
                if(!email.trim().isEmpty() && !senha.trim().isEmpty()){
                    entrar(email,senha);
                }else{
                    Toast.makeText(loginActivity.this, R.string.preencha_tudo, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void entrar(final String email, final String senha){
        if(conexao.estaConectado()){
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            conexao.dialogoCarregamento(this,getString(R.string.processando));


            StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.url_base) + getString(R.string.url_login),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            conexao.progressDialog.dismiss();
                            Log.i("josn_vai",response);
                            try {
                                JSONObject jo = new JSONObject(response);
                                if(jo.getString("status").equals("sucesso")){
                                    JSONObject dados = jo.getJSONObject("dados");

                                    SharedPreferences.Editor editor = sp.edit();

                                    editor.putString("email",dados.getString("email"));
                                    editor.putString("nome",dados.getString("nome"));
                                    editor.putString("id_usuario",dados.getString("id"));
                                    editor.putBoolean("logado",true);

                                    editor.apply();
                                    editor.commit();

                                    Intent i = new Intent(loginActivity.this,MainActivity.class);
                                    startActivity(i);

                                    finish();
                                }else{
                                    Toast.makeText(loginActivity.this, jo.getString("mensagem"), Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                conexao.progressDialog.dismiss();
                                e.printStackTrace();

                                Toast.makeText(loginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(loginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> parametros = new HashMap<>();
                    parametros.put("email",email);
                    parametros.put("senha",senha);

                    return parametros;
                }
            };

            requestQueue.add(stringRequest);

        }else{
            Toast.makeText(this, R.string.sem_conexao, Toast.LENGTH_SHORT).show();
        }
    }
}
