package br.com.bitcaseiro.filmesfamososparte2.adaptadores;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.bitcaseiro.filmesfamososparte2.R;
import br.com.bitcaseiro.filmesfamososparte2.utilidades.Resenha;

public class ResenhaAdapter extends
        RecyclerView.Adapter<ResenhaAdapter.ResenhaAdapterViewHolder> {

    public ResenhaAdapter(ResenhaAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    private List<Resenha> mResenha;
    private Context mContext;
    private final ResenhaAdapterOnClickHandler mClickHandler;

    public interface ResenhaAdapterOnClickHandler {
        void onClick(Resenha resenha);
    }

    @Override
    public ResenhaAdapter.ResenhaAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.resenha_item, parent, false);
        return new ResenhaAdapter.ResenhaAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResenhaAdapter.ResenhaAdapterViewHolder holder, int position) {
        holder.getTextViewTexto().setText(mResenha.get(position).getTexto());
        holder.getTextViewAutor().setText(String.format(mContext.getString(R.string.autor), mResenha.get(position).getAutor().trim()));
    }

    @Override
    public int getItemCount() {
        if (mResenha == null)
            return 0;

        return mResenha.size();
    }

    public void setResenhas(List<Resenha> resenhas) {
        mResenha = resenhas;
        notifyDataSetChanged();
    }

    public class ResenhaAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private final TextView mTexto;
        private final TextView mAutor;

        public final TextView getTextViewTexto() {
            return mTexto;
        }

        public final TextView getTextViewAutor() {
            return mAutor;
        }

        public ResenhaAdapterViewHolder(View itemView) {
            super(itemView);
            mTexto = (TextView) itemView.findViewById((R.id.tv_texto));
            mAutor = (TextView) itemView.findViewById((R.id.tv_autor));

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickHandler.onClick(mResenha.get(getAdapterPosition()));
        }
    }
}
