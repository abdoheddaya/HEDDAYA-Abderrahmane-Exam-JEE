package heddaya.abderrahmane.assurance.web;

import heddaya.abderrahmane.assurance.dtos.ClientDTO;
import heddaya.abderrahmane.assurance.services.AssuranceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientRestController {

    private final AssuranceService assuranceService;

    @PostMapping
    public ClientDTO addClient(@Valid @RequestBody ClientDTO dto) {
        return assuranceService.addClient(dto);
    }

    @PutMapping("/{id}")
    public ClientDTO updateClient(@PathVariable Long id, @Valid @RequestBody ClientDTO dto) {
        return assuranceService.updateClient(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable Long id) {
        assuranceService.deleteClient(id);
    }

    @GetMapping("/search")
    public List<ClientDTO> searchByNom(@RequestParam String nom) {
        return assuranceService.searchClientByNom(nom);
    }

    @GetMapping
    public List<ClientDTO> getAllClients() {
        return assuranceService.showallClients();
    }

    @GetMapping("/{id}")
    public ClientDTO getClientById(@PathVariable Long id) {
        return assuranceService.showClientById(id);
    }
}