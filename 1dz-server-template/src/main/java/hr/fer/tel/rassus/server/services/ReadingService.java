package hr.fer.tel.rassus.server.services;

import hr.fer.tel.rassus.server.beans.DTO.ReadingDTO;
import hr.fer.tel.rassus.server.beans.DTO.ReadingResponseDTO;

import java.util.List;

public interface ReadingService {

    ReadingResponseDTO getReading(Long id);

    ReadingResponseDTO saveReading(Long id, ReadingDTO readingDTO);

    List<ReadingResponseDTO> getAllReadings(Long id);
}
