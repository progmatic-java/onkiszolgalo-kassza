package hu.progmatic.kozos.kassza.keszpenz;

import hu.progmatic.kozos.kassza.Kosar;

import java.util.*;

public class VisszajaroElokeszito {

    private final Kosar kosar;

    public VisszajaroElokeszito(Kosar kosar) {
        this.kosar = kosar;
    }


    public void addBedobottCimletToKosar(Bankjegy bankjegy) {
        Optional<BedobottBankjegy> bedobottMennyiseg = kosar.getBedobottBankjegyek().stream()
                .filter(bankj -> bankj.getBankjegy().getErtek().equals(bankjegy.getErtek())).findAny();
        if (bedobottMennyiseg.isEmpty()) {
            kosar.getBedobottBankjegyek().add(BedobottBankjegy.builder()
                    .bedobottMenyiseg(1)
                    .bankjegy(bankjegy)
                    .kosar(kosar)
                    .build());
        } else {
            bedobottMennyiseg.get().setBedobottMenyiseg(bedobottMennyiseg.get().getBedobottMenyiseg() + 1);
        }
    }

    public Integer getKosarVegosszegRoundFive() {
        Integer vegosszeg = Kosar.kosarVegosszeg(kosar);
        if (vegosszeg % 5 == 1) {
            vegosszeg -= 1;
        }
        if (vegosszeg % 5 == 2) {
            vegosszeg -= 2;
        }
        if (vegosszeg % 5 == 3) {
            vegosszeg += 2;
        }
        if (vegosszeg % 5 == 4) {
            vegosszeg += 1;
        }
        return vegosszeg;
    }

    public Integer osszegBedobva() {
        return kosar.getBedobottBankjegyek().stream()
                .mapToInt(bedobottBankjegy -> (bedobottBankjegy.getBedobottMenyiseg() * bedobottBankjegy.getBankjegy().getErtek()))
                .sum();
    }
    public void clearBedobottBankjegyek(){
        kosar.getBedobottBankjegyek().clear();
    }

    public Integer getKulonbozet(){
        return osszegBedobva() - getKosarVegosszegRoundFive();
    }

    public List<BedobottBankjegy> getBedobottBankjegyek(){
        return kosar.getBedobottBankjegyek();
    }
}
