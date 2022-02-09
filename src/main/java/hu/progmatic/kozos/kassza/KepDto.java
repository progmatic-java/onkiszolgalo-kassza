package hu.progmatic.kozos.kassza;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class KepDto {
    private Integer id;
    private String megnevezes;
    private String meret;
}
