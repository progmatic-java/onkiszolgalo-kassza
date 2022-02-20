package hu.progmatic.kozos.kassza.keszpenz;

import hu.progmatic.kozos.kassza.KosarService;
import hu.progmatic.kozos.kassza.KosarViewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FizetesController {
    @Autowired
    KosarService kosarService;

    @Autowired
    KeszpenzService keszpenzService;

    @GetMapping("/kassza/fizetes/{kosarId}")
    public String toSzamla(@PathVariable("kosarId") Integer kosarId,
                           Model model) {
        model.addAttribute("kosar", kosarService.getKosarViewDTOById(kosarId));
        return "kassza/szamla";
    }

    @GetMapping("/kassza/bankkartyaUrlap/{kosarId}")
    public String toBankkartya(@PathVariable("kosarId") Integer kosarId, Model model) {
        model.addAttribute("kosar", kosarService.getKosarViewDTOById(kosarId));
        return "kassza/bankkartyaUrlap";
    }

    @GetMapping("/kassza/szamla/{kosarId}")
    public String backToSzamla(@PathVariable("kosarId") Integer kosarId, Model model) {
        model.addAttribute("kosar", kosarService.getKosarViewDTOById(kosarId));
        return "kassza/szamla";
    }


    @GetMapping("/kassza/keszpenzesfizetes/{kosarId}")
    public String toKeszpenzesFizetes(@PathVariable("kosarId") Integer kosarId, Model model) {
        KeszpenzDto keszpenzDto = KeszpenzDto.builder()
                .kosarId(kosarId)
                .bedobottCimlet(0)
                .build();
        keszpenzDto= keszpenzService.visszajaro(keszpenzDto);
        keszpenzDto.setVisszajaro("10000000000");
        model.addAttribute("kosar", keszpenzDto);
        model.addAttribute("keszpenzDto", keszpenzDto);
        return "kassza/keszpenzesfizetes";
    }


    @PostMapping("/kassza/keszpenzesfizetes/{kosarId}/{osszeg}")
    public String minusFt(@PathVariable("kosarId") Integer kosarId, @PathVariable("osszeg") Integer osszeg, Model model) throws InterruptedException {
        KosarViewDTO kosarViewDTO = kosarService.getKosarDtoById(kosarId);
        KeszpenzDto keszpenzDto = keszpenzService.visszajaro(KeszpenzDto.builder().bedobottCimlet(osszeg).kosarId(kosarId).build());
        model.addAttribute("kosar", kosarViewDTO);
        model.addAttribute("keszpenzDto", keszpenzDto);
        return "kassza/keszpenzesfizetes";
    }



    @GetMapping("/kassza/befejezes/{kosarId}")
    public String befejezes(@PathVariable("kosarId") Integer kosarId) {
        kosarService.deleteKosarById(kosarId);
        return "kassza/visszaigazolas";
    }


}
