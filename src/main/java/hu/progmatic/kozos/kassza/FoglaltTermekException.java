package hu.progmatic.kozos.kassza;

import java.util.Map;

public class FoglaltTermekException extends RuntimeException{
    private final Map<String, String> bindingProperty;

    public FoglaltTermekException(Map<String, String> bindingProperty){
        this.bindingProperty = bindingProperty;
    }

    public Map<String, String> getBindingProperty() {
        return bindingProperty;
    }
}
