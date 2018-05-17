package com.example.yuri.portfolioacademico.adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yuri.portfolioacademico.R;
import com.example.yuri.portfolioacademico.interfaces.RecyclerOnClick;
import com.example.yuri.portfolioacademico.modelos.Monitoria;
import com.example.yuri.portfolioacademico.modelos.Usuario;

import java.util.ArrayList;
import java.util.List;

public class MonitoriaAdaptador extends RecyclerView.Adapter<MonitoriaAdaptador.MonitoriaViewHolder>{
    private Context context;
    private List<Monitoria> lista;
    private RecyclerOnClick recyclerOnClick;

    public MonitoriaAdaptador(Context context, List<Monitoria> lista) {
        this.context = context;
        this.lista = lista;
    }

    public void setRecyclerOnClick(RecyclerOnClick recyclerOnClick) {
        this.recyclerOnClick = recyclerOnClick;
    }

    @NonNull
    @Override
    public MonitoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_monitoria,parent,false);//infla o layout do card
        return new MonitoriaViewHolder(view); //envia o layout inflado para a classe MonitoriaViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull MonitoriaViewHolder holder, int position) {
        Monitoria monitoria = lista.get(position);//pega cada objeto da lista

        holder.tvDescricao.setText(monitoria.getDescricao()); //seta os dados em cada TextView
        holder.tvFeedback.setText(monitoria.getFeedback());
        holder.tvDataCadastro.setText(monitoria.getDataCadastro());
    }

    @Override
    public int getItemCount() {
        return lista.size();//retorna tamanho da lista
    }

    public class MonitoriaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView ivEditar, ivDeletar;
        private TextView tvDescricao, tvFeedback, tvDataCadastro;

        public MonitoriaViewHolder(View itemView) {
            super(itemView);
            //lincando com os botões imagem do card_monitoria
            ivEditar = itemView.findViewById(R.id.iv_editar);
            ivDeletar = itemView.findViewById(R.id.iv_deletar);

            tvDescricao = itemView.findViewById(R.id.tv_descricao);
            tvFeedback = itemView.findViewById(R.id.tv_feedback);
            tvDataCadastro = itemView.findViewById(R.id.tv_data_cadastro);

            itemView.setOnClickListener(this);//ativa click no card indeiro

            ivEditar.setOnClickListener(this);//ativa click no botão de editar

            ivDeletar.setOnClickListener(this);//ativa click no botão excluir

        }

        @Override
        public void onClick(View v) {
            recyclerOnClick.onClick(v,getAdapterPosition());
        }
    }
}
