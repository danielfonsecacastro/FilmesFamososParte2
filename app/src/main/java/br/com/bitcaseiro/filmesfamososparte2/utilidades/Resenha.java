package br.com.bitcaseiro.filmesfamososparte2.utilidades;


import android.os.Parcel;
import android.os.Parcelable;

public class Resenha implements Parcelable {
    public Resenha() {
    }

    private Resenha(Parcel parcel) {
        setId(parcel.readString());
        setAutor(parcel.readString());
        setTexto(parcel.readString());
        setUrl(parcel.readString());
    }

    private String mId;
    private String mAutor;
    private String mTexto;
    private String mUrl;

    public String getId() {
        return mId;
    }

    public String getAutor() {
        return mAutor;
    }

    public String getTexto() {
        if (mTexto.equals("") || mTexto == null)
            return mTexto;

        if (mTexto.length() > 100)
            return mTexto.trim().substring(0, 100) + " ...Ver mais";

        return mTexto + " ...Ver mais";
    }

    public String getUrl() {
        return mUrl;
    }

    public void setId(String id) {
        mId = id;
    }

    public void setAutor(String autor) {
        mAutor = autor;
    }

    public void setTexto(String texto) {
        mTexto = texto;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mAutor);
        parcel.writeString(mTexto);
        parcel.writeString(mUrl);
    }

    public static final Parcelable.Creator<Resenha> CREATOR = new Parcelable.Creator<Resenha>() {
        public Resenha createFromParcel(Parcel in) {
            return new Resenha(in);
        }

        public Resenha[] newArray(int size) {
            return new Resenha[size];
        }
    };
}
