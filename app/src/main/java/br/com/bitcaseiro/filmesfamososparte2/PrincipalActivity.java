package br.com.bitcaseiro.filmesfamososparte2;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.bitcaseiro.filmesfamososparte2.adaptadores.FilmeAdapter;
import br.com.bitcaseiro.filmesfamososparte2.dados.FavoritoContract;
import br.com.bitcaseiro.filmesfamososparte2.utilidades.AsyncTaskDelegate;
import br.com.bitcaseiro.filmesfamososparte2.utilidades.Filme;
import br.com.bitcaseiro.filmesfamososparte2.utilidades.TheMovieConsulta;
import br.com.bitcaseiro.filmesfamososparte2.utilidades.TheMovieProcessor;
import br.com.bitcaseiro.filmesfamososparte2.utilidades.TheMovieDbRede;
import br.com.bitcaseiro.filmesfamososparte2.utilidades.TheMovieDbService;

public class PrincipalActivity extends AppCompatActivity implements FilmeAdapter.FilmeAdapterOnClickHandler, AsyncTaskDelegate {

    private static final String FILMES_POPULARES = "FILMES_POPULARES";
    private static final String FILMES_RECOMENDADOS = "FILMES_RECOMENDADOS";
    private static final String FILMES_FAVORITOS = "FILMES_FAVORITOS";
    private static final String ULTIMA_ORDENACAO = "ULTIMA_ORDENACAO";
    private static final String POPULARES = "Populares";
    private static final String RECOMENDADOS = "Recomendados";
    private static final String FAVORITOS = "Favoritos";
    private static final int REQUEST_CODE_FILME = 888;

    private FilmeAdapter mFilmeAdapter;
    private ProgressBar mCarregandoProgressBar;
    private TextView mMensagemErroTextView;
    private ArrayList<Filme> mFilmesPopulares;
    private ArrayList<Filme> mFilmesRecomendados;
    private ArrayList<Filme> mFilmesFavoritos;
    private String mUltimaOrdenacao;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(FILMES_POPULARES, mFilmesPopulares);
        outState.putParcelableArrayList(FILMES_RECOMENDADOS, mFilmesRecomendados);
        outState.putParcelableArrayList(FILMES_FAVORITOS, mFilmesFavoritos);
        outState.putString(ULTIMA_ORDENACAO, mUltimaOrdenacao);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        if (savedInstanceState != null && savedInstanceState.containsKey(FILMES_POPULARES))
            mFilmesPopulares = savedInstanceState.getParcelableArrayList(FILMES_POPULARES);

        if (savedInstanceState != null && savedInstanceState.containsKey(FILMES_RECOMENDADOS))
            mFilmesRecomendados = savedInstanceState.getParcelableArrayList(FILMES_RECOMENDADOS);

        if (savedInstanceState != null && savedInstanceState.containsKey(FILMES_FAVORITOS))
            mFilmesFavoritos = savedInstanceState.getParcelableArrayList(FILMES_FAVORITOS);

        if (savedInstanceState != null && savedInstanceState.containsKey(ULTIMA_ORDENACAO))
            mUltimaOrdenacao = savedInstanceState.getString(ULTIMA_ORDENACAO);

        mCarregandoProgressBar = (ProgressBar) findViewById(R.id.pb_carregando);
        mMensagemErroTextView = (TextView) findViewById(R.id.tv_mensagem_erro);

        mFilmeAdapter = new FilmeAdapter(this);

        RecyclerView filmeRecyclerView = (RecyclerView) findViewById(R.id.filme_recyclerView);

        GridLayoutManager layoutManage = new GridLayoutManager(this, 2);
        filmeRecyclerView.setLayoutManager(layoutManage);
        filmeRecyclerView.setHasFixedSize(true);
        filmeRecyclerView.setAdapter(mFilmeAdapter);

