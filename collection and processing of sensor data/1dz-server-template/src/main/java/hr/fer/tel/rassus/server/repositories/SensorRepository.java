package hr.fer.tel.rassus.server.repositories;

import hr.fer.tel.rassus.server.beans.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {


}
