package hu.progmatic.kozos.kassza.keszpenz;


import java.util.ArrayList;
import java.util.List;

public class VisszaadasSzamolo {

    private List<Bankjegy> bankjegyek;
    private Integer kulonbozet;
    private final List<Bankjegy> visszajaroLista = new ArrayList<>();

    public VisszaadasSzamolo() {
    }

    public VisszaadasSzamolo(List<Bankjegy> bankjegyek, Integer kulonbozet) {
        setBankjegyek(bankjegyek);
        setKulonbozet(kulonbozet);
    }

    public boolean visszaadasSzamolo() {
        if (kulonbozet.equals(0)) {
            return true;
        }
        for (Bankjegy bankjegy : bankjegyek) {
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
            return false;
        }
    }

    public List<Bankjegy> getVisszajaroLista() {
        return visszajaroLista;
    }

    public List<Bankjegy> getBankjegyek() {
        return bankjegyek;
    }

    public void setBankjegyek(List<Bankjegy> bankjegyek) {
        this.bankjegyek = bankjegyek.stream()
                .sorted((o1, o2) -> o2.getErtek() - o1.getErtek())
                .toList();
    }

    public void setKulonbozet(Integer kulonbozet) {
        if (kulonbozet < 0) {
            throw new NemLehetNegativErtekException("Visszajáró csak a végösszegnél nagyobb bedobott érték esetén lehet!");
        }
        this.kulonbozet = kulonbozet;
    }
}
