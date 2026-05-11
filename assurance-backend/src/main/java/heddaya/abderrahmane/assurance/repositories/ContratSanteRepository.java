package heddaya.abderrahmane.assurance.repositories;

import heddaya.abderrahmane.assurance.entities.ContratAssuranceSante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContratSanteRepository extends JpaRepository<ContratAssuranceSante, Long> {
}