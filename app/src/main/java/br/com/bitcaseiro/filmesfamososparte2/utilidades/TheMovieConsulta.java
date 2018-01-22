package br.com.bitcaseiro.filmesfamososparte2.utilidades;

import java.net.URL;

public class TheMovieConsulta {
    private URL mUrl;
    private String mJson;


    public void setUrl(URL url) {
        this.mUrl = url;
    }

    public URL getUrl() {
        return mUrl ;
    }

    public String getJson() {
        return mJson;
    }

    public void setJson(String json) {
        mJson = json;
    }

    public Boolean ordenadoPorPopular(){
        return mUrl.toString().contains("popular");
    }

    public Boolean ordenadoPorRecomendados(){
        return mUrl.toString().contains("top_rated");
    }
}
