package hr.fer.tel.rassus.sensorsReadings.webClient;

import hr.fer.tel.rassus.sensorsReadings.webClient.model.Reading;
import hr.fer.tel.rassus.sensorsReadings.webClient.model.Sensor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.logging.Logger;

public class HttpClient implements httpInterface {

    private static final Logger logger = Logger.getLogger(HttpClient.class.getName());
    private final SensorApi sensorApi;
    private final ReadingApi readingApi;

    public HttpClient() {
        String baseURL = "http://localhost:8090";   //http server
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        this.sensorApi = retrofit.create(SensorApi.class);
        this.readingApi = retrofit.create(ReadingApi.class);
    }


    @Override
    public Integer newSensor(Sensor sensor) {
        try {
            Response<Void> response = sensorApi.sensorRegister(sensor).execute();
            String location = response.headers().get("Location");
            return Integer.parseInt(location.substring(location.lastIndexOf('/')+1));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int newReading(Long sensorId, Reading reading) {
        try {
            Response<Void> response = readingApi.saveReading(sensorId, reading).execute();
            String location = response.headers().get("Location");
            logger.info("Reading has been saved in database.");
            return Integer.parseInt(location.substring(location.lastIndexOf('/')+1));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Sensor getNearestSensor(Long mainSensorId) {
        try {
            Response<Sensor> response = sensorApi.getNearestNeighbour(mainSensorId).execute();
            if(response.code() == 204) return null;
            else return response.body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
