package heddaya.abderrahmane.assurance.services;

import heddaya.abderrahmane.assurance.dtos.*;

import java.math.BigDecimal;
import java.util.List;

public interface AssuranceService {
    ClientDTO addClient(ClientDTO dto);
    ClientDTO updateClient(Long id, ClientDTO dto);
    void deleteClient(Long id);
    List<ClientDTO> searchClientByNom(String nom);
    List<ClientDTO> showallClients();
    ClientDTO showClientById(Long id);

    ContratAutomobileDTO createContratAutomobile(NouveauContratAutomobileDTO dto);
    ContratHabitationDTO createContratHabitation(NouveauContratHabitationDTO dto);
    ContratSanteDTO createContratSante(NouveauContratSanteDTO dto);

    List<ContratAssuranceDTO> showallContrats();
    ContratAssuranceDTO showContratById(Long id);
    List<ContratAssuranceDTO> showContratsClient(Long clientId);
    ContratAssuranceDTO validateContrat(Long contratId);
    ContratAssuranceDTO terminateContrat(Long contratId);

    PaiementDTO addPaiement(Long contratId, NouveauPaiementDTO dto);
    List<PaiementDTO> showPaiementsContrat(Long contratId);
    BigDecimal calculateTotalPaye(Long contratId);
    BigDecimal calculateResteAPayer(Long contratId);
    DashboardStatsDTO dashboardStats();
}