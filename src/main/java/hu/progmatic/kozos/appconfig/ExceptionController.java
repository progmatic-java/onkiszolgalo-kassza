package hu.progmatic.kozos.appconfig;

import lombok.extern.log4j.Log4j2;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartException;

import java.nio.file.AccessDeniedException;

@Log4j2
@ControllerAdvice
public class ExceptionController {

  @ExceptionHandler(value = MultipartException.class)
  public String handleFileUploadException(MultipartException exception, Model model) {
    setTitle(model, "A megengedettnél nagyobb fájlt próbáltál feltölteni");
    setDetails(model, exception.getMessage());
    log.info("File feltöltés hiba: " + exception.getMessage());
    return "kassza/hibaOldal";
  }

  @ExceptionHandler(value = Exception.class)
  public String handleException(Exception exception, Model model) {

    log.error("Hiba a kérés feldolgozása közben", exception);
    setTitle(model, "A megengedettnél nagyobb fájlt próbáltál feltölteni");
    setDetails(model, exception.getMessage());
    return "kassza/hibaOldal";
  }

  @ExceptionHandler(value = AccessDeniedException.class)
  public String handleAccessDeniedException(AccessDeniedException exception, Model model) {
    log.error("Rossz helyre tévedtél,", exception);
    setTitle(model, "Rossz helyre tévedtél");
    setDetails(model, exception.getMessage());
    return "kassza/hibaOldal";
  }

  private void setDetails(Model model, String details) {
    model.addAttribute("errorPageDetails", details);
  }

  private void setTitle(Model model, String title) {
    model.addAttribute("errorPageTitle", title);
  }
}