        if (TheMovieDbRede.temConexaoComInternet(this)) {
            if (mUltimaOrdenacao == null || mUltimaOrdenacao.isEmpty()) {
                carregarPopulares();
            } else {
                if (mUltimaOrdenacao.equals(POPULARES))
                    carregarPopulares();
                if (mUltimaOrdenacao.equals(RECOMENDADOS))
                    carregarRecomendados();
                if (mUltimaOrdenacao.equals(FAVORITOS))
                    carregarFavoritos();
            }

        } else {
            Toast.makeText(getApplicationContext(), getBaseContext().getString(R.string.aviso_sem_internet), Toast.LENGTH_LONG).show();
            carregarFavoritos();
        }
    }

    private void carregarPopulares() {
        try {
            setTitle(String.format(getBaseContext().getString(R.string.active_title), POPULARES));
            mUltimaOrdenacao = POPULARES;

            if (mFilmesPopulares == null) {
                mCarregandoProgressBar.setVisibility(View.VISIBLE);
                new TheMovieDbService(this).execute(TheMovieDbRede.construirUrlPopulares());
                return;
            }

            mFilmeAdapter.setFilmes(mFilmesPopulares);
        } catch (Exception e) {
            mCarregandoProgressBar.setVisibility(View.INVISIBLE);
            mMensagemErroTextView.setVisibility(View.VISIBLE);
        }
    }

    private void carregarRecomendados() {
        try {
            setTitle(String.format(getBaseContext().getString(R.string.active_title), RECOMENDADOS));
            mUltimaOrdenacao = RECOMENDADOS;

            if (mFilmesRecomendados == null) {
                mCarregandoProgressBar.setVisibility(View.VISIBLE);
                new TheMovieDbService(this).execute(TheMovieDbRede.construirUrlRecomendados());
                return;
            }

            mFilmeAdapter.setFilmes(mFilmesRecomendados);
        } catch (Exception e) {
            mCarregandoProgressBar.setVisibility(View.INVISIBLE);
            mMensagemErroTextView.setVisibility(View.VISIBLE);
        }
    }

    private void carregarFavoritos() {
        try {
            setTitle(String.format(getBaseContext().getString(R.string.active_title), FAVORITOS));
            mUltimaOrdenacao = FAVORITOS;

            mCarregandoProgressBar.setVisibility(View.VISIBLE);
            Cursor cursor = getContentResolver().query(FavoritoContract.FavoritoEntry.CONTENT_URI, FavoritoContract.FavoritoEntry.colunas(),
                    null, null, null);

            mFilmesFavoritos = new ArrayList<>();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Filme filme = new Filme();
                    filme.setFilmeId(cursor.getInt(cursor.getColumnIndex(FavoritoContract.FavoritoEntry.COLUMN_FILME_ID)));
                    filme.setPosterBytes(cursor.getBlob(cursor.getColumnIndex(FavoritoContract.FavoritoEntry.COLUMN_POSTER)));

                    mFilmesFavoritos.add(filme);
                } while (cursor.moveToNext());
                cursor.close();
            }


            mCarregandoProgressBar.setVisibility(View.INVISIBLE);
            mFilmeAdapter.setFilmes(mFilmesFavoritos);
        } catch (Exception e) {
            mCarregandoProgressBar.setVisibility(View.INVISIBLE);
            mMensagemErroTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.acao_populares == item.getItemId()) {
            carregarPopulares();
            return true;
        }

        if (R.id.acao_recomendados == item.getItemId()) {
            carregarRecomendados();
            return true;
        }

        if (R.id.acao_favoritos == item.getItemId()) {
            carregarFavoritos();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Filme filme) {
        Intent intent = new Intent(this, DetalheFilmeActivity.class);
        intent.putExtra("FILME", filme);
        intent.putExtra(ULTIMA_ORDENACAO, mUltimaOrdenacao);
        startActivityForResult(intent, REQUEST_CODE_FILME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_FILME && resultCode == RESULT_OK) {
            mUltimaOrdenacao = data.getStringExtra(ULTIMA_ORDENACAO);

            if(mUltimaOrdenacao.equals(FAVORITOS))
                carregarFavoritos();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void processFinish(Object output, String origem) {
        mCarregandoProgressBar.setVisibility(View.INVISIBLE);

        if (output != null) {
            TheMovieConsulta resultado = (TheMovieConsulta) output;

            try {
                if (resultado.ordenadoPorPopular()) {
                    mFilmesPopulares = TheMovieProcessor.processarFilmes(new JSONObject(resultado.getJson()));
                    mFilmeAdapter.setFilmes(mFilmesPopulares);
                }

                if (resultado.ordenadoPorRecomendados()) {
                    mFilmesRecomendados = TheMovieProcessor.processarFilmes(new JSONObject(resultado.getJson()));
                    mFilmeAdapter.setFilmes(mFilmesRecomendados);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            mMensagemErroTextView.setVisibility(View.VISIBLE);
        }
    }
}
