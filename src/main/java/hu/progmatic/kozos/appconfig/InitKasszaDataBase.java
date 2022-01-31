package hu.progmatic.kozos.appconfig;

import hu.progmatic.kozos.kassza.Termek;
import hu.progmatic.kozos.kassza.TermekSzerviz;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;


@Component
@Transactional
public class InitKasszaDataBase implements InitializingBean {

    private TermekSzerviz termekSzerviz;

    public InitKasszaDataBase(TermekSzerviz termekSzerviz) {
        this.termekSzerviz = termekSzerviz;
    }

    @Override
    public void afterPropertiesSet() {
        if (termekSzerviz.findAll().isEmpty()) {
            termekSzerviz.saveAll(
                    List.of(
                            Termek.builder().ar(100).megnevezes("víz").vonalkod("12345").build(),
                            Termek.builder().ar(200).megnevezes("kóla").vonalkod("12335").build(),
                            Termek.builder().ar(300).megnevezes("sör").vonalkod("1232345").build(),
                            Termek.builder().ar(250).megnevezes("kenyér").vonalkod("12344345").build(),
                            Termek.builder().ar(100).megnevezes("Joghurt").vonalkod("12134235").build(),
                            Termek.builder().ar(300).megnevezes("fanta").vonalkod("23212345").build(),
                            Termek.builder().ar(120).megnevezes("zsemle").vonalkod("16752345").build(),
                            Termek.builder().ar(1300).megnevezes("bor").vonalkod("123226345").build(),
                            Termek.builder().ar(11320).megnevezes("vodka").vonalkod("121134354345").build(),
                            Termek.builder().ar(400).megnevezes("narancs").vonalkod("113344512345").build(),
                            Termek.builder().ar(500).megnevezes("tüske").vonalkod("1234133425").build(),
                            Termek.builder().ar(1000).megnevezes("cigaretta").vonalkod("1221421345").build()
                    )
            );
        }
    }
}
