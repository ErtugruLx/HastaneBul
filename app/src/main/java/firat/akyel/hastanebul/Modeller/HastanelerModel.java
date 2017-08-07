package firat.akyel.hastanebul.Modeller;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Onur on 30.04.2017.
 */

public class HastanelerModel implements Serializable {

    @SerializedName("hastane_id")
    private int hastaneId;

    @SerializedName("hastane_adi")
    private String hastaneAdi;

    @SerializedName("hastane_aciklama")
    private String hastaneAciklama;

    @SerializedName("hastane_lat")
    private String hastaneLat;

    @SerializedName("hastane_long")
    private String hastaneLong;

    public int getHastaneId() {
        return hastaneId;
    }

    public void setHastaneId(int hastaneId) {
        this.hastaneId = hastaneId;
    }

    public String getHastaneAdi() {
        return hastaneAdi;
    }

    public void setHastaneAdi(String hastaneAdi) {
        this.hastaneAdi = hastaneAdi;
    }

    public String getHastaneAciklama() {
        return hastaneAciklama;
    }

    public void setHastaneAciklama(String hastaneAciklama) {
        this.hastaneAciklama = hastaneAciklama;
    }

    public String getHastaneLat() {
        return hastaneLat;
    }

    public void setHastaneLat(String hastaneLat) {
        this.hastaneLat = hastaneLat;
    }

    public String getHastaneLong() {
        return hastaneLong;
    }

    public void setHastaneLong(String hastaneLong) {
        this.hastaneLong = hastaneLong;
    }
}
