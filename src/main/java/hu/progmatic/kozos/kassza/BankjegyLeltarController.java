package hu.progmatic.kozos.kassza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.io.IOException;

@Controller
public class BankjegyLeltarController {
    @Autowired BankjegyService bankjegyService;

    @GetMapping("/kassza/bankjegyleltar")
    public String items(Model model) {
        model.addAttribute("allBankjegy", bankjegyService.findAll());
        model.addAttribute("formItem", Bankjegy.builder().build());
        return "/kassza/bankjegyleltar";
    }

    @GetMapping("/kassza/bankjegyleltar/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Bankjegy bankjegy = bankjegyService.getById(id);
        model.addAttribute("allBankjegy", bankjegyService.findAll());
        model.addAttribute("formItem", bankjegy);
        return "/kassza/bankjegyleltar";
    }


    @PostMapping("/kassza/bankjegyleltar/{id}")
    public String modify(@PathVariable Integer id, @Valid Bankjegy formItem, BindingResult bindingResult, Model model){
        if(!bindingResult.hasErrors()){
            bankjegyService.editById(id,formItem.getMennyiseg());
        }
        model.addAttribute("allBankjegy", bankjegyService.findAll());
        model.addAttribute("formItem", Bankjegy.builder().build());
        return "/kassza/bankjegyleltar";
    }

    @PostMapping("/kassza/bankjegyleltar/clear")
    public String clearBankjegyek(Model model){
        bankjegyService.clear();
        model.addAttribute("allBankjegy", bankjegyService.findAll());
        model.addAttribute("formItem", Bankjegy.builder().build());
        return "kassza/bankjegyleltar";
    }


}
