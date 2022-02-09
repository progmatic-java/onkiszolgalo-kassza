package hu.progmatic.kozos.kassza;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class KepMegjelenitesDto {

    private byte[] kepAdat;
    private String contentType;
}
