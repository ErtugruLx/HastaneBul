package firat.akyel.hastanebul;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import firat.akyel.hastanebul.Clients.HastanelerWSC;
import firat.akyel.hastanebul.Modeller.HastanelerModel;
import firat.akyel.hastanebul.Modeller.MerkerModel;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TumHastaneler extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<HastanelerModel> hastanelerModels = new ArrayList<>();
    private ProgressDialog progressDialog;
    private ArrayList<String> hastaneIsimleriList = new ArrayList<>();
    private Marker marker = null;
    private LocationManager locationManager;
    private int sayac = 0;
    ArrayList<MerkerModel> markers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tum_hastaneler);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        progressDialog = new ProgressDialog(TumHastaneler.this);
        postRequest();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    public void postRequest() {
        progressDialog.setMessage("Yükleniyor");
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
                hastaneleriEkranaBas();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<HastanelerModel>> call, Throwable t) {
                progressDialog.dismiss();
                new AlertDialog.Builder(TumHastaneler.this)
                        .setTitle("Bağlantı hatası.")
                        .setMessage("Bağlantınızı kontrol ediniz.")
                        .setPositiveButton("Tekrar Dene", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                postRequest();
                            }
                        })
                        .setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        });
    }

    public void hastaneleriEkranaBas(){
        for (int a=1; a<hastaneIsimleriList.size(); a=a+1){
            LatLng gelencogulKonum = new LatLng(Double.valueOf(hastanelerModels.get(a).getHastaneLat()),
                    Double.valueOf(hastanelerModels.get(a).getHastaneLong()));
            MerkerModel model = new MerkerModel(
                    new LatLng(Double.valueOf(hastanelerModels.get(a).getHastaneLat()), Double.valueOf(hastanelerModels.get(a).getHastaneLong()))
                    ,hastaneIsimleriList.get(a)
                    ,"");
            this.markers.add(model);
            mMap.addMarker(model.makerYapilandir());

        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(
                hastanelerModels.get(1).getHastaneLat()), Double.valueOf(hastanelerModels.get(1).getHastaneLong())),17));
    }
}