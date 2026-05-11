package heddaya.abderrahmane.assurance.dtos;

import heddaya.abderrahmane.assurance.enums.StatutContrat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ContratAssuranceDTO {
    private Long id;
    private String type;
    private LocalDate dateSouscription;
    private StatutContrat statut;
    private LocalDate dateValidation;
    private BigDecimal montantCotisation;
    private Integer dureeContrat;
    private Double tauxCouverture;
    private Long clientId;
}