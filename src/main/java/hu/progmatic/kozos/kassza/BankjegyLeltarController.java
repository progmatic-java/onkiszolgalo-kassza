package hu.progmatic.kozos.kassza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BankjegyLeltarController {
    @Autowired BankjegyService bankjegyService;

    @GetMapping("/kassza/bankjegyleltar")
    public String items(Model model) {
        model.addAttribute("allBankjegy", bankjegyService.findAll());
        return "/kassza/bankjegyleltar";
    }


}
