package hr.fer.tel.rassus.server.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import hr.fer.tel.rassus.server.beans.DTO.SensorDTO;
import hr.fer.tel.rassus.server.beans.DTO.SensorResponseDTO;
import hr.fer.tel.rassus.server.services.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sensors")
public class SensorController {


    private final SensorService sensorService;

    @GetMapping("/{id}")
    public ResponseEntity<SensorResponseDTO> getSensor(@PathVariable("id") Long id) {
        return ResponseEntity.ok(sensorService.getSensor(id));
    }

    //  TODO 4.1  Registracija
    @PostMapping()
    public ResponseEntity<String> sensorRegister(@RequestBody SensorDTO sensorDTO) {
        SensorResponseDTO sensor = sensorService.sensorRegister(sensorDTO);
        return ResponseEntity.created(linkTo(methodOn(this.getClass()).getSensor(sensor.getId())).toUri()).build();
    }

    //  TODO 4.4  Popis senzora
    @GetMapping()
    public ResponseEntity<List<SensorResponseDTO>> getSensors() {
        return ResponseEntity.ok(sensorService.getSensors());
    }

    //  TODO 4.2  Najbliži susjed
    @GetMapping("/nn/{id}")
    public ResponseEntity<SensorResponseDTO> getNearestNeighbour(@PathVariable("id") Long id) {
        return ResponseEntity.ok(sensorService.getNearestNeighbour(id));
    }
}
