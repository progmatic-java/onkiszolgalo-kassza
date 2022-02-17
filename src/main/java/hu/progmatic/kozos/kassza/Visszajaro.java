package hu.progmatic.kozos.kassza;

import java.util.*;

public class Visszajaro {

    private final Kosar kosar;
    private final Bankjegy bankjegy;
    private final List<Bankjegy> banjegyek = new ArrayList<>();

    public Visszajaro(Kosar kosar, Bankjegy bankjegy) {
        this.kosar = kosar;
        this.bankjegy = bankjegy;
    }


    public void addBedobottCimletToKosar() {
        Optional<BedobottBankjegy> bedobottMennyiseg = kosar.getBedobottBankjegyek().stream()
                .filter(bankj -> bankj.getBankjegy().getErtek().equals(this.bankjegy.getErtek())).findAny();
        if (bedobottMennyiseg.isEmpty()) {
            kosar.getBedobottBankjegyek().add(BedobottBankjegy.builder()
                    .bedobottMenyiseg(1)
                    .bankjegy(this.bankjegy)
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
}
