package hu.progmatic.kozos.kassza;

import net.bytebuddy.implementation.bind.annotation.Super;

public class NincsConnectionToEanServerException extends RuntimeException {
    public NincsConnectionToEanServerException(String message) {
        super(message);
    }
}
