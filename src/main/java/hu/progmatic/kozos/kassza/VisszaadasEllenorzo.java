package hu.progmatic.kozos.kassza;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisszaadasEllenorzo {

    private final Bankjegy bedobottBankjegy;
    private final List<Bankjegy> banjegyek;
    private final Integer vegosszeg;
    private final List<Bankjegy> visszajaroLista = new ArrayList<>();

    public VisszaadasEllenorzo(Bankjegy bedobottBankjegy, List<Bankjegy> banjegyek, Integer vegosszeg) {
        this.bedobottBankjegy = bedobottBankjegy;
        this.banjegyek = banjegyek.stream()
                .sorted((o1, o2) -> o2.getErtek() - o1.getErtek())
                .toList();
        ;
        this.vegosszeg = vegosszeg;
    }

    private boolean VisszaadasVegigszamolas() {
        if (bedobottBankjegy.getErtek() - vegosszeg < 0){
            return false;
        }
        Integer kulonbozet = bedobottBankjegy.getErtek() - vegosszeg;
        Map<Integer, Integer> visszajaroMap = new HashMap<>();
        for (Bankjegy bankjegy : banjegyek) {
            while (kulonbozet >= bankjegy.getErtek() && bankjegy.getMennyiseg() > 0) {
                bankjegy.setMennyiseg(bankjegy.getMennyiseg() - 1);
                kulonbozet -= bankjegy.getErtek();
                bankjegy.setMennyiseg(bankjegy.getMennyiseg() - 1);
                bankjegyekMentese(visszajaroMap, bankjegy.getErtek());
            }
        }
        if (kulonbozet.equals(0)){
            mapToList(visszajaroMap);
            return true;
        }else{
            return false;
        }
    }

    private void bankjegyekMentese(Map<Integer, Integer> visszajaroMap, Integer mentendoBankjegyErtek){
        Integer cimletDb = visszajaroMap.getOrDefault(mentendoBankjegyErtek, 0);
        visszajaroMap.put(mentendoBankjegyErtek, cimletDb + 1);
    }

    private List<Bankjegy> mapToList(Map<Integer, Integer> visszajaroMap){
        List<Bankjegy> visszajaroList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> bankjegy: visszajaroMap.entrySet()) {
            visszajaroList.add(Bankjegy.builder()
                            .ertek(bankjegy.getKey())
                            .mennyiseg(bankjegy.getValue())
                    .build());
        }
        return visszajaroList;
    }

    public List<Bankjegy> getVisszajaroLista() {
        return visszajaroLista;
    }
}
