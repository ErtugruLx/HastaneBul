package firat.akyel.hastanebul.Modeller;

import android.app.Activity;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import firat.akyel.hastanebul.R;

/**
 * Created by root on 5/7/17.
 */

public class MerkerModel extends Activity {

    private LatLng pozisyon;
    private String  baslik,
            aciklama;

    public MerkerModel(LatLng pozisyon, String baslik, String aciklama) {
        this.pozisyon = pozisyon;
        this.baslik = baslik;
        this.aciklama = aciklama;
    }

    public LatLng getPozisyon() {
        return pozisyon;
    }

    public void setPozisyon(LatLng pozisyon) {
        this.pozisyon = pozisyon;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public MarkerOptions makerYapilandir(){
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.hastane);
        return new MarkerOptions()
                .position(pozisyon)
                .title(baslik)
                .icon(icon)
                .snippet(aciklama)
                .anchor(0.5f,0.5f);
    }

}