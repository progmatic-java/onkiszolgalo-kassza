package hu.progmatic.kozos.kassza;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class EanService {
    public static String apiToken = "bc03ad58ca5ce4c87d9a407316caa89625566ec31e812f0d1f4d0b46d7c36c5d";

    public static Termek searchForBarcode(String ean) {
        String productName = "unknown";
        try {
            URL url = new URL("https://api.ean-search.org/api?token="
                    + apiToken + "&op=barcode-lookup&ean=" + ean);
            InputStream is = url.openStream();
            int ptr = 0;
            StringBuffer apiResult = new StringBuffer();
            while ((ptr = is.read()) != -1) {
                apiResult.append((char) ptr);
            }
            Pattern p = Pattern.compile("<name>(.*)</name>");
            Matcher m = p.matcher(apiResult);
            if (m.find()) {
                productName = m.group(1);
            }
        } catch (Exception ex) {
            throw new NincsConnectionToEanServerException(ex.getMessage());
        }
        return Termek.builder()
                .vonalkod(ean)
                .mennyiseg(0)
                .ar(0)
                .megnevezes(productName)
                .build();
    }


}
