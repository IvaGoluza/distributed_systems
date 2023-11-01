package hr.fer.tel.rassus.server.beans.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReadingResponseDTO {

    private Long id;

    private float temperature;

    private float pressure;

    private float humidity;

    private float co;

    private float no2;

    private float so2;

    private Long sensorId;
}
