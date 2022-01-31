package hu.progmatic.kozos.kassza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KosarSzerviz {
    @Autowired
    private KosarRepositoryProba repository;

    public Kosar save(Kosar kosar) {
        return repository.saveAndFlush(kosar);
    }

    public Kosar getById(Integer id) {
        return repository.getById(id);
    }

    public void saveAll(List<Kosar> initItems) {
        repository.saveAllAndFlush(initItems);
    }


}
