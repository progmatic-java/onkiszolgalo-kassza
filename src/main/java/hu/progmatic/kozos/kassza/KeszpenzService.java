package hu.progmatic.kozos.kassza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class KeszpenzService {


    @Autowired
    private BankjegyService bankjegyService;

    @Autowired
    private KosarService kosarService;


    private Integer osszegBedobva(List<BedobottBankjegy> bedobottBankjegyek) {
        return bedobottBankjegyek.stream()
                .mapToInt(bedobottBankjegy -> (bedobottBankjegy.getBedobottMenyiseg() * bedobottBankjegy.getBankjegy().getErtek()))
                .sum();
    }

    public KeszpenzDto visszajaro(KeszpenzDto keszpenzDto) {
        Kosar kosar = kosarService.getById(keszpenzDto.getKosarId());
        Integer vegosszeg = kosarVegosszegRoundFive(kosarService.kosarVegosszeg(kosar));
        keszpenzDto.setVegosszeg(vegosszeg);
        if (keszpenzDto.getBedobottCimlet() == null) {
            keszpenzDto.setMaradek(vegosszeg);
            return keszpenzDto;
        }
        Bankjegy bankjegy = bankjegyService.findByErtek(keszpenzDto.getBedobottCimlet());
        addBedobottCimletToKosar(kosar, keszpenzDto.getBedobottCimlet());
        bankjegy.setMennyiseg(bankjegy.getMennyiseg() + 1);
        Integer osszesBedobottPenz = osszegBedobva(kosar.getBedobottBankjegyek());
        if (vegosszeg - osszesBedobottPenz > 0) {
            keszpenzDto.setMaradek(vegosszeg - osszesBedobottPenz);
            return keszpenzDto;
        } else if (vegosszeg - osszesBedobottPenz == 0) {
            kosar.getBedobottBankjegyek().removeAll(kosar.getBedobottBankjegyek());
            keszpenzDto.setMaradek(0);
            keszpenzDto.setVisszajaro("0 Ft");
            return keszpenzDto;
        } else {
            Integer kulonbozet = osszesBedobottPenz - vegosszeg;
            List<Bankjegy> bankjegyek = bankjegyService.findAll().stream()
                    .sorted((o1, o2) -> o2.getErtek() - o1.getErtek())
                    .toList();
            Map<Integer, Integer> visszajaroMap = new HashMap<>();
            for (Bankjegy bankj : bankjegyek) {
                while (kulonbozet >= bankj.getErtek() && bankj.getMennyiseg() > 0) {
                    Integer cimletDb = visszajaroMap.getOrDefault(bankj.getErtek(), 0);
                    visszajaroMap.put(bankj.getErtek(), cimletDb + 1);
                    kulonbozet -= bankj.getErtek();
                    bankj.setMennyiseg(bankj.getMennyiseg() - 1);
                }
            }
            if (kulonbozet.equals(0)){
                keszpenzDto.setMaradek(0);
                keszpenzDto.setVisszajaro(vissazjaroMapToStr(visszajaroMap));
            }else{
                mapBankjegyAddToDatabase(visszajaroMap);
                bedobottCimletekLevonasaFromDataBase(kosar.getBedobottBankjegyek());
                kosar.getBedobottBankjegyek().removeAll(kosar.getBedobottBankjegyek());
                keszpenzDto.setMaradek(vegosszeg);
                keszpenzDto.setNemTudVisszaadni(true);
            }
            return keszpenzDto;
        }

    }

    private void bedobottCimletekLevonasaFromDataBase(List<BedobottBankjegy> bedobottBankjegyek){
        for (BedobottBankjegy bedobottBankjegy : bedobottBankjegyek){
            Bankjegy bankjegy = bedobottBankjegy.getBankjegy();
            bankjegy.setMennyiseg(bankjegy.getMennyiseg() + bedobottBankjegy.getBedobottMenyiseg());
        }
    }

    private void mapBankjegyAddToDatabase(Map<Integer, Integer> penzek){
        for (Map.Entry<Integer,Integer> entry : penzek.entrySet()){
            Bankjegy bankjegy = bankjegyService.findByErtek(entry.getKey());
            bankjegy.setMennyiseg(bankjegy.getMennyiseg() + entry.getValue());
        }
    }

    private String vissazjaroMapToStr(Map<Integer, Integer> visszajaroMap){
        String visszajaroStr = "";
        for (Map.Entry<Integer,Integer> entry : visszajaroMap.entrySet()){
            visszajaroStr += String.format("%s db %s Ft-os címlet", entry.getValue(), entry.getKey());
            visszajaroStr += "; ";
        }
        return visszajaroStr;
    }

    private void addBedobottCimletToKosar(Kosar kosar, Integer cimlet) {
        Optional<BedobottBankjegy> marLetezoBankjegy = kosar.getBedobottBankjegyek().stream()
                .filter(bBankjegy -> bBankjegy.getBankjegy().getErtek().equals(cimlet))
                .findAny();
        if (marLetezoBankjegy.isEmpty()) {
            kosar.getBedobottBankjegyek().add(BedobottBankjegy.builder()
                    .bedobottMenyiseg(1)
                    .bankjegy(bankjegyService.findByErtek(cimlet))
                    .kosar(kosar)
                    .build());
        } else {
            marLetezoBankjegy.get().setBedobottMenyiseg(marLetezoBankjegy.get().getBedobottMenyiseg() + 1);
        }
    }

    public Integer kosarVegosszegRoundFive(Integer vegosszeg) {
        if (vegosszeg % 5 == 1) {
            vegosszeg -= 1;
        }
        if(vegosszeg % 5 == 2){
            vegosszeg -= 2;
        }
        if(vegosszeg % 5 == 3){
            vegosszeg += 2;
        }
        if (vegosszeg % 5 == 4){
            vegosszeg += 1;
        }
        return vegosszeg;
    }

}
