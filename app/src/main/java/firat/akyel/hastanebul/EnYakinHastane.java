package firat.akyel.hastanebul;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import firat.akyel.hastanebul.Clients.HastanelerWSC;
import firat.akyel.hastanebul.Modeller.HastanelerModel;
import firat.akyel.hastanebul.Utils.OkuText;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EnYakinHastane extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker markerKonumum, markerHastane;
    int konumSayac = 0;
    int sayac = 0, sayac2 = 0;
    private MediaPlayer mediaPlayer;
    private ProgressDialog progressDialog;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private ArrayList<HastanelerModel> hastanelerModels = new ArrayList<>();
    private ArrayList<String> hastaneIsimleriList = new ArrayList<>();
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_en_yakin_hastane);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Görsel Nesne Tanımları
        progressDialog = new ProgressDialog(EnYakinHastane.this);

        postRequest();

        //Lokasyon bilgilerini dinleyen listener.
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Her konum bilgisi geldiğinde çalışır.
                //Giriş parametresi lokasyondur.

                double hastaneLat = 0, hastaneLong = 0;
                double farkLat, farkLong, fark;
                if (konumSayac == 0) {
                    id = enYakinHastaneBul(hastanelerModels, location.getLatitude(), location.getLongitude());
                    System.out.println(id);
                    hastaneKonumlandir(hastanelerModels.get(id).getHastaneLat(), hastanelerModels.get(id).getHastaneLong(),
                            hastanelerModels.get(id).getHastaneAdi(), hastanelerModels.get(id).getHastaneAciklama());

                    hastaneLat = Double.parseDouble(hastanelerModels.get(id).getHastaneLat());
                    hastaneLong = Double.parseDouble(hastanelerModels.get(id).getHastaneLong());
                }

                farkLat = hastaneLat-location.getLatitude();
                farkLong = hastaneLong-location.getLongitude();
                fark = Math.sqrt((farkLat*farkLat)+(farkLong*farkLong));

                if (fark<=0.0004){
                    //Textto sipic
                    if (sayac2 == 0){
                        new asyncSes().execute();
                    }
                    sayac2++;
                }



                markerGuncelle(location);
                System.out.println(location);
                konumSayac++;

                progressDialog.dismiss();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                if (sayac == 0){
                    new AlertDialog.Builder(EnYakinHastane.this)
                            .setTitle("GPS Kapalı")
                            .setMessage("Hizmeti kullanabilmek için GPS aktif olmalı.")
                            .setPositiveButton("GİT", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                            .setNegativeButton("ÇIK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(EnYakinHastane.this, AnaSayfa.class));
                                    finish();
                                }
                            })
                            .setCancelable(false)
                            .show();
                    sayac++;
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ActivityCompat.checkSelfPermission(EnYakinHastane.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(EnYakinHastane.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        progressDialog.setMessage("Gps");
        progressDialog.setTitle("Konumunuz bulunuyor. Süre değişkenlik gösterebilir.");
        progressDialog.setIcon(getResources().getDrawable(R.drawable.hastane));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public int enYakinHastaneBul(ArrayList<HastanelerModel> hastanelerList, Double konumumLat, Double konumumLong) {
        double guncelFark = 0.0d;
        int guncelEnKisaID = 0;
        for (int y = 0; y < hastanelerList.size(); y++) {
            double latFark = Double.parseDouble(hastanelerList.get(y).getHastaneLat()) - konumumLat;
            double longFark = Double.parseDouble(hastanelerList.get(y).getHastaneLong()) - konumumLong;
            double tamFark = Math.sqrt((latFark * latFark) + (longFark * longFark));

            if (y == 0) {
                guncelFark = tamFark;
                guncelEnKisaID = y;
            } else {
                if (tamFark < guncelFark) {
                    guncelEnKisaID = y;
                    guncelFark = tamFark;
                }
            }
        }
        //Bu fonksiyondan id bilgisi dönecek.
        return guncelEnKisaID;
    }

    public void hastaneKonumlandir(String hastane_lat, String hastane_long, String hastane_adi, String hastane_aciklama) {
        LatLng latLngHastane = new LatLng(Double.parseDouble(hastane_lat), Double.parseDouble(hastane_long));
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.hastane);
        markerHastane = mMap.addMarker(new MarkerOptions().position(latLngHastane).title(hastane_adi).snippet(hastane_aciklama).icon(icon));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngHastane, 15));
    }

    public void markerGuncelle(Location location) {
        if (markerKonumum != null) {
            markerKonumum.remove();
        }
        LatLng latLngKonumum = new LatLng(location.getLatitude(), location.getLongitude());
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.human);
        markerKonumum = mMap.addMarker(new MarkerOptions().position(latLngKonumum).title("Konumum").icon(icon));
    }

    public void postRequest() {
        progressDialog.setMessage("Yükleniyor");
        progressDialog.setIcon(getResources().getDrawable(R.drawable.hastane));
        progressDialog.setTitle("Hastane Bul");
        progressDialog.show();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.SERVIS_KOKU))
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient.build()).build();

        HastanelerWSC client = retrofit.create(HastanelerWSC.class);
        Call<List<HastanelerModel>> call = client.hastanelerCall(1);
        Log.w("istek", call.request().toString());

        call.enqueue(new Callback<List<HastanelerModel>>() {
            @Override
            public void onResponse(Call<List<HastanelerModel>> call, Response<List<HastanelerModel>> response) {
                System.out.println("Gelen veri adeti : " + response.body().size());

                //veri tekrarından kaçınmak için öncelikle listeyi temizliyoruz.
                hastanelerModels.clear();

                //veri alakalı listeye ekleniyor.(model üzerinden)
                for (int x = 0; x < response.body().size(); x = x + 1) {
                    HastanelerModel hastane = new HastanelerModel();
                    //parse
                    hastane.setHastaneId(response.body().get(x).getHastaneId());
                    hastane.setHastaneAdi(response.body().get(x).getHastaneAdi());
                    hastane.setHastaneAciklama(response.body().get(x).getHastaneAciklama());
                    hastane.setHastaneLat(response.body().get(x).getHastaneLat());
                    hastane.setHastaneLong(response.body().get(x).getHastaneLong());

                    hastaneIsimleriList.add(response.body().get(x).getHastaneAdi());

                    hastanelerModels.add(hastane);

                    System.out.println(response.body().get(x).getHastaneAdi());


                }
                progressDialog.dismiss();
                if (ActivityCompat.checkSelfPermission(EnYakinHastane.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(EnYakinHastane.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
                progressDialog.setTitle("Gps");
                progressDialog.setMessage("Konumunuz bulunuyor. Süre değişkenlik gösterebilir.");
                progressDialog.setIcon(getResources().getDrawable(R.drawable.hastane));
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }

            @Override
            public void onFailure(Call<List<HastanelerModel>> call, Throwable t) {
                System.out.println("Bağlantı Yok !");
            }
        });
    }

    public class asyncSes extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {

            mediaPlayer = new MediaPlayer();

            try {
                mediaPlayer.setDataSource("https://translate.google.com/translate_tts?ie=UTF-8&q="+(hastanelerModels.get(id).getHastaneAdi()+"ne 20 Metre Kaldı").replaceAll(" ","%20")+"&tl=tr&client=tw-ob");
                mediaPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(!mediaPlayer.isPlaying()){
                mediaPlayer.start();
            }
            return null;
        }
    }
}
