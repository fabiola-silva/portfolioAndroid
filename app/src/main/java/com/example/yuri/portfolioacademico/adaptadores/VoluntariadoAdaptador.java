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
import com.example.yuri.portfolioacademico.modelos.Voluntariado;

import java.util.List;

public class VoluntariadoAdaptador extends RecyclerView.Adapter<VoluntariadoAdaptador.VoluntariadoViewHolder>{
    private Context context;
    private List<Voluntariado> lista;
    private RecyclerOnClick recyclerOnClick;

    public VoluntariadoAdaptador(Context context, List<Voluntariado> lista) {
        this.context = context;
        this.lista = lista;
    }

    public void setRecyclerOnClick(RecyclerOnClick recyclerOnClick) {
        this.recyclerOnClick = recyclerOnClick;
    }

    @NonNull
    @Override
    public VoluntariadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_voluntariado,parent,false);
        return new VoluntariadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoluntariadoViewHolder holder, int position) {
        Voluntariado voluntariado = lista.get(position);

        holder.tvDescricao.setText(voluntariado.getDescricao());
        holder.tvFeedback.setText(voluntariado.getFeedback());
        holder.tvDataCadastro.setText(voluntariado.getDataCadastro());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class VoluntariadoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView ivEditar, ivDeletar;
        private TextView tvDescricao, tvFeedback, tvDataCadastro;

        public VoluntariadoViewHolder(View itemView) {
            super(itemView);

            ivEditar = itemView.findViewById(R.id.iv_editar);
            ivDeletar = itemView.findViewById(R.id.iv_deletar);

            tvDescricao = itemView.findViewById(R.id.tv_descricao);
            tvFeedback = itemView.findViewById(R.id.tv_feedback);
            tvDataCadastro = itemView.findViewById(R.id.tv_data_cadastro);

            itemView.setOnClickListener(this);
            ivEditar.setOnClickListener(this);

            ivDeletar.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerOnClick.onClick(v,getAdapterPosition());
        }
    }
}
