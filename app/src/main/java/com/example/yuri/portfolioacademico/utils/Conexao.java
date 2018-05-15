package com.example.yuri.portfolioacademico.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.yuri.portfolioacademico.R;

public class Conexao {
    private Context context;
    public ProgressDialog progressDialog;
    public Conexao (Context context){
        this.context = context;
    }

    public boolean estaConectado(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void dialogoCarregamento(Context context, String message){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }
}
