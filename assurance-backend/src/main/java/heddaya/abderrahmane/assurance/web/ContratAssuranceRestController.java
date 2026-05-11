package heddaya.abderrahmane.assurance.web;

import heddaya.abderrahmane.assurance.dtos.*;
import heddaya.abderrahmane.assurance.services.AssuranceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contrats")
@RequiredArgsConstructor
public class ContratAssuranceRestController {

    private final AssuranceService assuranceService;

    @PostMapping("/automobile")
    public ContratAutomobileDTO creerContratAutomobile(@Valid @RequestBody NouveauContratAutomobileDTO dto) {
        return assuranceService.createContratAutomobile(dto);
    }

    @PostMapping("/habitation")
    public ContratHabitationDTO creerContratHabitation(@Valid @RequestBody NouveauContratHabitationDTO dto) {
        return assuranceService.createContratHabitation(dto);
    }

    @PostMapping("/sante")
    public ContratSanteDTO creerContratSante(@Valid @RequestBody NouveauContratSanteDTO dto) {
        return assuranceService.createContratSante(dto);
    }

    @GetMapping
    public List<ContratAssuranceDTO> getAllContrats() {
        return assuranceService.showallContrats();
    }

    @GetMapping("/{id}")
    public ContratAssuranceDTO getContratById(@PathVariable Long id) {
        return assuranceService.showContratById(id);
    }

    @GetMapping("/client/{clientId}")
    public List<ContratAssuranceDTO> getContratsClient(@PathVariable Long clientId) {
        return assuranceService.showContratsClient(clientId);
    }

    @PutMapping("/{id}/valider")
    public ContratAssuranceDTO validerContrat(@PathVariable Long id) {
        return assuranceService.validateContrat(id);
    }

    @PutMapping("/{id}/resilier")
    public ContratAssuranceDTO resilierContrat(@PathVariable Long id) {
        return assuranceService.terminateContrat(id);
    }
}