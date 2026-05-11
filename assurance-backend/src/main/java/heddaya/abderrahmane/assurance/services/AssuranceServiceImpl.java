package heddaya.abderrahmane.assurance.services;

import heddaya.abderrahmane.assurance.dtos.*;
import heddaya.abderrahmane.assurance.entities.*;
import heddaya.abderrahmane.assurance.enums.StatutContrat;
import heddaya.abderrahmane.assurance.exceptions.BusinessException;
import heddaya.abderrahmane.assurance.exceptions.ResourceNotFoundException;
import heddaya.abderrahmane.assurance.mappers.AssuranceMapper;
import heddaya.abderrahmane.assurance.repositories.ClientRepository;
import heddaya.abderrahmane.assurance.repositories.ContratAssuranceRepository;
import heddaya.abderrahmane.assurance.repositories.PaiementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AssuranceServiceImpl implements AssuranceService {

    private final ClientRepository clientRepository;
    private final ContratAssuranceRepository contratRepository;
    private final PaiementRepository paiementRepository;

    @Override
    public ClientDTO addClient(ClientDTO dto) {
        Client client = AssuranceMapper.toClientEntity(dto);
        client.setId(null);
        return AssuranceMapper.toClientDTO(clientRepository.save(client));
    }

    @Override
    public ClientDTO updateClient(Long id, ClientDTO dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable: " + id));
        client.setNom(dto.getNom());
        client.setEmail(dto.getEmail());
        return AssuranceMapper.toClientDTO(clientRepository.save(client));
    }

    @Override
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client introuvable: " + id);
        }
        clientRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> searchClientByNom(String nom) {
        return clientRepository.findByNomContainingIgnoreCase(nom)
                .stream()
                .map(AssuranceMapper::toClientDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> showallClients() {
        return clientRepository.findAll().stream().map(AssuranceMapper::toClientDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDTO showClientById(Long id) {
        return AssuranceMapper.toClientDTO(
                clientRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Client introuvable: " + id))
        );
    }

    @Override
    public ContratAutomobileDTO createContratAutomobile(NouveauContratAutomobileDTO dto) {
        Client client = findClient(dto.getClientId());
        ContratAssuranceAutomobile contrat = new ContratAssuranceAutomobile();
        applyBaseContrat(contrat, dto.getDateSouscription(), dto.getMontantCotisation(), dto.getDureeContrat(), dto.getTauxCouverture(), client);
        contrat.setNumeroImmatriculation(dto.getNumeroImmatriculation());
        contrat.setMarqueVehicule(dto.getMarqueVehicule());
        contrat.setModeleVehicule(dto.getModeleVehicule());
        return AssuranceMapper.toContratAutoDTO((ContratAssuranceAutomobile) contratRepository.save(contrat));
    }

    @Override
    public ContratHabitationDTO createContratHabitation(NouveauContratHabitationDTO dto) {
        Client client = findClient(dto.getClientId());
        ContratAssuranceHabitation contrat = new ContratAssuranceHabitation();
        applyBaseContrat(contrat, dto.getDateSouscription(), dto.getMontantCotisation(), dto.getDureeContrat(), dto.getTauxCouverture(), client);
        contrat.setTypeLogement(dto.getTypeLogement());
        contrat.setAdresseLogement(dto.getAdresseLogement());
        contrat.setSuperficie(dto.getSuperficie());
        return AssuranceMapper.toContratHabitationDTO((ContratAssuranceHabitation) contratRepository.save(contrat));
    }

    @Override
    public ContratSanteDTO createContratSante(NouveauContratSanteDTO dto) {
        Client client = findClient(dto.getClientId());
        ContratAssuranceSante contrat = new ContratAssuranceSante();
        applyBaseContrat(contrat, dto.getDateSouscription(), dto.getMontantCotisation(), dto.getDureeContrat(), dto.getTauxCouverture(), client);
        contrat.setNiveauCouverture(dto.getNiveauCouverture());
        contrat.setNombrePersonnesCouvertes(dto.getNombrePersonnesCouvertes());
        return AssuranceMapper.toContratSanteDTO((ContratAssuranceSante) contratRepository.save(contrat));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContratAssuranceDTO> showallContrats() {
        return contratRepository.findAll().stream().map(AssuranceMapper::toContratDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ContratAssuranceDTO showContratById(Long id) {
        return AssuranceMapper.toContratDTO(findContrat(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContratAssuranceDTO> showContratsClient(Long clientId) {
        return contratRepository.findByClientId(clientId).stream().map(AssuranceMapper::toContratDTO).toList();
    }

    @Override
    public ContratAssuranceDTO validateContrat(Long contratId) {
        ContratAssurance contrat = findContrat(contratId);
        if (contrat.getStatut() == StatutContrat.RESILIE) {
            throw new BusinessException("Impossible de valider un contrat résilié");
        }
        contrat.setStatut(StatutContrat.VALIDE);
        contrat.setDateValidation(LocalDate.now());
        return AssuranceMapper.toContratDTO(contratRepository.save(contrat));
    }

    @Override
    public ContratAssuranceDTO terminateContrat(Long contratId) {
        ContratAssurance contrat = findContrat(contratId);
        contrat.setStatut(StatutContrat.RESILIE);
        return AssuranceMapper.toContratDTO(contratRepository.save(contrat));
    }

    @Override
    public PaiementDTO addPaiement(Long contratId, NouveauPaiementDTO dto) {
        ContratAssurance contrat = findContrat(contratId);
        if (contrat.getStatut() == StatutContrat.RESILIE) {
            throw new BusinessException("Impossible d'ajouter un paiement sur un contrat résilié");
        }
        Paiement paiement = Paiement.builder()
                .date(dto.getDate())
                .montant(dto.getMontant())
                .type(dto.getType())
                .contrat(contrat)
                .build();
        return AssuranceMapper.toPaiementDTO(paiementRepository.save(paiement));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaiementDTO> showPaiementsContrat(Long contratId) {
        findContrat(contratId);
        return paiementRepository.findByContratId(contratId)
                .stream()
                .map(AssuranceMapper::toPaiementDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalPaye(Long contratId) {
        findContrat(contratId);
        return paiementRepository.findByContratId(contratId)
                .stream()
                .map(Paiement::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateResteAPayer(Long contratId) {
        ContratAssurance contrat = findContrat(contratId);
        BigDecimal totalPaye = calculateTotalPaye(contratId);
        BigDecimal montantTotalContrat = contrat.getMontantCotisation().multiply(BigDecimal.valueOf(contrat.getDureeContrat()));
        BigDecimal reste = montantTotalContrat.subtract(totalPaye);
        return reste.max(BigDecimal.ZERO);
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsDTO dashboardStats() {
        DashboardStatsDTO dto = new DashboardStatsDTO();
        dto.setTotalClients(clientRepository.count());
        dto.setTotalContrats(contratRepository.count());
        dto.setTotalPaiements(paiementRepository.count());

        List<ContratAssurance> contrats = contratRepository.findAll();
        dto.setContratsParType(contrats.stream()
                .collect(Collectors.groupingBy(c -> c.getClass().getSimpleName(), Collectors.counting())));
        dto.setContratsParStatut(contrats.stream()
                .collect(Collectors.groupingBy(c -> c.getStatut().name(), Collectors.counting())));

        List<Paiement> paiements = paiementRepository.findAll();
        dto.setPaiementsParType(paiements.stream()
                .collect(Collectors.groupingBy(p -> p.getType().name(), Collectors.counting())));

        Map<String, Double> mensuel = paiements.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getDate().getYear() + "-" + String.format("%02d", p.getDate().getMonthValue()),
                        TreeMap::new,
                        Collectors.reducing(0.0, p -> p.getMontant().doubleValue(), Double::sum)
                ));
        dto.setStatistiquesMensuellesPaiements(mensuel);
        return dto;
    }

    private Client findClient(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable: " + id));
    }

    private ContratAssurance findContrat(Long id) {
        return contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat introuvable: " + id));
    }

    private void applyBaseContrat(ContratAssurance contrat,
                                  LocalDate dateSouscription,
                                  BigDecimal montantCotisation,
                                  Integer dureeContrat,
                                  Double tauxCouverture,
                                  Client client) {
        contrat.setDateSouscription(dateSouscription);
        contrat.setMontantCotisation(montantCotisation);
        contrat.setDureeContrat(dureeContrat);
        contrat.setTauxCouverture(tauxCouverture);
        contrat.setStatut(StatutContrat.EN_COURS);
        contrat.setClient(client);
    }
}