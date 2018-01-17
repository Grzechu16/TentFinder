package thedoctors05.tentfinder;


import java.util.List;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Retrofit API which allows to generate get and post requests
 */

public interface RetrofitWebService {
    @GET("/GetTent")
    void getData(Callback<List<Tent>> pResponse);

    @FormUrlEncoded
    @POST("/GetTent")
      void postData(@Field("Title") String Title,
                  @Field("Longitude") String Longitude,
                  @Field("Latitude") String Latitude,
                  Callback<String> pResponse);
}
