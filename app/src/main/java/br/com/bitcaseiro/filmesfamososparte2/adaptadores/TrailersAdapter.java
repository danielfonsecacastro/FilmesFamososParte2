package br.com.bitcaseiro.filmesfamososparte2.adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.bitcaseiro.filmesfamososparte2.R;
import br.com.bitcaseiro.filmesfamososparte2.utilidades.Trailer;


public class TrailersAdapter extends
        RecyclerView.Adapter<TrailersAdapter.TrailersAdapterViewHolder> {

    public TrailersAdapter(TrailerAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    private List<Trailer> mTrailer;
    private final TrailerAdapterOnClickHandler mClickHandler;

    public interface TrailerAdapterOnClickHandler {
        void onClick(Trailer trailer);
    }

    @Override
    public TrailersAdapter.TrailersAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.trailer_item, parent, false);
        return new TrailersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailersAdapterViewHolder holder, int position) {
        holder.getTextViewNome().setText(mTrailer.get(position).getNome());
    }

    @Override
    public int getItemCount() {
        if (mTrailer == null)
            return 0;

        return mTrailer.size();
    }

    public void setTrailers(List<Trailer> trailer) {
        mTrailer = trailer;
        notifyDataSetChanged();
    }

    public class TrailersAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private final TextView mNome;

        public final TextView getTextViewNome() {
            return mNome;
        }

        public TrailersAdapterViewHolder(View itemView) {
            super(itemView);
            mNome = (TextView) itemView.findViewById((R.id.tv_nome));
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickHandler.onClick(mTrailer.get(getAdapterPosition()));
        }
    }
}
