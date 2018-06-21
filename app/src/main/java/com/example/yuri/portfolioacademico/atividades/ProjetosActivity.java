package com.example.yuri.portfolioacademico.atividades;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;

import com.example.yuri.portfolioacademico.R;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProjetosActivity extends AppCompatActivity {
    private Button btEnviar;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projetos);

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle(R.string.titulo_projetos);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btEnviar = findViewById(R.id.bt_enviar);

        sp = getSharedPreferences("dados_usuario",MODE_PRIVATE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                return;
            }
        }


        ativarBotao();

    }


    private void ativarBotao(){
        btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialFilePicker()
                        .withActivity(ProjetosActivity.this)
                        .withRequestCode(10)
                        .start();
            }
        });
    }


    ProgressDialog progressDialog;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if(requestCode == 10 && resultCode == RESULT_OK){
            progressDialog = new ProgressDialog(ProjetosActivity.this);
            progressDialog.setTitle("Enviando");
            progressDialog.setMessage("Aguarde ...");
            progressDialog.show();

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    File file = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));

                    String contentType = getMimeType(file.getPath());

                    String filePath  = file.getAbsolutePath();

                    OkHttpClient client = new OkHttpClient();
                    Log.i("TAG","2.0");
                    RequestBody fileBody = RequestBody.create(MediaType.parse(contentType),file);

                    Log.i("TAG",contentType);
                    //Log.i("TAG",sp.getString("is_usuario","0")+filePath.substring(filePath.lastIndexOf("/")+1));

                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("type",contentType)
                            .addFormDataPart("uploaded_file",sp.getString("id_usuario","0")+"-"+filePath.substring(filePath.lastIndexOf("/")+1),fileBody)
                            .build();

                    Request request = new Request.Builder()
                            .url(getString(R.string.url_base)+getString(R.string.utl_enviar_projeto))
                            .post(requestBody)
                            .build();

                    try {

                        Response response = client.newCall(request).execute();

                        JSONObject jo = new JSONObject(response.body().string());
                        progressDialog.dismiss();
                        Log.i("TAG",jo.getString("status"));
                        Log.i("TAG",jo.getString("mensagem"));
                        if(jo.getString("status").equals("sucesso")){
                            //Toast.makeText(this, jo.getString("mensagem"), Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            //Toast.makeText(Atividades_FaculdadeActivity.this, jo.getString("mensagem"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.i("TAG",e.getMessage());
                        //Toast.makeText(Atividades_FaculdadeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("TAG",e.getMessage());
                        //Toast.makeText(Atividades_FaculdadeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });


            t.start();
        }
    }

    private String getMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            ativarBotao();
        }else{
            if(Build.VERSION.SDK_INT >=  Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_projetos,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return true;
    }
}
