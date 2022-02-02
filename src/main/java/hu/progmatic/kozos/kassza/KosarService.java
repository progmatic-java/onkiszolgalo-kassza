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
        return kosarRepository.saveAndFlush(kosar);
    }

    public KosarViewDTO getKosarDtoById(Integer id) {
        return kosarToKosarViewDTO(kosarRepository.getById(id));
    }

    public void saveAll(List<Kosar> initItems) {
        kosarRepository.saveAllAndFlush(initItems);
    }


    public KosarViewDTO kosarViewCreate() {
        Kosar kosar = kosarRepository.save(Kosar.builder().build());
        return KosarViewDTO.builder()
                .kosarId(kosar.getId())
                .build();
    }

    public KosarViewDTO addTermekMennyisegCommand(TermekMennyisegHozzaadasCommand command) {
        Kosar kosar = kosarRepository.getById(command.getKosarId());

        Termek termek = termekService.getByVonalkod(command.getVonalkod());
        if (termek.getMennyiseg() < command.getMennyiseg()) {
            throw new NincsElegRaktarKeszletException();
        }
        termek.setMennyiseg(termek.getMennyiseg() - command.getMennyiseg());

        Optional<TermekMennyiseg> termekMennyisegOptional = kosar.getTermekMennyisegek().stream()
                .filter(termekM -> termekM.getTermek().getVonalkod().equals(command.getVonalkod()))
                .findAny();
        if (termekMennyisegOptional.isEmpty()) {
            kosar.getTermekMennyisegek().add(TermekMennyiseg.builder()
                    .mennyiseg(command.getMennyiseg())
                    .termek(termekService.getByVonalkod(command.getVonalkod()))
                    .kosar(kosar)
                    .build());
        }else{
            termekMennyisegOptional.get().setMennyiseg( termekMennyisegOptional.get().getMennyiseg() + command.getMennyiseg());
        }
        return kosarToKosarViewDTO(kosar);
    }

    private KosarViewDTO kosarToKosarViewDTO(Kosar kosar) {
        return KosarViewDTO.builder()
                .kosarId(kosar.getId())
                .vegosszeg(kosar.getTermekMennyisegek().stream()
                        .mapToInt(osszeg -> osszeg.getMennyiseg() * osszeg.getTermek().getAr())
                        .sum())
                .termekMennyisegDtoList(kosar.getTermekMennyisegek().stream()
                        .map(termekMennyiseg -> TermekMennyisegDto.builder()
                                .ar(termekMennyiseg.getTermek().getAr())
                                .nev(termekMennyiseg.getTermek().getMegnevezes())
                                .mennyiseg(termekMennyiseg.getMennyiseg())
                                .build()).toList())
                .build();
    }

    public void deleteKosarById(Integer id) {
        kosarRepository.delete(kosarRepository.getById(id));
    }

    public List<Termek> findAllTermekNotNullMennyiseg(){
        return termekService.findAllNotNullMennyiseg();
    }
}
