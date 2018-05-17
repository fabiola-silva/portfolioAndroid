package com.example.yuri.portfolioacademico.utils;

public class ConverteData {
    private String data;

    public ConverteData(String data) {
        this.data = data;
    }

    public String converter(){
        String data =  this.data.substring(0,10);
        String dia  = data.substring(8,10);
        String mes = data.substring(5,7);
        String ano = data.substring(0,4);

        return dia+"/"+mes+"/"+ano;
    }

}
