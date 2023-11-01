package hr.fer.tel.rassus.server.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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

    private double temperature;

    private double pressure;

    private double humidity;

    private double co;

    private double no2;

    private double so2;

    @ManyToOne
    @JoinColumn(name = "sensorId")
    private Sensor sensor;

}

