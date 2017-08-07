package firat.akyel.hastanebul.Clients;

import java.util.List;

import firat.akyel.hastanebul.Modeller.HastanelerModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by Onur on 30.04.2017.
 */

public interface HastanelerWSC {
    @GET("cevapp/hastane/hastaneCek.php")
    Call<List<HastanelerModel>> hastanelerCall(@Header("hastane") int uniId);
}
