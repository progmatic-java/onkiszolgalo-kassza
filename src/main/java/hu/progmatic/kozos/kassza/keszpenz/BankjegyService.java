package hu.progmatic.kozos.kassza.keszpenz;

import hu.progmatic.kozos.kassza.keszpenz.Bankjegy;
import hu.progmatic.kozos.kassza.keszpenz.BankjegyRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class BankjegyService implements InitializingBean {


    @Autowired
    private BankjegyRepository bankjegyRepository;

    private List<Bankjegy> iniBankjegyek = List.of(
            Bankjegy.builder()
                    .ertek(20000)
                    .mennyiseg(0)
                    .build(),
            Bankjegy.builder()
                    .ertek(10000)
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(5000)
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(2000)
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(1000)
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(500)
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(200)
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(100)
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(50)
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(20)
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(10)
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(5)
                    .mennyiseg(3)
                    .build()
    );
    @Override
    public void afterPropertiesSet() {
        if(bankjegyRepository.count() == 0){
            bankjegyRepository.saveAll(iniBankjegyek);
        }
    }


    public Bankjegy findByErtek(Integer ertek){
        return bankjegyRepository.findByErtek(ertek).orElseThrow();
    }

    public List<Bankjegy> findAll(){
        return bankjegyRepository.findAll();
    }

    public Bankjegy editById(Integer id, Integer mennyiseg){
        Bankjegy bankjegy = bankjegyRepository.getById(id);
        bankjegy.setMennyiseg(mennyiseg);
        return bankjegy;
    }

    public void clear(){
        List<Bankjegy> bankjegyek = bankjegyRepository.findAll();
        for (Bankjegy bankjegy : bankjegyek){
            bankjegy.setMennyiseg(0);
        }
    }


    public Bankjegy getById(Integer id) {
        return bankjegyRepository.getById(id);
    }
}
