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
@Table(name = "READING")
public class Reading {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private float temperature;

    private float pressure;

    private float humidity;

    private float co;

    private float no2;

    private float so2;

    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

}

