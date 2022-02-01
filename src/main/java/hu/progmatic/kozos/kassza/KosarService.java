package hu.progmatic.kozos.kassza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class KosarService {

    @Autowired
    private KosarRepositoryProba repository;

    @Autowired
    private TermekRepository termekRepository;


    public Kosar save(Kosar kosar) {
        return repository.saveAndFlush(kosar);
    }

    public KosarViewDTO getKosarDtoById(Integer id) {
        return kosarToKosarViewDTO(repository.getById(id));
    }

    public void saveAll(List<Kosar> initItems) {
        repository.saveAllAndFlush(initItems);
    }


    public KosarViewDTO kosarViewCreate() {
        Kosar kosar = repository.save(Kosar.builder().build());
        return KosarViewDTO.builder()
                .kosarId(kosar.getId())
                .build();
    }

    public KosarViewDTO addTermekMennyisegCommand(TermekMennyisegHozzaadasCommand command) {
        Kosar kosar = repository.getById(command.getKosarId());
        kosar.getTermekMennyisegek().add(TermekMennyiseg.builder()
                .mennyiseg(command.getMennyiseg())
                .termek(termekRepository.findByVonalkod(command.getVonalkod()))
                .build());
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
        repository.delete(repository.getById(id));
    }
}
