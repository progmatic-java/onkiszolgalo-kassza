package hu.progmatic.kozos.kassza;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BelepesController {

    @RequestMapping("/")
    public String kassza() {
        return "kassza/kezdes";
    }

}
