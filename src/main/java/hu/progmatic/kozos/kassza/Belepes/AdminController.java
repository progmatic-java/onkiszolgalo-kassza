package hu.progmatic.kozos.kassza.Belepes;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {
    @RequestMapping("/kassza/login")
    public String login() {
        return "kassza";
    }
}
