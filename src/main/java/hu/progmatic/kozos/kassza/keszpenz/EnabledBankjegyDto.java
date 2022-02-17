package hu.progmatic.kozos.kassza.keszpenz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnabledBankjegyDto {
    private Integer bankjegyErtek;
    private boolean engedelyezve;
}
