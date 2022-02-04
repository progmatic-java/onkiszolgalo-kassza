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


    @Autowired
    private KosarService kosarService;

    @GetMapping("/kassza/kassza")
    public String kosarIndex(Model model) {
        KosarViewDTO kosarViewDTO = kosarService.kosarViewCreate();
        model.addAttribute("kosar", kosarViewDTO);
        return "kassza/kassza";
    }

    @GetMapping("/kassza/{kosarId}")
    public String kosarIdIndex(@PathVariable("kosarId") Integer kosarId,
                               Model model) {
        model.addAttribute("kosar", kosarService.getKosarViewDTOById(kosarId));
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
                kosarService.addTermekMennyisegCommand(kosarId, command);
                model.addAttribute("allTermek", kosarService.findAllTermekNotNullMennyiseg());
                model.addAttribute("termekMennyisegHozzaadasCommand", new TermekMennyisegHozzaadasCommand());
            } catch (NincsElegRaktarKeszletException e) {
                bindingResult.addError(new FieldError("termekMennyisegHozzaadasCommand",
                        "mennyiseg",
                        e.getMessage()));
            }
        }
        model.addAttribute("kosar", kosarService.getKosarViewDTOById(kosarId));
        return "kassza/kassza";
    }

    @PostMapping("/kassza/{kosarId}/delete/{termekMId}")
    public String deleteTermekMennyiseg(
            @PathVariable("kosarId") Integer kosarId,
            @PathVariable("termekMId") Integer termekMId,
            Model model) {
        KosarViewDTO kosarViewDTO = kosarService.deleteTermekMennyiseg(kosarId, termekMId);
        model.addAttribute("kosar", kosarViewDTO);
        return "kassza/kassza";
    }

    @GetMapping("/kassza/{kosarId}/delete/{termekMId}")
    public String editTermekMennyiseg(
            @PathVariable("kosarId") Integer kosarId,
            @PathVariable("termekMId") Integer termekMId,
            Model model) {
        model.addAttribute("kosar", kosarService.getKosarViewDTOById(kosarId));
        model.addAttribute("termekMennyisegHozzaadasCommand", kosarService.kivalasztottTermekHozzaad(kosarId, termekMId));
        return "kassza/kassza";
    }


    @ModelAttribute("allTermek")
    List<Termek> allKosar() {
        return kosarService.findAllTermekNotNullMennyiseg();
    }


    @ModelAttribute("termekMennyisegHozzaadasCommand")
    TermekMennyisegHozzaadasCommand termekMennyisegHozzaadasCommand() {
        return new TermekMennyisegHozzaadasCommand();
    }


}

