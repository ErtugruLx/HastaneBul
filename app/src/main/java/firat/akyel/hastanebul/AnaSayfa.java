package firat.akyel.hastanebul;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import firat.akyel.hastanebul.Clients.KontrolWSC;
import firat.akyel.hastanebul.Modeller.KontrolModel;
import firat.akyel.hastanebul.Utils.OnRequestPermissionsCallBack;
import firat.akyel.hastanebul.Utils.PermissionCompat;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AnaSayfa extends AppCompatActivity {


    private ProgressDialog progressDialog;
    private ArrayList<KontrolModel> kontrolModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_sayfa);

        //Görsel Nesne Tanımları
        progressDialog = new ProgressDialog(AnaSayfa.this);

        postRequest();

        //En yakın hastaneye geçiş.
        findViewById(R.id.crdEnYakinHastane).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AnaSayfa.this, EnYakinHastane.class));
            }
        });

        //Tüm hastaneler geçiş
        findViewById(R.id.crdTumHastaneler).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AnaSayfa.this, TumHastaneler.class));
            }
        });

        izinKontrol();



    }

    public void postRequest() {
        progressDialog.setIcon(getResources().getDrawable(R.drawable.hastane));
        progressDialog.setTitle("Ayarlanıyor");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.SERVIS_KOKU))
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient.build()).build();

        KontrolWSC client = retrofit.create(KontrolWSC.class);
        Call<List<KontrolModel>> call = client.kontrolCall(1);
        Log.w("istek", call.request().toString());

        call.enqueue(new Callback<List<KontrolModel>>() {
            @Override
            public void onResponse(Call<List<KontrolModel>> call, Response<List<KontrolModel>> response) {
                System.out.println("Gelen veri adeti : " + response.body().size());

                //veri tekrarından kaçınmak için öncelikle listeyi temizliyoruz.
                kontrolModels.clear();

                //veri alakalı listeye ekleniyor.(model üzerinden)
                for (int x = 0; x < response.body().size(); x = x + 1) {
                    KontrolModel kontrol = new KontrolModel();
                    //parse
                    kontrol.setDurum(response.body().get(x).getDurum());
                    kontrol.setMesaj(response.body().get(x).getMesaj());
                    kontrolModels.add(kontrol);

                    System.out.println(response.body().get(x).getMesaj());
                }
                progressDialog.dismiss();
                if(kontrolModels.get(0).getDurum()==1){
                    new AlertDialog.Builder(AnaSayfa.this)
                            .setTitle("Kontrol")
                            .setCancelable(false)
                            .setMessage(kontrolModels.get(0).getMesaj())
                            .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                }
                            })
                            .show();
                }
            }
            @Override
            public void onFailure(Call<List<KontrolModel>> call, Throwable t) {
                System.out.println("Bağlantı Yok !");
            }
        });
    }

    public void izinKontrol(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            new PermissionCompat.Builder(AnaSayfa.this)
                    .addPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
                    .addPermissionRationale("Lokasyon izni gerekli.")
                    .addRequestPermissionsCallBack(new OnRequestPermissionsCallBack() {
                        @Override
                        public void onGrant() {
                            Toast.makeText(AnaSayfa.this, "Comolokko", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onDenied(String permission) {
                            izinKontrol();
                        }

                    }).build().request();
        }
    }
}
