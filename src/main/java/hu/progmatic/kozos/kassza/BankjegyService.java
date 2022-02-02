package hu.progmatic.kozos.kassza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BankjegyService {


    @Autowired
    private BankjegyRepository bankjegyRepository;

    public int visszajaro(int vegosszeg, int befizetett) {
        return befizetett - vegosszeg;
    }

    public Map<String, Integer> bankjegyek(int vegosszeg, int befizetett) {
        Map<String, Integer> cimletek = new HashMap<>();
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

        while (visszajaro >= 10000 && bankjegyRepository.findByErtek(10000).orElseThrow().getMennyiseg() > 0) {
            visszajaro -= 10000;
            tizEzresDarab++;
            bankjegyRepository.findByErtek(10000).orElseThrow()
                    .setMennyiseg(bankjegyRepository.findByErtek(10000).orElseThrow().getMennyiseg()-1);
        }
        while (visszajaro >= 5000 && bankjegyRepository.findByErtek(5000).orElseThrow().getMennyiseg() > 0) {
            visszajaro -= 5000;
            otEzresDarab++;
            bankjegyRepository.findByErtek(5000).orElseThrow()
                    .setMennyiseg(bankjegyRepository.findByErtek(5000).orElseThrow().getMennyiseg()-1);
        }
        while (visszajaro >= 2000 && bankjegyRepository.findByErtek(2000).orElseThrow().getMennyiseg() > 0) {
            visszajaro -= 2000;
            ketEzresDarab++;
            bankjegyRepository.findByErtek(2000).orElseThrow()
                    .setMennyiseg(bankjegyRepository.findByErtek(2000).orElseThrow().getMennyiseg()-1);
        }
        while (visszajaro >= 1000 && bankjegyRepository.findByErtek(1000).orElseThrow().getMennyiseg() > 0) {
            visszajaro -= 1000;
            ezresDarab++;
            bankjegyRepository.findByErtek(1000).orElseThrow()
                    .setMennyiseg(bankjegyRepository.findByErtek(1000).orElseThrow().getMennyiseg()-1);
        }
        while (visszajaro >= 500 && bankjegyRepository.findByErtek(500).orElseThrow().getMennyiseg() > 0) {
            visszajaro -= 500;
            otszazasDarab++;
            bankjegyRepository.findByErtek(500).orElseThrow()
                    .setMennyiseg(bankjegyRepository.findByErtek(500).orElseThrow().getMennyiseg()-1);
        }
        while (visszajaro >= 200 && bankjegyRepository.findByErtek(200).orElseThrow().getMennyiseg() > 0) {
            visszajaro -= 200;
            ketszazasDarab++;
            bankjegyRepository.findByErtek(200).orElseThrow()
                    .setMennyiseg(bankjegyRepository.findByErtek(200).orElseThrow().getMennyiseg()-1);
        }
        while (visszajaro >= 100 && bankjegyRepository.findByErtek(100).orElseThrow().getMennyiseg() > 0) {
            visszajaro -= 100;
            szazasDarab++;
            bankjegyRepository.findByErtek(100).orElseThrow()
                    .setMennyiseg(bankjegyRepository.findByErtek(100).orElseThrow().getMennyiseg()-1);
        }
        while (visszajaro >= 50 && bankjegyRepository.findByErtek(50).orElseThrow().getMennyiseg() > 0) {
            visszajaro -= 50;
            otvenesDarab++;
            bankjegyRepository.findByErtek(50).orElseThrow()
                    .setMennyiseg(bankjegyRepository.findByErtek(50).orElseThrow().getMennyiseg()-1);
        }
        while (visszajaro >= 20 && bankjegyRepository.findByErtek(20).orElseThrow().getMennyiseg() > 0) {
            visszajaro -= 20;
            huszasDarab++;
            bankjegyRepository.findByErtek(20).orElseThrow()
                    .setMennyiseg(bankjegyRepository.findByErtek(20).orElseThrow().getMennyiseg()-1);
        }
        while (visszajaro >= 10 && bankjegyRepository.findByErtek(10).orElseThrow().getMennyiseg() > 0) {
            visszajaro -= 10;
            tizesDarab++;
            bankjegyRepository.findByErtek(10).orElseThrow()
                    .setMennyiseg(bankjegyRepository.findByErtek(10).orElseThrow().getMennyiseg()-1);
        }
        while (visszajaro >= 5 && bankjegyRepository.findByErtek(5).orElseThrow().getMennyiseg() > 0) {
            visszajaro -= 5;
            otosDarab++;
            bankjegyRepository.findByErtek(5).orElseThrow()
                    .setMennyiseg(bankjegyRepository.findByErtek(5).orElseThrow().getMennyiseg()-1);
        }
        if (visszajaro >= 3 && bankjegyRepository.findByErtek(5).orElseThrow().getMennyiseg() > 0) {
            otosDarab++;
            bankjegyRepository.findByErtek(5).orElseThrow()
                    .setMennyiseg(bankjegyRepository.findByErtek(5).orElseThrow().getMennyiseg()-1);
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

    public void save(List<Bankjegy> bankjegyek) {
        bankjegyRepository.saveAll(bankjegyek);
    }

    public void deleteAll() {
        bankjegyRepository.deleteAll();
    }
}
