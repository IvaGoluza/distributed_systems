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

    private double temperature;

    private double pressure;

    private double humidity;

    private double co;

    private double no2;

    private double so2;

    private Long sensorId;
}
