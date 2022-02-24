package hu.progmatic.kozos.kassza;

import hu.progmatic.kozos.felhasznalo.Felhasznalo;
import hu.progmatic.kozos.felhasznalo.FelhasznaloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class KosarService {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private KosarRepositoryProba kosarRepository;

    @Autowired
    private TermekMennyisegRepository termekMennyisegRepository;

    @Autowired
    private TermekService termekService;
    @Autowired
    private FelhasznaloService felhasznaloService;


    public KosarViewDTO getKosarDtoById(Integer id) {
        return kosarToKosarViewDTO(kosarRepository.getById(id));
    }

    public KosarViewDTO kosarViewCreate() {
        List<Kosar> kosarak = kosarRepository.findAll();
        kosarRepository.deleteAll(kosarak);
        Kosar kosar = kosarRepository.save(Kosar.builder()
                .hitelesites(Hitelesites.NEM_KELL_HITELESITENI)
                .letrehozasDatuma(LocalDateTime.now())
                .build());
        return KosarViewDTO.builder()
                .kosarId(kosar.getId())
                .letrehozasDatuma(dateTimeFormatter.format(kosar.getLetrehozasDatuma()))
                .build();
    }


    public KosarViewDTO addTermekMennyisegCommand(Integer kosarId, TermekMennyisegHozzaadasCommand command) {
        Kosar kosar = kosarRepository.findById(kosarId).orElseThrow();
        mennyisegValidacio(command.getVonalkod(), command.getMennyiseg());
        Optional<TermekMennyiseg> termekMennyisegOptional = kosar.getTermekMennyisegek().stream()
                .filter(termekM -> termekM.getTermek().getVonalkod().equals(command.getVonalkod()))
                .findAny();
        if (termekMennyisegOptional.isEmpty()) {
            Termek termek = termekService.getByVonalkod(command.getVonalkod());
            if (termek.isHitelesitesSzukseges() && kosar.getHitelesites() != Hitelesites.HITELESITVE) {
                kosar.setHitelesites(Hitelesites.NINCS_HITELESITVE);
            }
            TermekMennyiseg ujTermekMennyiseg = TermekMennyiseg.builder()
                    .mennyiseg(command.getMennyiseg())
                    .termek(termek)
                    .kosar(kosar)
                    .hitelesitesSzukseges(termek.isHitelesitesSzukseges())
                    .build();
            kosar.getTermekMennyisegek().add(ujTermekMennyiseg);
            termekMennyisegRepository.save(ujTermekMennyiseg);
            kosar.setUtolsoHozzaadottTermekmennyiseg(null);
            //kosar.setUtolsoHozzaadottTermekmennyiseg(ujTermekMennyiseg);
        } else {
            termekMennyisegOptional.get().setMennyiseg(termekMennyisegOptional.get().getMennyiseg() + command.getMennyiseg());
            kosar.setUtolsoHozzaadottTermekmennyiseg(null);
            //kosar.setUtolsoHozzaadottTermekmennyiseg(termekMennyisegOptional.get());
        }
        return kosarToKosarViewDTO(kosar);
    }

    private void mennyisegValidacio(String vonalkod, Integer mennyiseg) {
        Termek termek = termekService.getByVonalkod(vonalkod);
        if (termek != null) {
            int termekMennyiseg = termek.getMennyiseg();
            if (termekMennyiseg < mennyiseg) {
                throw new NincsElegRaktarKeszletException(
                        String.format("Nincs elég termék a raktáron! Maximálisan %s termék adható hozzá!", termekMennyiseg)
                );
            }
            termek.setMennyiseg(termekMennyiseg - mennyiseg);
        } else {
            throw new NincsIlyenTermek("Boltban nem szereplő termék!");
        }
    }

    private KosarViewDTO kosarToKosarViewDTO(Kosar kosar) {
        List<TermekMennyisegDto> termekMennyisegDtoList = kosar.getTermekMennyisegek().stream()
                .sorted((o1, o2) -> o2.getId() - o1.getId())
                .map(termekMennyiseg -> TermekMennyisegDto.builder()
                        .termekMennyisegId(termekMennyiseg.getId())
                        .ar(termekMennyiseg.getTermek().getAr())
                        .nev(termekMennyiseg.getTermek().getMegnevezes())
                        .mennyiseg(termekMennyiseg.getMennyiseg())
                        .termekId(termekMennyiseg.getTermek().getId())
                        .hitelesitesSzukseges(termekMennyiseg.isHitelesitesSzukseges())
                        .vanKep(termekMennyiseg.getTermek().getKep() != null)
                        .build()).toList();
        return KosarViewDTO.builder()
                .kosarId(kosar.getId())
                .vegosszeg(Kosar.kosarVegosszeg(kosar))
                .utolsoHozzaadottTermekmennyisegDto(
                        termekMennyisegDtoList.size() == 0 ? null : termekMennyisegDtoList.get(0))
                .termekMennyisegDtoList(termekMennyisegDtoList)
                .hitelesites(kosar.getHitelesites())
                .letrehozasDatuma(dateTimeFormatter.format(kosar.getLetrehozasDatuma()))
                .build();
    }

    public void deleteKosarById(Integer id) {
        kosarRepository.delete(kosarRepository.getById(id));
    }

    public KosarViewDTO getKosarViewDTOById(Integer kosarId) {
        return kosarToKosarViewDTO(kosarRepository.getById(kosarId));
    }

    public List<Termek> findAllTermekNotNullMennyiseg() {
        return termekService.findAllNotNullMennyiseg();
    }

    public List<Termek> findAllTermek() {
        return termekService.findAll();
    }

    public KosarViewDTO deleteTermekMennyiseg(Integer kosarId, Integer termekMId) {
        Kosar kosar = kosarRepository.findById(kosarId).orElseThrow();
        TermekMennyiseg termekMenny = kosar.getTermekMennyisegek().stream()
                .filter(termekMennyiseg -> termekMennyiseg.getId()
                        .equals(termekMId))
                .findAny()
                .orElseThrow();
        Termek termek = termekService.getById(termekMenny.getTermek().getId());
        termek.setMennyiseg(termek.getMennyiseg() + termekMenny.getMennyiseg());
        kosar.getTermekMennyisegek().remove(termekMenny);
        termekMennyisegRepository.delete(termekMenny);
        return kosarToKosarViewDTO(kosar);
    }

    public TermekMennyisegHozzaadasCommand kivalasztottTermekHozzaad(Integer kosarId, Integer termekMId) {
        Kosar kosar = kosarRepository.getById(kosarId);
        TermekMennyiseg termekM = kosar.getTermekMennyisegek().stream()
                .filter(termekMennyiseg -> termekMennyiseg.getId().equals(termekMId))
                .findAny()
                .orElseThrow();
        //termekM.getTermek().setMennyiseg(termekM.getTermek().getMennyiseg() + termekM.getMennyiseg());
        return TermekMennyisegHozzaadasCommand.builder()
                .vonalkod(termekM.getTermek().getVonalkod())
                .mennyiseg(termekM.getMennyiseg())
                .build();
    }

    public KosarViewDTO termekMennyisegModosit(Integer kosarId, TermekMennyisegHozzaadasCommand command) {
        Kosar kosar = kosarRepository.findById(kosarId).orElseThrow();
        TermekMennyiseg termekMennyiseg = kosar.getTermekMennyisegek().stream()
                .filter(termekM -> termekM.getTermek().getVonalkod().equals(command.getVonalkod()))
                .findAny().orElseThrow();
        termekMennyiseg.getTermek().setMennyiseg(
                termekMennyiseg.getTermek().getMennyiseg() + termekMennyiseg.getMennyiseg());
        mennyisegValidacio(command.getVonalkod(), command.getMennyiseg());
        //termekMennyiseg.getTermek().setMennyiseg(
        //        termekMennyiseg.getTermek().getMennyiseg() - command.getMennyiseg() );
        termekMennyiseg.setMennyiseg(command.getMennyiseg());

        return kosarToKosarViewDTO(kosar);
    }

    public void kosarDeleteById(Integer kosarId) {
        Kosar kosar = kosarRepository.getById(kosarId);
        for (TermekMennyiseg termekMennyiseg : kosar.getTermekMennyisegek()) {
            Termek termek = termekMennyiseg.getTermek();
            termek.setMennyiseg(termek.getMennyiseg() + termekMennyiseg.getMennyiseg());
        }
        kosarRepository.delete(kosar);
    }

    public Kosar getById(Integer id) {
        return kosarRepository.getById(id);
    }

    public List<KosarViewDTO> findAllKosarViewDto() {
        return kosarRepository.findAll().stream()
                .map(this::kosarToKosarViewDTO).toList();
    }

    public KosarViewDTO kosarHitelesit(Integer kosarId, String hitelesitKod) {
        Kosar kosar = kosarRepository.getById(kosarId);
        String hitelesitKodcserelt = hitelesitKod.replaceAll("ö", "0");
        List<Felhasznalo> felhasznalok = felhasznaloService.findAll();
        Optional<Felhasznalo> felhasznaloOpt = felhasznalok.stream()
                .filter(felhasznalo -> felhasznalo.getHitelesitoKod().equals(hitelesitKodcserelt) && !felhasznalo.getHitelesitoKod().isEmpty())
                .findAny();
        if (felhasznaloOpt.isPresent()) {
            kosar.setHitelesites(Hitelesites.HITELESITVE);
        } else {
            throw new ErvenytelenHitelesitoKodException();
        }
        return kosarToKosarViewDTO(kosar);
    }
}
