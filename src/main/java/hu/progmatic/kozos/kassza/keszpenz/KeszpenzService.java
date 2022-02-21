package hu.progmatic.kozos.kassza.keszpenz;

import hu.progmatic.kozos.kassza.Kosar;
import hu.progmatic.kozos.kassza.KosarService;
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
        if (keszpenzDto.getBedobottCimlet().equals(0)){
            keszpenzDto.setEnabledBankjegyek(rendezCsokkeno(keszpenzVisszaadas.enabledBankjegyekMeghatarozasa()));
            keszpenzDto = initKeszpenzDto(keszpenzDto, keszpenzVisszaadas.getKosarVegosszegRoundFive());
            return keszpenzDto;
        }
        if (keszpenzDto.isFizetesMegszakitas()){
            keszpenzVisszaadas.fizetesVisszavonas();
        }else{
            keszpenzVisszaadas.addBedobottCimlet(bankjegyService.findByErtek(keszpenzDto.getBedobottCimlet()));
            keszpenzVisszaadas.szamolas();
        }
        keszpenzDto.setVegosszeg(keszpenzVisszaadas.getKosarVegosszegRoundFive());
        keszpenzDto.setMaradek(keszpenzVisszaadas.getMaradek());
        keszpenzDto.setEnabledBankjegyek(rendezCsokkeno(keszpenzVisszaadas.enabledBankjegyekMeghatarozasa()));
        keszpenzDto.setVisszajaro(
                visszajaroListToStr(
                        keszpenzVisszaadas.getVisszajaroBankjegyek()));

        keszpenzDto.getEnabledBankjegyek().stream().filter(enabledBankjegyDto -> enabledBankjegyDto.getBankjegyErtek()==5).map(EnabledBankjegyDto::isEngedelyezve).findFirst();
        return keszpenzDto;
    }

    private List<EnabledBankjegyDto> rendezCsokkeno(List<EnabledBankjegyDto> enabledBankjegyDtoList){
        return enabledBankjegyDtoList.stream().sorted((o1, o2) -> o2.getBankjegyErtek() - o1.getBankjegyErtek())
                .toList();
    }

    private KeszpenzDto initKeszpenzDto(KeszpenzDto keszpenzDto, Integer vegosszeg) {
       keszpenzDto.setVegosszeg(vegosszeg);
       keszpenzDto.setMaradek(vegosszeg);
        return keszpenzDto;

    }

    private String visszajaroListToStr(List<Bankjegy> visszajaroList) {
        if (visszajaroList.isEmpty()) {
            return "0 Ft";
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
