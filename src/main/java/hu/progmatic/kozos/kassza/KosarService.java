package hu.progmatic.kozos.kassza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class KosarService {

    @Autowired
    private KosarRepositoryProba kosarRepository;

    @Autowired
    private TermekMennyisegRepository termekMennyisegRepository;

    @Autowired
    private TermekService termekService;


    public Kosar save(Kosar kosar) {
        return kosarRepository.save(kosar);
    }

    public KosarViewDTO getKosarDtoById(Integer id) {
        return kosarToKosarViewDTO(kosarRepository.getById(id));
    }

    public void saveAll(List<Kosar> initItems) {
        kosarRepository.saveAll(initItems);
    }


    public KosarViewDTO kosarViewCreate() {
        Kosar kosar = kosarRepository.save(Kosar.builder().build());
        return KosarViewDTO.builder()
                .kosarId(kosar.getId())
                .build();
    }


    public KosarViewDTO addTermekMennyisegCommand(Integer kosarId, TermekMennyisegHozzaadasCommand command) {
        Kosar kosar = kosarRepository.findById(kosarId).orElseThrow();


        mennyisegValidacio(command.getVonalkod(), command.getMennyiseg());
        /*Termek termek = termekService.getByVonalkod(command.getVonalkod());
        int termekMennyiseg = termek.getMennyiseg();
        if (termekMennyiseg < command.getMennyiseg()) {
            throw new NincsElegRaktarKeszletException(
                    String.format("Nincs elég termék a raktáron! Maximálisan %s termék adható hozzá!", termekMennyiseg)
            );
        }
        termek.setMennyiseg(termekMennyiseg - command.getMennyiseg());*/

        Optional<TermekMennyiseg> termekMennyisegOptional = kosar.getTermekMennyisegek().stream()
                .filter(termekM -> termekM.getTermek().getVonalkod().equals(command.getVonalkod()))
                .findAny();
        if (termekMennyisegOptional.isEmpty()) {
            TermekMennyiseg ujTermekMennyiseg = TermekMennyiseg.builder()
                    .mennyiseg(command.getMennyiseg())
                    .termek(termekService.getByVonalkod(command.getVonalkod()))
                    .kosar(kosar)
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
        int termekMennyiseg = termek.getMennyiseg();
        if (termekMennyiseg < mennyiseg) {
            throw new NincsElegRaktarKeszletException(
                    String.format("Nincs elég termék a raktáron! Maximálisan %s termék adható hozzá!", termekMennyiseg)
            );
        }
        termek.setMennyiseg(termekMennyiseg - mennyiseg);
    }

    private KosarViewDTO kosarToKosarViewDTO(Kosar kosar) {
        return KosarViewDTO.builder()
                .kosarId(kosar.getId())
                .vegosszeg(kosar.getTermekMennyisegek().stream()
                        .mapToInt(osszeg -> osszeg.getMennyiseg() * osszeg.getTermek().getAr())
                        .sum())
                .utolsoHozzaadottTermekmennyisegDto(
                        kosar.getUtolsoHozzaadottTermekmennyiseg() == null ? null :
                                TermekMennyisegDto.builder()
                                        .termekMennyisegId(kosar.getUtolsoHozzaadottTermekmennyiseg().getId())
                                        .ar(kosar.getUtolsoHozzaadottTermekmennyiseg().getTermek().getAr())
                                        .nev(kosar.getUtolsoHozzaadottTermekmennyiseg().getTermek().getMegnevezes())
                                        .mennyiseg(kosar.getUtolsoHozzaadottTermekmennyiseg().getMennyiseg())
                                        .build())
                .termekMennyisegDtoList(kosar.getTermekMennyisegek().stream()
                        .sorted((o1, o2) -> o2.getId()- o1.getId())
                        .map(termekMennyiseg -> TermekMennyisegDto.builder()
                                .termekMennyisegId(termekMennyiseg.getId())
                                .ar(termekMennyiseg.getTermek().getAr())
                                .nev(termekMennyiseg.getTermek().getMegnevezes())
                                .mennyiseg(termekMennyiseg.getMennyiseg())
                                .build()).toList())
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

    public List<Termek> findAllTermek(){
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
}
