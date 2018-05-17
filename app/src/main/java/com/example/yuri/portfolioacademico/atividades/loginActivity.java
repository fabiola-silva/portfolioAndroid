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

        sp = getSharedPreferences("dados_usuario",MODE_PRIVATE);//salva dados locais do usuário

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
            RequestQueue requestQueue = Volley.newRequestQueue(this);//cria lista de requisição
            conexao.dialogoCarregamento(this,getString(R.string.processando));//chama o dialogo de carregamento passando a mensagem ser mostrada

            /** primeiro parametro é o tipo de requisição POST ou GET,
             *  o segundo é a url que deve ser accessada,
             * note que URL base sempre é a mesma, o que muda é a do lado dela */
            StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.url_base) + getString(R.string.url_login),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            conexao.progressDialog.dismiss();//encerra dialogo de carregamento
                            //se sucesso na requisição ele chama essa parte
                            Log.i("josn_vai",response);
                            try {
                                JSONObject jo = new JSONObject(response);
                                if(jo.getString("status").equals("sucesso")){//verifica se retornou dados
                                    JSONObject dados = jo.getJSONObject("dados");//pega o objeto JSON com os dados do usuário

                                    SharedPreferences.Editor editor = sp.edit();//prepara para editar o arquivos de dados locais do usuario

                                    editor.putString("email",dados.getString("email")); //salva dados localmente para usar quando necessário
                                    editor.putString("nome",dados.getString("nome"));
                                    editor.putString("id_usuario",dados.getString("id"));
                                    editor.putBoolean("logado",true);//seta que foi feito o login

                                    editor.apply();//aplica as alterações
                                    editor.commit();//salva as alterações

                                    Intent i = new Intent(loginActivity.this,MainActivity.class); //seta a tela que tem que ir
                                    startActivity(i); //chama a outra tela

                                    finish(); //fecha a tela atual

                                }else{//se não encontrou usuário
                                    Toast.makeText(loginActivity.this, jo.getString("mensagem"), Toast.LENGTH_SHORT).show();
                                    //mostra a mensagem de erro do retorno
                                }
                            } catch (JSONException e) {
                                conexao.progressDialog.dismiss();//encerra dialogo de carregamento
                                e.printStackTrace();
                                //aqui mostra erros referente a JSOM
                                Toast.makeText(loginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //se erro na requisição ele chama essa parte
                            Toast.makeText(loginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            //se der erro aqui é mostrado a mensagem de erro
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {//aqui são setados os posts a serem enviados
                    HashMap<String,String> parametros = new HashMap<>();
                    parametros.put("email",email);/** primeiro parametro é o name do post, o segundo é o valor*/
                    parametros.put("senha",senha);

                    return parametros; //retorna os posts
                }
            };

            requestQueue.add(stringRequest);
            //adiciona requisição
        }else{
            Toast.makeText(this, R.string.sem_conexao, Toast.LENGTH_SHORT).show();
        }
    }
}
