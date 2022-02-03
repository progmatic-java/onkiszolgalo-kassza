package hu.progmatic.kozos.kassza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class KosarController {



    /*@Autowired
    TermekMennyisegService termekMennyisegService;

    @Autowired
    TermekService termekService;*/

    @Autowired
    private KosarService kosarService;

    @GetMapping("/kassza/kassza")
    public String termekek(Model model) {

        return termekek();
    }


    private String termekek() {
        return "kassza/kassza";
    }

    @PostMapping("/kassza/{kosarId}/addtermek")
    public String addTermek(
            @PathVariable("kosarId") Integer kosarId,
            @ModelAttribute("termekMennyisegHozzaadasCommand") @Valid TermekMennyisegHozzaadasCommand command,
            BindingResult bindingResult,
            Model model) {
        if (!bindingResult.hasErrors()) {
            try {
                command.setKosarId(kosarId);
                KosarViewDTO kosarViewDto = kosarService.addTermekMennyisegCommand(command);

                model.addAttribute("kosar", kosarViewDto);
            } catch (NincsElegRaktarKeszletException e) {
                //model.addAttribute("kosar", kosarService.getKosarViewDTOById(kosarId));
                bindingResult.addError(new FieldError("termekMennyisegHozzaadasCommand",
                        "mennyiseg",
                        "Nincs elég termék a raktáron!"));
            }
        }
        model.addAttribute("kosar", kosarService.getKosarViewDTOById(kosarId));
        return "kassza/kassza";
    }

   /* @PostMapping("kassza/kassza/termekek")
    public String kosarbanLevoTermekek(@ModelAttribute("allKasszaTermek") TermekMennyiseg termekMennyiseg,
                                       Model model) {
        model.addAttribute(allKosar());
        model.addAttribute(kivalasztottKosar());
        return termekek();
    }*/

    //@ModelAttribute("allKasszaTermek")
    @ModelAttribute("allTermek")
    List<Termek> allKosar() {
        return kosarService.findAllTermekNotNullMennyiseg();
    }

    /*@ModelAttribute("kivalasztottTermek")
    List<TermekMennyisegDto> kivalasztottKosar() {
        return termekMennyisegService.findAllDto();
    }*/

    @ModelAttribute("termekMennyisegHozzaadasCommand")
    TermekMennyisegHozzaadasCommand termekMennyisegHozzaadasCommand() {
        return new TermekMennyisegHozzaadasCommand();
    }

    //@ModelAttribute("allKosarTermek")
    @ModelAttribute("kosar")
    KosarViewDTO kosarViewDTO() {
        return kosarService.kosarViewCreate();
    }

    ;


}

