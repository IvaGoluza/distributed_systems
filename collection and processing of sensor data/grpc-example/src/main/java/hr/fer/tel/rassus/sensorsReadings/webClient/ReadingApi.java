package hr.fer.tel.rassus.sensorsReadings.webClient;

import hr.fer.tel.rassus.sensorsReadings.webClient.model.Reading;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface ReadingApi {

    @POST("/api/readings/{id}")
    Call<Void> saveReading(@Path("id") Long id, @Body Reading reading);

    @GET("/api/readings/all/{id}")
    Call<List<Reading>> getReadings(@Path("id") Long id);

    @GET("/api/readings/{id}")
    Call<Reading> getReading(@Path("id") Long id);

}
