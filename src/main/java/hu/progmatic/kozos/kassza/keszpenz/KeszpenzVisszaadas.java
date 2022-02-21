package hu.progmatic.kozos.kassza.keszpenz;

import hu.progmatic.kozos.kassza.Kosar;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KeszpenzVisszaadas {

    private final VisszajaroElokeszito visszajaroElokeszito;
    private final List<Bankjegy> bankjegyek;
    private Integer maradek;
    private List<Bankjegy> visszajaroBankjegyek = new ArrayList<>();
    private boolean megszakitas = false;

    public KeszpenzVisszaadas(Kosar kosar, List<Bankjegy> bankjegyek) {

        this.visszajaroElokeszito = new VisszajaroElokeszito(kosar);
        this.bankjegyek = bankjegyek;
    }

    public void addBedobottCimlet(Bankjegy bedobottBankjegy) {
        Bankjegy bankjegy = bankjegyek.stream()
                .filter(bj -> bj.getErtek().equals(bedobottBankjegy.getErtek()))
                .findAny()
                .orElseThrow();
        bankjegy.noveles();
        visszajaroElokeszito.addBedobottCimletToKosar(bedobottBankjegy);
    }

    public void szamolas() {
        if (visszajaroElokeszito.getKulonbozet() <= 0) {
            maradek = Math.abs(visszajaroElokeszito.getKulonbozet());
        } else {
            VisszaadasSzamolo visszaadasSzamolo =
                    new VisszaadasSzamolo(bankjegyek, visszajaroElokeszito.getKulonbozet());
            if (visszaadasSzamolo.visszaadasSzamolo()) {
                visszajaroBankjegyek = visszaadasSzamolo.getVisszajaroLista();
                maradek = 0;
            } else {
                throw new NemEngedelyezettBankjegyException("Érvénytelen bankjegy!");
            }
        }
    }

    public void fizetesVisszavonas() {
        List<BedobottBankjegy> bedobottBankjegyList = visszajaroElokeszito.getBedobottBankjegyek();
        for (Bankjegy bankjegy : bankjegyek) {
            Optional<BedobottBankjegy> bedobottBankjegyOp = bedobottBankjegyList.stream()
                    .filter(bBJ -> bBJ.getBankjegy().getErtek().equals(bankjegy.getErtek()))
                    .findAny();
            Bankjegy visszajaro = Bankjegy.bankjegyCloneFactory(bankjegy);
            if (bedobottBankjegyOp.isEmpty()) {
                visszajaro.setMennyiseg(0);
                visszajaroBankjegyek.add(visszajaro);
            }else{
                visszajaro.setMennyiseg(bedobottBankjegyOp.get().getBedobottMenyiseg());
                visszajaroBankjegyek.add(visszajaro);
                bankjegy.setMennyiseg(bankjegy.getMennyiseg() - bedobottBankjegyOp.get().getBedobottMenyiseg());
            }

        }
        megszakitas = true;
        maradek = visszajaroElokeszito.getKosarVegosszegRoundFive();
        visszajaroElokeszito.clearBedobottBankjegyek();
    }

    public List<EnabledBankjegyDto> enabledBankjegyekMeghatarozasa() {
        /*List<Bankjegy> bankjegyekClone = bankjegyek.stream()
                .map(Bankjegy::bankjegyCloneFactory)
                .toList();*/
        VisszaadasSzamolo visszaadasSzamolo = new VisszaadasSzamolo();
        boolean engedelyezve;
        List<EnabledBankjegyDto> enabledBankjegyek = new ArrayList<>();
        //for (Bankjegy bankjegyClone : bankjegyekClone) {
        for (Bankjegy bankjegy : bankjegyek) {
            List<Bankjegy> bankjegyekClone = bankjegyek.stream()
                    .map(Bankjegy::bankjegyCloneFactory)
                    .toList();
            Bankjegy bankjegyClone = Bankjegy.bankjegyCloneFactory(bankjegy);
            bankjegyekClone.stream().filter(bj->bj.getErtek().equals(bankjegy.getErtek())).findAny().orElseThrow().noveles();
            if (visszajaroElokeszito.getKulonbozet().equals(0) || megszakitas) {
                enabledBankjegyek.add(enabledBankjegyDtoBuilder(bankjegyClone, false));
            } else {
                Integer ujKulonbozet = visszajaroElokeszito.getKulonbozet() + bankjegyClone.getErtek();
                if (ujKulonbozet >= 0) {
                    visszaadasSzamolo.setBankjegyek(bankjegyekClone);
                    visszaadasSzamolo.setKulonbozet(ujKulonbozet);
                    engedelyezve = visszaadasSzamolo.visszaadasSzamolo();
                    enabledBankjegyek.add(enabledBankjegyDtoBuilder(bankjegyClone, engedelyezve));
                } else {
                    enabledBankjegyek.add(enabledBankjegyDtoBuilder(bankjegyClone, true));
                }
            }
        }
        return enabledBankjegyek;
    }

    public EnabledBankjegyDto enabledBankjegyDtoBuilder(Bankjegy bankjegy, boolean engedelyezve) {
        return EnabledBankjegyDto.builder()
                .bankjegyErtek(bankjegy.getErtek())
                .engedelyezve(engedelyezve)
                .build();
    }

    public Integer getKosarVegosszegRoundFive() {
        return visszajaroElokeszito.getKosarVegosszegRoundFive();
    }

    public Integer getMaradek() {
        return maradek;
    }


    public List<Bankjegy> getVisszajaroBankjegyek() {
        return visszajaroBankjegyek;
    }

    public List<Bankjegy> getBankjegyek() {
        return bankjegyek;
    }
}
