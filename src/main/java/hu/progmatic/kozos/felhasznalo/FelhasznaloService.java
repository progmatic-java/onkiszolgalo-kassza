package hu.progmatic.kozos.felhasznalo;

import lombok.extern.java.Log;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Log
@Transactional
public class FelhasznaloService implements InitializingBean {

    private final FelhasznaloRepository felhasznaloRepository;
    private final PasswordEncoder encoder;

    public FelhasznaloService(FelhasznaloRepository felhasznaloRepository, PasswordEncoder encoder) {
        this.felhasznaloRepository = felhasznaloRepository;
        this.encoder = encoder;
    }

    //@RolesAllowed(UserType.Roles.USER_READ_ROLE)
    public List<Felhasznalo> findAll() {
        return felhasznaloRepository.findAll();
    }

    public ModositFelhasznalo getModositFelhasznaloById(Long id){
        Felhasznalo felhasznalo = felhasznaloRepository.getById(id);
        return ModositFelhasznalo.builder()
                .felhasznaloId(felhasznalo.getId())
                .nev(felhasznalo.getNev())
                .role(felhasznalo.getRole())
                .hitelesitoKod(felhasznalo.getHitelesitoKod())
                .build();
    }


    @RolesAllowed(UserType.Roles.USER_MODIFYING)
    public void add(UjFelhasznaloCommand command) {
        if (felhasznaloRepository.findByNev(command.getNev()).isPresent()) {
            throw new FelhasznaloLetrehozasException("Ilyen névvel már létezik felhasználó!");
        }
        Felhasznalo felhasznalo = Felhasznalo.builder()
                .nev(command.getNev())
                .jelszo(encoder.encode(command.getJelszo()))
                .role(command.getRole())
                .hitelesitoKod(command.getHitelesitoKod())
                .build();
        felhasznaloRepository.save(felhasznalo);
    }

    @RolesAllowed(UserType.Roles.USER_MODIFYING)
    public void delete(Long id) {
        felhasznaloRepository.deleteById(id);
    }

    @RolesAllowed(UserType.Roles.USER_MODIFYING)
    public Optional<Felhasznalo> findByName(String nev) {
        return felhasznaloRepository.findByNev(nev);
    }

    @Override
    public void afterPropertiesSet() {
        if (findAll().isEmpty()) {
            add(new UjFelhasznaloCommand("admin", "adminpass", UserType.MANAGER, "8594001021758"));
            add(new UjFelhasznaloCommand("assistant", "assistantpass", UserType.ASSISTANT, "5051007149822"));
            add(new UjFelhasznaloCommand("customer", "customer", UserType.CUSTOMER, ""));
        }
    }

    public boolean hasRole(String role) {
        MyUserDetails userPrincipal = getMyUserDetails();
        return userPrincipal.getRole().hasRole(role);
    }

    public Long getFelhasznaloId() {
        MyUserDetails userPrincipal = getMyUserDetails();
        return userPrincipal.getFelhasznaloId();
    }

    private MyUserDetails getMyUserDetails() {
        return (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public void modosit(ModositFelhasznalo modositFelhasznalo, Long id) {
        Felhasznalo felhasznalo = felhasznaloRepository.getById(id);
        felhasznalo.setRole(modositFelhasznalo.getRole());
        felhasznalo.setHitelesitoKod(modositFelhasznalo.getHitelesitoKod());
        felhasznaloRepository.save(felhasznalo);
    }

    public String getNev(){
        MyUserDetails felhasznalo = getMyUserDetails();
        return felhasznalo.getUsername();
    }
}
