package heddaya.abderrahmane.assurance.repositories;

import heddaya.abderrahmane.assurance.entities.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    List<Paiement> findByContratId(Long contratId);
}