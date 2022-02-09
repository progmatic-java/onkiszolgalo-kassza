package hu.progmatic.kozos.kassza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@Transactional
public class BankjegyService {


    @Autowired
    private BankjegyRepository bankjegyRepository;

    private int visszajaro(int vegosszeg, int befizetett) {
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
    }

    public void save(List<Bankjegy> bankjegyek) {
        bankjegyRepository.saveAll(bankjegyek);
    }

    public void deleteAll() {
        bankjegyRepository.deleteAll();
    }
}
