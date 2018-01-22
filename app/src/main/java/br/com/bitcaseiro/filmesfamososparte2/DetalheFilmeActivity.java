package br.com.bitcaseiro.filmesfamososparte2;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.bitcaseiro.filmesfamososparte2.adaptadores.ResenhaAdapter;
import br.com.bitcaseiro.filmesfamososparte2.adaptadores.TrailersAdapter;
import br.com.bitcaseiro.filmesfamososparte2.dados.FavoritoContract;
import br.com.bitcaseiro.filmesfamososparte2.utilidades.AsyncTaskDelegate;
import br.com.bitcaseiro.filmesfamososparte2.utilidades.Filme;
import br.com.bitcaseiro.filmesfamososparte2.utilidades.Resenha;
import br.com.bitcaseiro.filmesfamososparte2.utilidades.TheMovieConsulta;
import br.com.bitcaseiro.filmesfamososparte2.utilidades.TheMovieProcessor;
import br.com.bitcaseiro.filmesfamososparte2.utilidades.TheMovieDbRede;
import br.com.bitcaseiro.filmesfamososparte2.utilidades.TheMovieDbService;
import br.com.bitcaseiro.filmesfamososparte2.utilidades.Trailer;

public class DetalheFilmeActivity extends AppCompatActivity
        implements AsyncTaskDelegate, TrailersAdapter.TrailerAdapterOnClickHandler,
        ResenhaAdapter.ResenhaAdapterOnClickHandler {

    private final String CONSULTA_FILME = "CONSULTA_FILME";
    private final String CONSULTA_TRAILER = "CONSULTA_TRAILER";
    private final String CONSULTA_RESENHAS = "CONSULTA_RESENHAS";
    private final String WEB_URI_YOUTUBE = "http://www.youtube.com/watch?v=";
    private final String APP_URI_YOUTUBE = "vnd.youtube:";

    private ImageView mPosterImageView;
    private ArrayList<Trailer> mTrailers;
    private ArrayList<Resenha> mResenhas;
    private Filme mFilme;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(CONSULTA_FILME, mFilme);
        outState.putParcelableArrayList(CONSULTA_TRAILER, mTrailers);
        outState.putParcelableArrayList(CONSULTA_RESENHAS, mResenhas);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_filme);

        Bundle data = getIntent().getExtras();
        Filme filme = data.getParcelable("FILME");
        if (filme == null)
            return;

        if (TheMovieDbRede.temConexaoComInternet(this)) {
            if (savedInstanceState != null && savedInstanceState.containsKey(CONSULTA_FILME)) {
                mFilme = savedInstanceState.getParcelable(CONSULTA_FILME);
                preencherFilme();
            } else {
                new TheMovieDbService(this, CONSULTA_FILME).execute(TheMovieDbRede.construirUrlDetalhesFilme(filme.getFilmeId()));
            }

            if (savedInstanceState != null && savedInstanceState.containsKey(CONSULTA_TRAILER)) {
                mTrailers = savedInstanceState.getParcelableArrayList(CONSULTA_TRAILER);
                preencherTrailers();
            } else {
                new TheMovieDbService(this, CONSULTA_TRAILER).execute(TheMovieDbRede.construirUrlTrailer(filme.getFilmeId()));
            }

            if (savedInstanceState != null && savedInstanceState.containsKey(CONSULTA_RESENHAS)) {
                mResenhas = savedInstanceState.getParcelableArrayList(CONSULTA_RESENHAS);
                preencherResenhas();
            } else {
                new TheMovieDbService(this, CONSULTA_RESENHAS).execute(TheMovieDbRede.construirUrlResenhas(filme.getFilmeId()));
            }
        } else {
            carregarFavoritoDoBancoDeDados(filme.getFilmeId());
        }
    }

    private void carregarFavoritoDoBancoDeDados(Integer filmeId) {

        Cursor cursor = getContentResolver().query(FavoritoContract.FavoritoEntry.CONTENT_URI, FavoritoContract.FavoritoEntry.colunas(),
                FavoritoContract.FavoritoEntry.COLUMN_FILME_ID + " = ?", new String[]{filmeId.toString()}, null);

        if (cursor.moveToFirst()) {

            mFilme = new Filme();
            mFilme.setId(cursor.getInt(cursor.getColumnIndex(FavoritoContract.FavoritoEntry._ID)));
            mFilme.setFilmeId(cursor.getInt(cursor.getColumnIndex(FavoritoContract.FavoritoEntry.COLUMN_FILME_ID)));
            mFilme.setPosterBytes(cursor.getBlob(cursor.getColumnIndex(FavoritoContract.FavoritoEntry.COLUMN_POSTER)));
            mFilme.setDataLancamento(cursor.getString(cursor.getColumnIndex(FavoritoContract.FavoritoEntry.COLUMN_DATA_LANCAMENTO)));
            mFilme.setMediaVotos(cursor.getDouble(cursor.getColumnIndex(FavoritoContract.FavoritoEntry.COLUMN_MEDIA_VOTOS)));
            mFilme.setResumo(cursor.getString(cursor.getColumnIndex(FavoritoContract.FavoritoEntry.COLUMN_RESUMO)));
            mFilme.setTempo(cursor.getInt(cursor.getColumnIndex(FavoritoContract.FavoritoEntry.COLUMN_TEMPO)));
            mFilme.setTitulo(cursor.getString(cursor.getColumnIndex(FavoritoContract.FavoritoEntry.COLUMN_TITULO)));
            mFilme.setTituloOriginal(cursor.getString(cursor.getColumnIndex(FavoritoContract.FavoritoEntry.COLUMN_TITULO_ORIGINAL)));
            preencherFilme();
            cursor.close();
        } else {
            Toast.makeText(this, getText(R.string.aviso_sem_internet), Toast.LENGTH_LONG).show();
            findViewById(R.id.pb_carregando_detalhe).setVisibility(ProgressBar.INVISIBLE);
        }
    }

    @Override
    public void processFinish(Object output, String origem) {
        if (output == null)
            return;

        if (origem.equals(CONSULTA_FILME)) {
            TheMovieConsulta resultado = (TheMovieConsulta) output;

            try {
                mFilme = TheMovieProcessor.processarFilme(new JSONObject(resultado.getJson()));
                preencherFilme();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (origem.equals(CONSULTA_TRAILER)) {
            TheMovieConsulta resposta = (TheMovieConsulta) output;

            try {
                mTrailers = TheMovieProcessor.processarTrailers(new JSONObject(resposta.getJson()));
                preencherTrailers();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (origem.equals(CONSULTA_RESENHAS)) {
            TheMovieConsulta resposta = (TheMovieConsulta) output;

            try {
                mResenhas = TheMovieProcessor.processarResenhas(new JSONObject(resposta.getJson()));
                preencherResenhas();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void preencherResenhas() {
        if (mResenhas.size() > 0) {
            ResenhaAdapter resenhaAdapter = new ResenhaAdapter(this);
            RecyclerView resenhaRecyclerView = (RecyclerView) findViewById(R.id.resenha_recyclerView);

            LinearLayoutManager layoutManage = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            resenhaRecyclerView.setLayoutManager(layoutManage);
            resenhaRecyclerView.setHasFixedSize(false);
            resenhaRecyclerView.setAdapter(resenhaAdapter);

            resenhaAdapter.setResenhas(mResenhas);

        } else {
            TextView semResenha = (TextView) findViewById(R.id.tv_sem_resenhas);
            semResenha.setVisibility(TextView.VISIBLE);
        }
    }

    private void preencherTrailers() {
        if (mTrailers.size() > 0) {
            TrailersAdapter trailersAdapter = new TrailersAdapter(this);
            RecyclerView trailerRecyclerView = (RecyclerView) findViewById(R.id.trailer_recyclerView);

            LinearLayoutManager layoutManage = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            trailerRecyclerView.setLayoutManager(layoutManage);
            trailerRecyclerView.setHasFixedSize(false);
            trailerRecyclerView.setAdapter(trailersAdapter);

            trailersAdapter.setTrailers(mTrailers);
        } else {
            TextView semTrailer = (TextView) findViewById(R.id.tv_sem_trailer);
            semTrailer.setVisibility(TextView.VISIBLE);
        }
    }

    private void preencherFilme() {
        TextView votacaoTextView = (TextView) findViewById(R.id.tv_votacao);
        TextView tempoTextView = (TextView) findViewById(R.id.tv_duracao);
        TextView resumoTextView = (TextView) findViewById(R.id.tv_resumo);
        TextView tituloTextView = (TextView) findViewById(R.id.tv_titulo);
        mPosterImageView = (ImageView) findViewById(R.id.iv_poster);
        TextView dataLancamentoTextView = (TextView) findViewById(R.id.tv_dataLancamento);

        votacaoTextView.setText(getString(R.string.avaliacao, mFilme.getMediaVotos().toString()));
        tempoTextView.setText(getString(R.string.duracao, mFilme.getTempo().toString()));
        resumoTextView.setText(mFilme.getResumo());
        tituloTextView.setText(mFilme.getTitulo() + "(" + mFilme.getTituloOriginal() + ")");
        dataLancamentoTextView.setText(getString(R.string.data_lancamento, mFilme.getDataLancamento().substring(0, 4)));

        if (mFilme.getPosterBytes() == null)
            Picasso.with(getApplicationContext()).load(mFilme.getPoster()).into(mPosterImageView);
        else
            mPosterImageView.setImageBitmap(BitmapFactory.decodeByteArray(mFilme.getPosterBytes(), 0, mFilme.getPosterBytes().length));


        votacaoTextView.setVisibility(TextView.VISIBLE);
        tempoTextView.setVisibility(TextView.VISIBLE);
        resumoTextView.setVisibility(TextView.VISIBLE);
        tituloTextView.setVisibility(TextView.VISIBLE);
        mPosterImageView.setVisibility(TextView.VISIBLE);
        dataLancamentoTextView.setVisibility(TextView.VISIBLE);

        findViewById(R.id.pb_carregando_detalhe).setVisibility(ProgressBar.INVISIBLE);

        if (TheMovieDbRede.temConexaoComInternet(this)) {
            findViewById(R.id.tv_resenhas).setVisibility(TextView.VISIBLE);
            findViewById(R.id.tv_trailers).setVisibility(TextView.VISIBLE);

            if (ehFilmeFavorito())
                findViewById(R.id.bt_remover_favoritos).setVisibility(Button.VISIBLE);
            else
                findViewById(R.id.bt_favoritos).setVisibility(Button.VISIBLE);
        } else {
            findViewById(R.id.bt_remover_favoritos).setVisibility(Button.VISIBLE);
        }
    }

    @Override
    public void onClick(Trailer trailer) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(APP_URI_YOUTUBE + trailer.getKey()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(WEB_URI_YOUTUBE + trailer.getKey()));

        try {
            getApplicationContext().startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            getApplicationContext().startActivity(webIntent);
        }
    }

    @Override
    public void onClick(Resenha resenha) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(resenha.getUrl()));
        startActivity(intent);
    }

    public void salvarFavorito(View view) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(FavoritoContract.FavoritoEntry.COLUMN_DATA_LANCAMENTO, mFilme.getDataLancamento());
        contentValues.put(FavoritoContract.FavoritoEntry.COLUMN_FILME_ID, mFilme.getFilmeId());
        contentValues.put(FavoritoContract.FavoritoEntry.COLUMN_MEDIA_VOTOS, mFilme.getMediaVotos());
        contentValues.put(FavoritoContract.FavoritoEntry.COLUMN_RESUMO, mFilme.getResumo());
        contentValues.put(FavoritoContract.FavoritoEntry.COLUMN_TEMPO, mFilme.getTempo());
        contentValues.put(FavoritoContract.FavoritoEntry.COLUMN_TITULO_ORIGINAL, mFilme.getTituloOriginal());
        contentValues.put(FavoritoContract.FavoritoEntry.COLUMN_TITULO, mFilme.getTitulo());
        contentValues.put(FavoritoContract.FavoritoEntry.COLUMN_POSTER, mFilme.getImagemEmBytes(mPosterImageView));

        Uri uri = getContentResolver().insert(FavoritoContract.FavoritoEntry.CONTENT_URI, contentValues);

        if (uri != null) {
            Toast.makeText(getBaseContext(), getBaseContext().getText(R.string.msg_filme_salvo), Toast.LENGTH_LONG).show();
            findViewById(R.id.bt_remover_favoritos).setVisibility(Button.VISIBLE);
            findViewById(R.id.bt_favoritos).setVisibility(Button.INVISIBLE);
            mFilme.setId(Integer.parseInt(uri.getPathSegments().get(1)));
        }
    }

    public void removerFavorito(View view) {
        Uri uri = FavoritoContract.FavoritoEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(mFilme.getId().toString()).build();
        if (getContentResolver().delete(uri, null, null) > 0) {
            Toast.makeText(getBaseContext(), getBaseContext().getText(R.string.msg_filme_removido), Toast.LENGTH_LONG).show();
            findViewById(R.id.bt_remover_favoritos).setVisibility(Button.INVISIBLE);
            findViewById(R.id.bt_favoritos).setVisibility(Button.VISIBLE);
            mFilme.setId(0);
        }
    }

    private boolean ehFilmeFavorito() {
        Cursor cursor = getContentResolver().query(FavoritoContract.FavoritoEntry.CONTENT_URI, FavoritoContract.FavoritoEntry.colunas(),
                FavoritoContract.FavoritoEntry.COLUMN_FILME_ID + " = ?", new String[]{mFilme.getFilmeId().toString()}, null);

        if (cursor.moveToFirst()) {
            mFilme.setId(cursor.getInt(cursor.getColumnIndex(FavoritoContract.FavoritoEntry._ID)));
            cursor.close();

            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detalhe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.acao_compartilhar == item.getItemId()) {
            if (mTrailers != null && mTrailers.size() > 0) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, getBaseContext().getString(R.string.compartilhar_url));
                intent.putExtra(Intent.EXTRA_TEXT, WEB_URI_YOUTUBE + mTrailers.get(0).getSite());
                startActivity(Intent.createChooser(intent, getBaseContext().getString(R.string.compartilhar_url)));
            } else {
                Toast.makeText(getBaseContext(), getBaseContext().getText(R.string.trailer_nao_encontrado), Toast.LENGTH_LONG).show();
            }

            return true;
        }

        if (android.R.id.home == item.getItemId()) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Bundle data = getIntent().getExtras();

        Intent intent = new Intent();
        intent.putExtra("ULTIMA_ORDENACAO", data.getString("ULTIMA_ORDENACAO"));
        setResult(RESULT_OK, intent);
        finish();

        super.onBackPressed();
    }

}
