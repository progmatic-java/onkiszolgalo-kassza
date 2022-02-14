package hu.progmatic.kozos.kassza;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class KepfeltoltesCommand {
    @NotNull
    private MultipartFile file;

    Termek termek;
}
