package hu.progmatic.kozos.kassza.keszpenz;

import hu.progmatic.kozos.kassza.Kosar;

import java.util.ArrayList;
import java.util.List;

public class KeszpenzVisszaadas {

    private final VisszajaroElokeszito visszajaroElokeszito;
    private final List<Bankjegy> bankjegyek;
    private Integer maradek;
    private List<Bankjegy> visszajaroBankjegyek;

    public KeszpenzVisszaadas(Kosar kosar, List<Bankjegy> bankjegyek){

        this.visszajaroElokeszito = new VisszajaroElokeszito(kosar);
        this.bankjegyek = bankjegyek;
    }

    public void addBedobottCimlet(Bankjegy bankjegy){
        bankjegy.noveles();
        visszajaroElokeszito.addBedobottCimletToKosar(bankjegy);
    }

    public void szamolas(){
        if (visszajaroElokeszito.getKulonbozet() <= 0){
            maradek = Math.abs(visszajaroElokeszito.getKulonbozet());
        }else{
            VisszaadasSzamolo visszaadasSzamolo =
                    new VisszaadasSzamolo(bankjegyek, visszajaroElokeszito.getKulonbozet());
            if (visszaadasSzamolo.visszaadasSzamolo()){
                visszajaroBankjegyek = visszaadasSzamolo.getVisszajaroLista();
            }
        }
    }

    public List<EnabledBankjegyDto> enabledBankjegyekMeghatarozasa(){
        List<Bankjegy> bankjegyekClone = bankjegyek.stream()
                .map(Bankjegy::bankjegyCloneFactory)
                .toList();
        VisszaadasSzamolo visszaadasSzamolo = new VisszaadasSzamolo();
        boolean engedelyezve;
        List<EnabledBankjegyDto> enabledBankjegyek = new ArrayList<>();
        for (Bankjegy bankjegyClone : bankjegyekClone) {
            Integer kulonbozet = visszajaroElokeszito.getKulonbozet() + bankjegyClone.getErtek();
            enabledBankjegyek.add(enabledBankjegyDtoBuilder(bankjegyClone, true));
            if (kulonbozet >= 0){
                visszaadasSzamolo.setBanjegyek(bankjegyekClone);
                visszaadasSzamolo.setKulonbozet(kulonbozet);
                engedelyezve = visszaadasSzamolo.visszaadasSzamolo();
                enabledBankjegyek.add(enabledBankjegyDtoBuilder(bankjegyClone, engedelyezve));
            }
        }
        return enabledBankjegyek;
    }

    public EnabledBankjegyDto enabledBankjegyDtoBuilder(Bankjegy bankjegy, boolean engedelyezve){
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

    public Integer getKulonbozet(){
        return visszajaroElokeszito.getKulonbozet();
    }
}
