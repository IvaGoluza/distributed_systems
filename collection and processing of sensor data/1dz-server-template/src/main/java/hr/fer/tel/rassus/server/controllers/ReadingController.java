package hr.fer.tel.rassus.server.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import hr.fer.tel.rassus.server.beans.DTO.ReadingDTO;
import hr.fer.tel.rassus.server.beans.DTO.ReadingResponseDTO;
import hr.fer.tel.rassus.server.services.ReadingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readings")
public class ReadingController {

    private final ReadingService readingService;

    @GetMapping("/{id}")
    public ResponseEntity<ReadingResponseDTO> getReading(@PathVariable("id") Long id) {
        return ResponseEntity.ok(readingService.getReading(id));
    }

  // 4.3  Spremanje očitanja pojedinog senzora
    @PostMapping()
    public ResponseEntity<String> saveReading(ReadingDTO readingDTO) {
        ReadingResponseDTO reading = readingService.saveReading(readingDTO);
        if(reading != null) return ResponseEntity.created(linkTo(methodOn(this.getClass()).getReading(reading.getId())).toUri()).build();
        else return ResponseEntity.noContent().build();
    }

  // 4.5  Popis očitanja pojedinog senzora
    @GetMapping("/all/{id}")
    public ResponseEntity<List<ReadingResponseDTO>> getReadings(@PathVariable("id") Long id) {
        List<ReadingResponseDTO> readings = readingService.getAllReadings(id);
        if(readings != null) return ResponseEntity.ok(readings);
        else return ResponseEntity.noContent().build();
    }
}