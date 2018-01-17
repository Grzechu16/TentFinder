package thedoctors05.tentfinder;


import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
//import retrofit.http.POST;
import retrofit.http.Multipart;
import retrofit.http.POST;



/**
 * Created by Gregory on 2017-12-29.
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
//Call<Tent> postData(Tent tent);
// void postData(@Body Tent tent, Callback<Tent> pResponse);