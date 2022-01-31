package hu.progmatic.kozos.kassza;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class RaktarController {

    @Autowired
    TermekService termekService;

    @GetMapping("/kassza/raktar")
    public String raktar(){
        return "kassza/raktar";
    }

    @PostMapping("/kassza/addTermek")
    public String addTermek(
            @ModelAttribute("termekHozzaadasa") @Valid Termek termek,
            BindingResult bindingResult,
            Model model) {
        if (!bindingResult.hasErrors()) {
                termekService.addTermek(
                        Termek.builder()
                                .mennyiseg(termek.getMennyiseg())
                                .ar(termek.getAr())
                                .megnevezes(termek.getMegnevezes())
                                .vonalkod(termek.getVonalkod())
                        .build());
                model.addAttribute("termekHozzaadasa",termek);
        return "kassza/raktar";
        }
        return "kassza/raktar";
    }

    @ModelAttribute("allTermek")
    List<Termek> allTermek(){
       return termekService.findAll();
    }

    @ModelAttribute("termekHozzaadasa")
    void addtermek(){
        termekService.addTermek(Termek.builder().build());
    }


}
