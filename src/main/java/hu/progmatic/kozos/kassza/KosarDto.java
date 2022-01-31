package hu.progmatic.kozos.kassza;

import lombok.Data;

import java.io.Serializable;

@Data
public class KosarDto implements Serializable {
    private final Integer id;
    private final String nev;
}
