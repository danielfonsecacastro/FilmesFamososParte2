package br.com.bitcaseiro.filmesfamososparte2.dados;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoritoDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favoritosDb.db";
    private static final int VERSION = 3;

    FavoritoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
         String createTable = "CREATE TABLE "  + FavoritoContract.FavoritoEntry.TABLE_NAME + " (" +
                FavoritoContract.FavoritoEntry._ID                + " INTEGER PRIMARY KEY, " +
                FavoritoContract.FavoritoEntry.COLUMN_FILME_ID + " TEXT NOT NULL, " +
                FavoritoContract.FavoritoEntry.COLUMN_TITULO + " TEXT NOT NULL, " +
                FavoritoContract.FavoritoEntry.COLUMN_TITULO_ORIGINAL + " TEXT NOT NULL, " +
                FavoritoContract.FavoritoEntry.COLUMN_DATA_LANCAMENTO + " TEXT NOT NULL, " +
                FavoritoContract.FavoritoEntry.COLUMN_MEDIA_VOTOS + " REAL NOT NULL, " +
                FavoritoContract.FavoritoEntry.COLUMN_RESUMO + " TEXT NOT NULL, " +
                FavoritoContract.FavoritoEntry.COLUMN_POSTER + " BLOB NOT NULL, " +
                FavoritoContract.FavoritoEntry.COLUMN_TEMPO   + " INTEGER NOT NULL);";

        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoritoContract.FavoritoEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
