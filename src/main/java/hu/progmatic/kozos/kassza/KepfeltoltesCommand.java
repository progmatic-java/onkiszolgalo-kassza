package hu.progmatic.kozos.kassza;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@Data
public class KepfeltoltesCommand {
    @NotEmpty
    @NotNull
    private String nev;
    @NotNull
    private MultipartFile file;
}
