package br.com.bitcaseiro.filmesfamososparte2.dados;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoritoContract {
    public static final String AUTHORITY = "br.com.bitcaseiro.filmesfamososparte2.dados.favorito";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FAVORITOS = "favoritos";

    public static final class FavoritoEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITOS).build();


        public static final String TABLE_NAME = "favoritos";

        public static final String COLUMN_FILME_ID = "filmeId";
        public static final String COLUMN_TITULO = "titulo";
        public static final String COLUMN_TITULO_ORIGINAL = "tituloOriginal";
        public static final String COLUMN_DATA_LANCAMENTO = "dataLancamento";
        public static final String COLUMN_MEDIA_VOTOS = "mediaVotos";
        public static final String COLUMN_RESUMO = "resumo";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_TEMPO = "tempo";

        public static String[] colunas() {
            return new String[]{
                    _ID,
                    COLUMN_FILME_ID,
                    COLUMN_TITULO,
                    COLUMN_TITULO_ORIGINAL,
                    COLUMN_DATA_LANCAMENTO,
                    COLUMN_MEDIA_VOTOS,
                    COLUMN_RESUMO,
                    COLUMN_POSTER,
                    COLUMN_TEMPO
            };
        }
    }
}
