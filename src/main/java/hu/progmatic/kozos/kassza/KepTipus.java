package hu.progmatic.kozos.kassza;

import org.springframework.http.MediaType;

public enum KepTipus {
    JPEG(MediaType.IMAGE_JPEG_VALUE);
    public final String mediaType;

    KepTipus(String mediaType) {
        this.mediaType = mediaType;
    }
}
