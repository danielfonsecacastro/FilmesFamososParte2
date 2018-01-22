package br.com.bitcaseiro.filmesfamososparte2.utilidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TheMovieProcessor {

    public static ArrayList<Filme> processarFilmes(JSONObject jsonObject) {
        ArrayList<Filme> resultado = new ArrayList<>();

        try {
            JSONArray resultados = jsonObject.getJSONArray("results");

            for (int i = 0; i < resultados.length(); i++) {
                Filme filme = new Filme();
                JSONObject json = resultados.getJSONObject(i);

                filme.setFilmeId(json.optInt("id"));
                filme.setPoster(json.optString("poster_path"));

                resultado.add(filme);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultado;
    }

    public static Filme processarFilme(JSONObject jsonObject) {
        Filme resultado = new Filme();

        resultado.setFilmeId(jsonObject.optInt("id"));
        resultado.setTitulo(jsonObject.optString("title"));
        resultado.setTituloOriginal(jsonObject.optString("original_title"));
        resultado.setTitulo(jsonObject.optString("original_title"));
        resultado.setDataLancamento(jsonObject.optString("release_date"));
        resultado.setMediaVotos(jsonObject.optDouble("vote_average"));
        resultado.setResumo(jsonObject.optString("overview"));
        resultado.setPoster(jsonObject.optString("poster_path"));
        resultado.setTempo(jsonObject.optInt("runtime"));

        return resultado;
    }

    public static ArrayList<Trailer> processarTrailers(JSONObject jsonObject) {
        ArrayList<Trailer> resultados = new ArrayList<>();

        try {
            JSONArray resultadoJson = jsonObject.getJSONArray("results");
            for (int i = 0; i < resultadoJson.length(); i++) {
                Trailer trailer = new Trailer();
                JSONObject json = resultadoJson.getJSONObject(i);

                trailer.setId(json.optString("id"));
                trailer.setKey(json.optString("key"));
                trailer.setType(json.optString("type"));
                trailer.setNome(json.optString("name"));
                trailer.setSite(json.optString("site"));

                resultados.add(trailer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultados;
    }

    public static ArrayList<Resenha> processarResenhas(JSONObject jsonObject) {
        ArrayList<Resenha> resultados = new ArrayList<>();

        try {
            JSONArray resultadoJson = jsonObject.getJSONArray("results");
            for (int i = 0; i < resultadoJson.length(); i++) {
                Resenha resenha = new Resenha();
                JSONObject json = resultadoJson.getJSONObject(i);

                resenha.setId(json.optString("id"));
                resenha.setAutor(json.optString("author"));
                resenha.setTexto(json.optString("content"));
                resenha.setUrl(json.optString("url"));

                resultados.add(resenha);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultados;
    }
}
