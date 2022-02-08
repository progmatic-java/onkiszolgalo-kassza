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
        model.addAttribute("isTermekMennyisegEdit", false);
        return "kassza/kassza";
    }

    @GetMapping("/kassza/{kosarId}")
    public String kosarIdIndex(@PathVariable("kosarId") Integer kosarId,
                               Model model) {
        model.addAttribute("kosar", kosarService.getKosarViewDTOById(kosarId));
        return "kassza/kassza";
    }

    @PostMapping("/kassza/torles/{kosarId}")
    public String kosarTorles(@PathVariable("kosarId") Integer kosarId){
        kosarService.kosarDeleteById(kosarId);
        return "kassza/kezdes";
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

    @PostMapping("/kassza/{kosarId}/modosit")
    public String modositTermek(
            @PathVariable("kosarId") Integer kosarId,
            @ModelAttribute("termekMennyisegHozzaadasCommand") @Valid TermekMennyisegHozzaadasCommand command,
            BindingResult bindingResult,
            Model model) {
        if (!bindingResult.hasErrors()) {
            try {
                kosarService.termekMennyisegModosit(kosarId, command);
                model.addAttribute("allTermek", kosarService.findAllTermekNotNullMennyiseg());
                model.addAttribute("termekMennyisegHozzaadasCommand", new TermekMennyisegHozzaadasCommand());
            } catch (NincsElegRaktarKeszletException e) {
                model.addAttribute("isTermekMennyisegEdit", true);
                model.addAttribute("allTermek", kosarService.findAllTermek());
                bindingResult.addError(new FieldError("termekMennyisegHozzaadasCommand",
                        "mennyiseg",
                        e.getMessage()));
            }
        } else {
            model.addAttribute("isTermekMennyisegEdit", true);
            model.addAttribute("allTermek", kosarService.findAllTermek());
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
        model.addAttribute("allTermek", kosarService.findAllTermekNotNullMennyiseg());
        return "kassza/kassza";
    }

    @GetMapping("/kassza/{kosarId}/modosit/{termekMId}")
    public String editTermekMennyiseg(
            @PathVariable("kosarId") Integer kosarId,
            @PathVariable("termekMId") Integer termekMId,
            Model model) {
        model.addAttribute("kosar", kosarService.getKosarViewDTOById(kosarId));
        model.addAttribute("termekMennyisegHozzaadasCommand", kosarService.kivalasztottTermekHozzaad(kosarId, termekMId));
        model.addAttribute("isTermekMennyisegEdit", true);
        model.addAttribute("allTermek", kosarService.findAllTermek());
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

    /*@ModelAttribute("isTermekMennyisegEdit")
    boolean isTermekMennyisegEdit(){
        return false;
    }*/
}

