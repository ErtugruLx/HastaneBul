package firat.akyel.hastanebul.Clients;

import java.util.List;

import firat.akyel.hastanebul.Modeller.HastanelerModel;
import firat.akyel.hastanebul.Modeller.KontrolModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by root on 5/7/17.
 */

public interface KontrolWSC {
    @GET("cevapp/hastane/kontrol.php")
    Call<List<KontrolModel>> kontrolCall(@Header("kontrol") int uniId);
}
