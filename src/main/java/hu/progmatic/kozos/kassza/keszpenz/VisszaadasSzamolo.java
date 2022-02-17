package hu.progmatic.kozos.kassza.keszpenz;

import hu.progmatic.kozos.kassza.keszpenz.Bankjegy;

import java.util.ArrayList;
import java.util.List;

public class VisszaadasSzamolo {

    private List<Bankjegy> banjegyek;
    private Integer kulonbozet;
    private final List<Bankjegy> visszajaroLista = new ArrayList<>();

    public VisszaadasSzamolo() {
    }

    public VisszaadasSzamolo(List<Bankjegy> bankjegyek, Integer kulonbozet) {
        this.banjegyek = bankjegyek.stream()
                .sorted((o1, o2) -> o2.getErtek() - o1.getErtek())
                .toList();
        this.kulonbozet = kulonbozet;
    }

    public boolean visszaadasSzamolo() {
        if (kulonbozet < 0){
            throw new NemLehetNegativErtekException("Visszajáró csak a végösszegnél nagyobb bedobott érték esetén lehet!");
        }
            for (Bankjegy bankjegy : banjegyek) {
                Bankjegy visszajaroBankjegy = Bankjegy.bankjegyCloneFactory(bankjegy);
                visszajaroBankjegy.setMennyiseg(0);
                visszajaroLista.add(visszajaroBankjegy);
                while (kulonbozet >= bankjegy.getErtek() && bankjegy.getMennyiseg() > 0) {
                    bankjegy.csokkentes();
                    kulonbozet -= bankjegy.getErtek();
                    visszajaroBankjegy.noveles();
                }
            }
        if (kulonbozet.equals(0)) {
            return true;
        } else {
            visszajaroLista.removeAll(visszajaroLista);
            return false;
        }
    }


    public List<Bankjegy> getVisszajaroLista() {
        return visszajaroLista;
    }


    public void setBanjegyek(List<Bankjegy> banjegyek) {
        this.banjegyek = banjegyek.stream()
                .sorted((o1, o2) -> o2.getErtek() - o1.getErtek())
                .toList();
    }

    public void setKulonbozet(Integer kulonbozet) {
        this.kulonbozet = kulonbozet;
    }
}
