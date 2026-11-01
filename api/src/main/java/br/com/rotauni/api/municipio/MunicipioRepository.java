package br.com.rotauni.api.municipio;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface MunicipioRepository extends JpaRepository <Municipio, Long> {

    boolean existsByCodigoIbge (String codigoIbge);

    Optional<Municipio> findByCodigoIbge(String codigoIbge);
    
}
