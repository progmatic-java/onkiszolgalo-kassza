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


    public KeszpenzDto visszajaro(KeszpenzDto keszpenzDto) {
        Kosar kosar = kosarService.getById(keszpenzDto.getKosarId());
        List<Bankjegy> bankjegyek = bankjegyService.findAll();
        KeszpenzVisszaadas keszpenzVisszaadas = new KeszpenzVisszaadas(kosar, bankjegyek);
        keszpenzVisszaadas.addBedobottCimlet(bankjegyService.findByErtek(keszpenzDto.getBedobottCimlet()));
        keszpenzVisszaadas.szamolas();
        keszpenzDto.setVegosszeg(keszpenzVisszaadas.getKosarVegosszegRoundFive());
        keszpenzDto.setMaradek(keszpenzVisszaadas.getMaradek());
        keszpenzDto.setEnabledBankjegyek(keszpenzVisszaadas.enabledBankjegyekMeghatarozasa());
        keszpenzDto.setVisszajaro(
                visszajaroListToStr(
                        keszpenzVisszaadas.getVisszajaroBankjegyek()));
        return keszpenzDto;
    }

    private String visszajaroListToStr(List<Bankjegy> visszajaroList) {
        if (visszajaroList == null) {
            return null;
        } else {
            String visszajaroStr = "";
            for (Bankjegy bankjegy : visszajaroList) {
                if (!bankjegy.getMennyiseg().equals(0)) {
                    visszajaroStr +=
                            String.format("%s db: %s Ft, ", bankjegy.getMennyiseg(), bankjegy.getErtek());
                }
            }
            return visszajaroStr.substring(0, visszajaroStr.length() - 2);
        }
    }

}
