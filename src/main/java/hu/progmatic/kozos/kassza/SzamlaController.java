package hu.progmatic.kozos.kassza;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SzamlaController {
    @GetMapping("/kassza/szamla")
    public String kassza() {
        return "kassza/szamla";
    }
}
