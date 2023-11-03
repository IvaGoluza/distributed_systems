package hr.fer.tel.rassus.sensorsReadings.webClient;

import hr.fer.tel.rassus.sensorsReadings.webClient.model.Reading;
import hr.fer.tel.rassus.sensorsReadings.webClient.model.Sensor;

import java.util.List;

public interface httpInterface {

    Integer newSensor(Sensor sensor);
    int newReading(Long sensorId, Reading reading);
    Sensor getNearestSensor(Long mainSensorId);
    //List<Reading> getSensorReadings(Long sensorId);

}
