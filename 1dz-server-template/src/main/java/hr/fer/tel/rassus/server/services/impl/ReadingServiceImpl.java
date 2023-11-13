package hr.fer.tel.rassus.server.services.impl;

import hr.fer.tel.rassus.server.beans.DTO.ReadingDTO;
import hr.fer.tel.rassus.server.beans.DTO.ReadingResponseDTO;
import hr.fer.tel.rassus.server.beans.Reading;
import hr.fer.tel.rassus.server.beans.Sensor;
import hr.fer.tel.rassus.server.repositories.ReadingRepository;
import hr.fer.tel.rassus.server.repositories.SensorRepository;
import hr.fer.tel.rassus.server.services.ReadingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReadingServiceImpl implements ReadingService {

    private final ReadingRepository readingRepository;

    private final SensorRepository sensorRepository;

    private final ModelMapper modelMapper;

    @Override
    public ReadingResponseDTO getReading(Long id) {
        Optional<Reading> reading = readingRepository.findById(id);
        if(reading.isPresent()) return modelMapper.map(reading.get(), ReadingResponseDTO.class);
        else throw new EntityNotFoundException("Reading with id: " + id + " not found.");
    }

    @Override
    public ReadingResponseDTO saveReading(Long id, ReadingDTO readingDTO) {
        Optional<Sensor> sensor = sensorRepository.findById(id);
        if(sensor.isPresent()) {
            Reading reading = modelMapper.map(readingDTO, Reading.class);
            reading.setSensor(sensor.get());
            reading = readingRepository.save(reading);
            return modelMapper.map(reading, ReadingResponseDTO.class);
        } else return null;
    }

    @Override
    public List<ReadingResponseDTO> getAllReadings(Long id) {
        Optional<Sensor> sensor = sensorRepository.findById(id);
        if(sensor.isPresent()) {
            List<Reading> readings = readingRepository.getAllBySensor(sensor.get());
            return readings.stream().map(reading -> modelMapper.map(reading, ReadingResponseDTO.class)).collect(Collectors.toList());
        } else return null;
    }
}
