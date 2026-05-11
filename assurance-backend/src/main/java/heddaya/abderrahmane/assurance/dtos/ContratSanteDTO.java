package heddaya.abderrahmane.assurance.dtos;

import heddaya.abderrahmane.assurance.enums.NiveauCouvertureSante;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ContratSanteDTO extends ContratAssuranceDTO {
    private NiveauCouvertureSante niveauCouverture;
    private Integer nombrePersonnesCouvertes;
}