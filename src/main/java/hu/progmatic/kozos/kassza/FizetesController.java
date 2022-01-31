package hu.progmatic.kozos.kassza;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FizetesController {
    @GetMapping("/kassza/bankkartyaUrlap")
    public String bankartyasfizetes() {
        return "kassza/bankkartyaUrlap";
    }

    @GetMapping("/kassza/visszaigazolas")
    public String keszpenzesfizetes() {
        return "kassza/visszaigazolas";
    }
}
