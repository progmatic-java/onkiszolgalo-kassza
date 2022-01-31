package hu.progmatic.kozos.kassza;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RaktarController {

    @Autowired
    TermekService termekService;

    @GetMapping("/kassza/raktar")
    public String raktar(){
        return "kassza/raktar";
    }
}
