package br.com.bitcaseiro.filmesfamososparte2.utilidades;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class Filme implements Parcelable {

    private static final String URL_IMG_NORMAL = "http://image.tmdb.org/t/p/w342";

    public Filme() {
    }

    private Filme(Parcel parcel) {
        setFilmeId(parcel.readInt());
        setTitulo(parcel.readString());
        setTituloOriginal(parcel.readString());
        setDataLancamento(parcel.readString());
        setMediaVotos(parcel.readDouble());
        setResumo(parcel.readString());
        setPoster(parcel.readString());
        setTempo(parcel.readInt());
    }

    private int mId;
    private int mFilmeId;
    private String mTitulo;
    private String mTituloOriginal;
    private String mDataLancamento;
    private double mMediaVotos;
    private String mResumo;
    private String mPoster;
    private int mTempo;
    private byte[] mPosterBytes;

    public Integer getId() {
        return mId;
    }

    public Integer getFilmeId() {
        return mFilmeId;
    }

    public Integer getTempo() {
        return mTempo;
    }

    public String getPoster() {
        return URL_IMG_NORMAL + mPoster;
    }

    public String getTitulo() {
        return mTitulo;
    }

    public String getTituloOriginal() {
        return mTituloOriginal;
    }

    public String getDataLancamento() {
        return mDataLancamento;
    }

    public Double getMediaVotos() {
        return mMediaVotos;
    }

    public String getResumo() {
        return mResumo;
    }

    public byte[] getPosterBytes() {
        return mPosterBytes;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setFilmeId(int filmeId) {
        mFilmeId = filmeId;
    }

    public void setTitulo(String titulo) {
        mTitulo = titulo;
    }

    public void setTituloOriginal(String tituloOriginal) {
        mTituloOriginal = tituloOriginal;
    }

    public void setDataLancamento(String dataLancamento) {
        mDataLancamento = dataLancamento;
    }

    public void setMediaVotos(double mediaVotos) {
        mMediaVotos = mediaVotos;
    }

    public void setResumo(String resumo) {
        mResumo = resumo;
    }

    public void setPoster(String poster) {
        mPoster = poster;
    }

    public void setTempo(int tempo) {
        mTempo = tempo;
    }

    public void setPosterBytes(byte[] imagem) {
        mPosterBytes = imagem;
    }

    public byte[] getImagemEmBytes(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mFilmeId);
        parcel.writeString(mTitulo);
        parcel.writeString(mTituloOriginal);
        parcel.writeString(mDataLancamento);
        parcel.writeDouble(mMediaVotos);
        parcel.writeString(mResumo);
        parcel.writeString(mPoster);
        parcel.writeInt(mTempo);
    }

    public static final Parcelable.Creator<Filme> CREATOR = new Parcelable.Creator<Filme>() {
        public Filme createFromParcel(Parcel in) {
            return new Filme(in);
        }

        public Filme[] newArray(int size) {
            return new Filme[size];
        }
    };
}
