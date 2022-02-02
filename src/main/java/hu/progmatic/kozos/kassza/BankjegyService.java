package hu.progmatic.kozos.kassza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
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

        while (visszajaro >= 10000) {
            visszajaro -= 10000;
            tizEzresDarab++;
        }
        while (visszajaro >= 5000) {
            visszajaro -= 5000;
            otEzresDarab++;
        }
        while (visszajaro >= 2000) {
            visszajaro -= 2000;
            ketEzresDarab++;
        }
        while (visszajaro >= 1000) {
            visszajaro -= 1000;
            ezresDarab++;
        }
        while (visszajaro >= 500) {
            visszajaro -= 500;
            otszazasDarab++;
        }
        while (visszajaro >= 200) {
            visszajaro -= 200;
            ketszazasDarab++;
        }
        while (visszajaro >= 100) {
            visszajaro -= 100;
            szazasDarab++;
        }
        while (visszajaro >= 50) {
            visszajaro -= 50;
            otvenesDarab++;
        }
        while (visszajaro >= 20) {
            visszajaro -= 20;
            huszasDarab++;
        }
        while (visszajaro >= 10) {
            visszajaro -= 10;
            tizesDarab++;
        }
        while (visszajaro >= 5) {
            visszajaro -= 5;
            otosDarab++;
        }
        if (visszajaro >= 3) {
            otosDarab++;
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
}
