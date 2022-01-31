package hu.progmatic.kozos.kassza;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class RaktarController {

    @Autowired
    TermekService termekService;

    @GetMapping("/kassza/raktar")
    public String raktar(){
        return "kassza/raktar";
    }

    @ModelAttribute("allTermek")
    List<Termek> allTermek(){
       return termekService.findAll();
    }


}
