package firat.akyel.hastanebul.Modeller;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by root on 5/7/17.
 */

public class KontrolModel implements Serializable {

        @SerializedName("durum")
        private int durum;

        @SerializedName("mesaj")
        private String mesaj;

    public int getDurum() {
        return durum;
    }

    public void setDurum(int durum) {
        this.durum = durum;
    }

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }
}

