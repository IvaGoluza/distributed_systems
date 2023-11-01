package hr.fer.tel.rassus.server.repositories;

import hr.fer.tel.rassus.server.beans.Reading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadingRepository extends JpaRepository<Reading, Long> {

}
