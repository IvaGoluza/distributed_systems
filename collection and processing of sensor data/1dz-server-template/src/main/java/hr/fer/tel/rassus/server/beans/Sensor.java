package hr.fer.tel.rassus.server.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "SENSOR")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private float latitude;

    private float longitude;

    private String ip;

    private Long port;

    @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL)
    private List<Reading> readings;

}
