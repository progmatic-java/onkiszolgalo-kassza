package hu.progmatic.kozos.kassza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FizetesController {
    @Autowired
    KosarService kosarService;


    @GetMapping("/kassza/fizetes/{kosarId}")
    public String toSzamla(@PathVariable("kosarId") Integer kosarId,
                                Model model) {
        model.addAttribute("kosar", kosarService.getKosarViewDTOById(kosarId));
        return "kassza/szamla";
    }

    @GetMapping("/kassza/bankkartyaUrlap/{kosarId}")
    public String toBankkartya(@PathVariable("kosarId") Integer kosarId, Model model){
        model.addAttribute("kosar", kosarService.getKosarViewDTOById(kosarId));
        return "kassza/bankkartyaUrlap";
    }
    @GetMapping("/kassza/szamla/{kosarId}")
    public String backToSzamla(@PathVariable("kosarId") Integer kosarId, Model model){
        model.addAttribute("kosar", kosarService.getKosarViewDTOById(kosarId));
        return "kassza/szamla";
    }

    @GetMapping("/kassza/keszpenzesfizetes/{kosarId}")
    public String toKeszpenzesFizetes(@PathVariable("kosarId") Integer kosarId, Model model){
        model.addAttribute("kosar", kosarService.getKosarViewDTOById(kosarId));
        return "kassza/keszpenzesfizetes";
    }

    @GetMapping("/kassza/befejezes/{kosarId}")
    public String befejezes(@PathVariable("kosarId") Integer kosarId, Model model){
        model.addAttribute("kosar", kosarService.getKosarViewDTOById(kosarId));
        return "kassza/visszaigazolas";
    }


}
