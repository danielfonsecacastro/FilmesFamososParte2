package br.com.bitcaseiro.filmesfamososparte2.utilidades;

import android.os.AsyncTask;

import java.net.URL;


public class TheMovieDbService extends AsyncTask<URL, Void, TheMovieConsulta> {
    private AsyncTaskDelegate mDelegate = null;
    private String mOrigem;

    public TheMovieDbService(AsyncTaskDelegate responder, String origem) {
        mDelegate = responder;
        mOrigem = origem;
    }

    public TheMovieDbService(AsyncTaskDelegate responder) {
        mDelegate = responder;
        mOrigem = responder.toString();
    }

    @Override
    protected TheMovieConsulta doInBackground(URL... urls) {
        TheMovieConsulta resultado = new TheMovieConsulta();
        resultado.setJson(TheMovieDbRede.consultar(urls[0]));
        resultado.setUrl(urls[0]);

        return resultado;
    }

    @Override
    protected void onPostExecute(TheMovieConsulta resultado) {
        super.onPostExecute(resultado);
        if (mDelegate != null)
            mDelegate.processFinish(resultado, mOrigem);
    }
}