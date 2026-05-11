package heddaya.abderrahmane.assurance.security;

import heddaya.abderrahmane.assurance.entities.Role;
import heddaya.abderrahmane.assurance.entities.Utilisateur;
import heddaya.abderrahmane.assurance.enums.RoleName;
import heddaya.abderrahmane.assurance.repositories.RoleRepository;
import heddaya.abderrahmane.assurance.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Role roleClient = roleRepository.findByName(RoleName.ROLE_CLIENT)
                .orElseGet(() -> roleRepository.save(Role.builder().name(RoleName.ROLE_CLIENT).build()));
        Role roleEmploye = roleRepository.findByName(RoleName.ROLE_EMPLOYE)
                .orElseGet(() -> roleRepository.save(Role.builder().name(RoleName.ROLE_EMPLOYE).build()));
        Role roleAdmin = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseGet(() -> roleRepository.save(Role.builder().name(RoleName.ROLE_ADMIN).build()));

        if (!utilisateurRepository.existsByUsername("abdo")) {
            Utilisateur admin = Utilisateur.builder()
                    .username("abdo")
                    .email("abdo@assurance.local")
                    .password(passwordEncoder.encode("heddaya2004"))
                    .roles(Set.of(roleAdmin, roleEmploye, roleClient))
                    .build();
            utilisateurRepository.save(admin);
        }
    }
}