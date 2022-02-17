package hu.progmatic.kozos.kassza.keszpenz;

import hu.progmatic.kozos.kassza.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class KeszpenzService {


    @Autowired
    private BankjegyService bankjegyService;

    @Autowired
    private KosarService kosarService;


    public KeszpenzDto visszajaro(KeszpenzDto keszpenzDto){
        Kosar kosar = kosarService.getById(keszpenzDto.getKosarId());
        List<Bankjegy> bankjegyek = bankjegyService.findAll();
        KeszpenzVisszaadas keszpenzVisszaadas = new KeszpenzVisszaadas(kosar, bankjegyek);
        if (keszpenzDto.getBedobottCimlet() == null){
            keszpenzDto.setEnabledBankjegyek(keszpenzVisszaadas.enabledBankjegyekMeghatarozasa());
            keszpenzDto.setVegosszeg(keszpenzVisszaadas.getKosarVegosszegRoundFive());
            return keszpenzDto;
        }
        keszpenzVisszaadas.addBedobottCimlet(bankjegyService.findByErtek(keszpenzDto.getBedobottCimlet()));
        keszpenzVisszaadas.szamolas();
        return null;
    }

}
