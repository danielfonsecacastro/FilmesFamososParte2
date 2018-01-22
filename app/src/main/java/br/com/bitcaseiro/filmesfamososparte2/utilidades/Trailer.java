package br.com.bitcaseiro.filmesfamososparte2.utilidades;


import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable {
    public Trailer() {
    }

    private Trailer(Parcel parcel) {
        setId(parcel.readString());
        setKey(parcel.readString());
        setType(parcel.readString());
        setNome(parcel.readString());
        setSite(parcel.readString());
    }

    private String mId;
    private String mKey;
    private String mType;
    private String mNome;
    private String mSite;

    public String getId() {
        return mId;
    }

    public String getKey() {
        return mKey;
    }

    public String getType() {
        return mType;
    }

    public String getNome() {
        return mNome;
    }

    public String getSite() {
        return mSite;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public void setId(String id) {
        mId = id;
    }

    public void setType(String type) {
        mType = type;
    }

    public void setNome(String nome) {
        mNome = nome;
    }

    public void setSite(String site) {
        mSite = site;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mKey);
        parcel.writeString(mType);
        parcel.writeString(mNome);
        parcel.writeString(mSite);
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
