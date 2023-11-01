package hr.fer.tel.rassus.server.services;

import hr.fer.tel.rassus.server.beans.DTO.SensorDTO;
import hr.fer.tel.rassus.server.beans.DTO.SensorResponseDTO;

import java.util.List;

public interface SensorService {

    SensorResponseDTO getSensor(Long id);
    SensorResponseDTO sensorRegister(SensorDTO sensorDTO);
    List<SensorResponseDTO> getSensors();
    SensorResponseDTO getNearestNeighbour(Long id);

}
