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


    @GetMapping("/kassza/bankkartyaUrlap/{kosarId}")
    public String kosarIdIndex(@PathVariable("kosarId") Integer kosarId,
                                Model model) {
        model.addAttribute("kosar", kosarService.getKosarViewDTOById(kosarId));
        return "kassza/bankkartyaUrlap";
    }

    @GetMapping("/kassza/visszaigazolas")
    public String keszpenzesfizetes() {
        return "kassza/visszaigazolas";
    }
}
