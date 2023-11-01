package hr.fer.tel.rassus.server.beans.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SensorDTO {

    private float latitude;

    private float longitude;

    private String ip;

    private long port;

}
