package br.com.bitcaseiro.filmesfamososparte2.dados;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import br.com.bitcaseiro.filmesfamososparte2.R;

import static br.com.bitcaseiro.filmesfamososparte2.dados.FavoritoContract.FavoritoEntry.TABLE_NAME;

public class FavoritoContentProvider extends ContentProvider {

    private static final int FAVORITOS = 100;
    private static final int FAVORITO_COM_ID = 101;

    private FavoritoDbHelper mFavoritoDbHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FavoritoContract.AUTHORITY, FavoritoContract.PATH_FAVORITOS, FAVORITOS);
        uriMatcher.addURI(FavoritoContract.AUTHORITY, FavoritoContract.PATH_FAVORITOS + "/#", FAVORITO_COM_ID);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        mFavoritoDbHelper = new FavoritoDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mFavoritoDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case FAVORITOS:
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException(String.format(getContext().getString(R.string.uri_desconhecida), uri));
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException(getContext().getString(R.string.nao_suportado));
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mFavoritoDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FAVORITOS:
                long id = db.insert(TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(FavoritoContract.FavoritoEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException(String.format(getContext().getString(R.string.uri_falha), uri));
                }
                break;

            default:
                throw new UnsupportedOperationException(String.format(getContext().getString(R.string.uri_desconhecida), uri));
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {

        final SQLiteDatabase sqlLiteDatabase = mFavoritoDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int favoritoDeletados;

        switch (match) {
            case FAVORITO_COM_ID:
                String id = uri.getPathSegments().get(1);
                favoritoDeletados = sqlLiteDatabase.delete(TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException(String.format(getContext().getString(R.string.uri_desconhecida), uri));
        }

        if (favoritoDeletados != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return favoritoDeletados;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException(getContext().getString(R.string.nao_suportado));
    }
}
