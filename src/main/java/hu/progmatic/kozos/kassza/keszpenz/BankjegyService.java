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

    /*@Autowired
    private KosarService kosarService;*/


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
    /*private int visszajaro(int vegosszeg, int befizetett) {
        return befizetett - vegosszeg;
    }

    public Map<String, Integer> bankjegyek(int vegosszeg, int befizetett) {
        Map<String, Integer> cimletek = new TreeMap<>();
        int visszajaro = visszajaro(vegosszeg, befizetett);
        int tizEzresDarab = 0;
        int otEzresDarab = 0;
        int ketEzresDarab = 0;
        int ezresDarab = 0;
        int otszazasDarab = 0;
        int ketszazasDarab = 0;
        int szazasDarab = 0;
        int otvenesDarab = 0;
        int huszasDarab = 0;
        int tizesDarab = 0;
        int otosDarab = 0;

        while (visszajaro >= 10000 && vanBelole(10000)) {
            visszajaro -= 10000;
            tizEzresDarab++;
            mennyisegCsokkentese(10000);
        }
        while (visszajaro >= 5000 && vanBelole(5000)) {
            visszajaro -= 5000;
            otEzresDarab++;
            mennyisegCsokkentese(5000);
        }
        while (visszajaro >= 2000 && vanBelole(2000)) {
            visszajaro -= 2000;
            ketEzresDarab++;
            mennyisegCsokkentese(2000);
        }
        while (visszajaro >= 1000 && vanBelole(1000)) {
            visszajaro -= 1000;
            ezresDarab++;
            mennyisegCsokkentese(1000);
        }
        while (visszajaro >= 500 && vanBelole(500)) {
            visszajaro -= 500;
            otszazasDarab++;
            mennyisegCsokkentese(500);

        }
        while (visszajaro >= 200 && vanBelole(200)) {
            visszajaro -= 200;
            ketszazasDarab++;
            mennyisegCsokkentese(200);
        }
        while (visszajaro >= 100 && vanBelole(100)) {
            visszajaro -= 100;
            szazasDarab++;
            mennyisegCsokkentese(100);
        }
        while (visszajaro >= 50 && vanBelole(50)) {
            visszajaro -= 50;
            otvenesDarab++;
            mennyisegCsokkentese(50);
        }
        while (visszajaro >= 20 && vanBelole(20)) {
            visszajaro -= 20;
            huszasDarab++;
            mennyisegCsokkentese(20);
        }
        while (visszajaro >= 10 && vanBelole(10)) {
            visszajaro -= 10;
            tizesDarab++;
            mennyisegCsokkentese(10);
        }
        while (visszajaro >= 5 && vanBelole(5)) {
            visszajaro -= 5;
            otosDarab++;
            mennyisegCsokkentese(5);
        }
        if (visszajaro >= 3 && vanBelole(5)) {
            otosDarab++;
            mennyisegCsokkentese(5);
        }


        cimletek.put("tizezres", tizEzresDarab);
        cimletek.put("otezres", otEzresDarab);
        cimletek.put("ketezres", ketEzresDarab);
        cimletek.put("ezres", ezresDarab);
        cimletek.put("otszazas", otszazasDarab);
        cimletek.put("ketszazas", ketszazasDarab);
        cimletek.put("szazas", szazasDarab);
        cimletek.put("otvenes", otvenesDarab);
        cimletek.put("huszas", huszasDarab);
        cimletek.put("tizes", tizesDarab);
        cimletek.put("otos", otosDarab);


        return cimletek;
    }

    private boolean vanBelole(int ertek) {
        return bankjegyRepository.findByErtek(ertek).isPresent();
    }

    private void mennyisegCsokkentese(Integer ertek) {
        Bankjegy bankjegy = bankjegyRepository.findByErtek(ertek).orElseThrow();
        bankjegy.setMennyiseg(bankjegy.getMennyiseg() - 1);
    }*/


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

/////////////////////////////////////////////////////////////////////////////////////6
   /* private Integer osszegBedobva(List<BedobottBankjegy> bedobottBankjegyek) {
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
        Bankjegy bankjegy = bankjegyRepository.findByErtek(keszpenzDto.getBedobottCimlet()).orElseThrow();
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
            List<Bankjegy> bankjegyek = bankjegyRepository.findAll().stream()
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
            Bankjegy bankjegy = bankjegyRepository.findByErtek(entry.getKey()).orElseThrow();
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
                    .bankjegy(bankjegyRepository.findByErtek(cimlet).orElseThrow())
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
    }*/
//////////////////////////////////////////////////////////////////
    public void clear(){
        List<Bankjegy> bankjegyek = bankjegyRepository.findAll();
        for (Bankjegy bankjegy : bankjegyek){
            bankjegy.setMennyiseg(0);
        }
    }


}
