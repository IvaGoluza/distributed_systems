package hr.fer.tel.rassus.sensorsReadings.webClient;

import hr.fer.tel.rassus.sensorsReadings.webClient.model.Sensor;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface SensorApi {

    @POST("/api/sensors")
    Call<Void> sensorRegister(@Body Sensor sensor);

    @GET("/api/sensors/{id}")
    Call<Sensor> getSensor(@Path("id") Long id);

    @GET("/api/sensors")
    Call<List<Sensor>> getSensors();

    @GET("/api/sensors/nn/{id}")
    Call<Sensor> getNearestNeighbour(@Path("id") Long id);

}
