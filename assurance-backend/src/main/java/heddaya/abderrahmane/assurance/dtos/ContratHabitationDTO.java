package heddaya.abderrahmane.assurance.dtos;

import heddaya.abderrahmane.assurance.enums.TypeLogement;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ContratHabitationDTO extends ContratAssuranceDTO {
    private TypeLogement typeLogement;
    private String adresseLogement;
    private Double superficie;
}