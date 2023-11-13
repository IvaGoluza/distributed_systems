package hr.fer.tel.rassus.server.services.impl;

import hr.fer.tel.rassus.server.beans.DTO.SensorDTO;
import hr.fer.tel.rassus.server.beans.DTO.SensorResponseDTO;
import hr.fer.tel.rassus.server.beans.Sensor;
import hr.fer.tel.rassus.server.repositories.SensorRepository;
import hr.fer.tel.rassus.server.services.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class SensorServiceImpl implements SensorService {

    private final SensorRepository sensorRepository;

    private final ModelMapper modelMapper;

    @Override
    public SensorResponseDTO getSensor(Long id) {
        Optional<Sensor> sensor = sensorRepository.findById(id);
        if(sensor.isPresent()) return modelMapper.map(sensor.get(), SensorResponseDTO.class);
        else throw new EntityNotFoundException("Sensor with id: " + id + " not found.");
    }

    @Override
    public SensorResponseDTO sensorRegister(SensorDTO sensorDTO) {
        Sensor sensor = modelMapper.map(sensorDTO, Sensor.class);
        sensor = sensorRepository.save(sensor);
        return modelMapper.map(sensor, SensorResponseDTO.class);
    }

    @Override
    public List<SensorResponseDTO> getSensors() {
        List<Sensor> sensors = sensorRepository.findAll();
        return sensors.stream().map(sensor -> modelMapper.map(sensor, SensorResponseDTO.class)).collect(Collectors.toList());
    }

    public double sensorsDistance(Sensor sensor1, Sensor sensor2) {

        int radius = 6371;
        double dlon = sensor2.getLongitude() - sensor1.getLongitude();
        double dlat = sensor2.getLatitude() - sensor1.getLatitude();
        double a = Math.pow(Math.sin(dlat/2), 2) + Math.cos(sensor1.getLatitude()) * Math.cos(sensor2.getLatitude()) * Math.pow(Math.sin(dlon/2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return radius * c;
    }

    @Override
    public SensorResponseDTO getNearestNeighbour(Long id) {

        List<Sensor> sensors = sensorRepository.findAll();
        Optional<Sensor> mainSensor = sensorRepository.findById(id);

        Sensor nn = null;
        if(mainSensor.isPresent()) {
            double minDistance = Double.MAX_VALUE;
            double distance;
            for (Sensor sensor : sensors) {
                distance = sensorsDistance(mainSensor.get(), sensor);
                if(distance < minDistance && !Objects.equals(sensor.getId(), mainSensor.get().getId())) {
                    minDistance = distance;
                    nn = sensor;
                }
            }
            if(nn != null) return modelMapper.map(nn, SensorResponseDTO.class);
        }
        return null;
    }
}
