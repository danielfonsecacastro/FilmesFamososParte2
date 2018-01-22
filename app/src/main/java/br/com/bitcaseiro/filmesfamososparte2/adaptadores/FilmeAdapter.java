package br.com.bitcaseiro.filmesfamososparte2.adaptadores;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.bitcaseiro.filmesfamososparte2.R;
import br.com.bitcaseiro.filmesfamososparte2.utilidades.Filme;

public class FilmeAdapter extends RecyclerView.Adapter<FilmeAdapter.FilmeAdapterViewHolder> {

    public FilmeAdapter(FilmeAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    private List<Filme> mFilmes;
    private Context mContext;

    private final FilmeAdapterOnClickHandler mClickHandler;

    public interface FilmeAdapterOnClickHandler {
        void onClick(Filme filme);
    }

    @Override
    public FilmeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.filme_item, parent, false);
        view.setMinimumHeight(parent.getMeasuredHeight() / 2);
        return new FilmeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FilmeAdapterViewHolder holder, int position) {
        byte[] imagem = mFilmes.get(position).getPosterBytes();

        if (imagem == null)
            Picasso.with(mContext).load(mFilmes.get(position).getPoster()).into(holder.getPosterImageView());
        else
            holder.getPosterImageView().setImageBitmap(BitmapFactory.decodeByteArray(imagem, 0, imagem.length));
    }

    @Override
    public int getItemCount() {
        if (mFilmes == null)
            return 0;

        return mFilmes.size();
    }

    public void setFilmes(List<Filme> filmes) {
        mFilmes = filmes;
        notifyDataSetChanged();
    }


    public class FilmeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView mPosterImageView;
        public final ImageView getPosterImageView(){return  mPosterImageView;}

        public FilmeAdapterViewHolder(View itemView) {
            super(itemView);
            mPosterImageView = (ImageView) itemView.findViewById((R.id.iv_poster));

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickHandler.onClick(mFilmes.get(getAdapterPosition()));
        }
    }
}
