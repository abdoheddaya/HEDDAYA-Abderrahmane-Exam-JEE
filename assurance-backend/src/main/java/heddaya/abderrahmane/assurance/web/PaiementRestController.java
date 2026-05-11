package heddaya.abderrahmane.assurance.web;

import heddaya.abderrahmane.assurance.dtos.NouveauPaiementDTO;
import heddaya.abderrahmane.assurance.dtos.PaiementDTO;
import heddaya.abderrahmane.assurance.services.AssuranceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
public class PaiementRestController {

    private final AssuranceService assuranceService;

    @PostMapping("/contrat/{contratId}")
    public PaiementDTO ajouterPaiement(@PathVariable Long contratId,
                                       @Valid @RequestBody NouveauPaiementDTO dto) {
        return assuranceService.addPaiement(contratId, dto);
    }

    @GetMapping("/contrat/{contratId}")
    public List<PaiementDTO> getPaiementsContrat(@PathVariable Long contratId) {
        return assuranceService.showPaiementsContrat(contratId);
    }

    @GetMapping("/contrat/{contratId}/total")
    public Map<String, BigDecimal> totalPaye(@PathVariable Long contratId) {
        return Map.of("totalPaye", assuranceService.calculateTotalPaye(contratId));
    }

    @GetMapping("/contrat/{contratId}/reste")
    public Map<String, BigDecimal> resteAPayer(@PathVariable Long contratId) {
        return Map.of("resteAPayer", assuranceService.calculateResteAPayer(contratId));
    }
}