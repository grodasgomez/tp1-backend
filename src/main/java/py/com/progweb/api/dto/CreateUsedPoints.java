package py.com.progweb.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUsedPoints {
    private Integer clientId;
	private Integer conceptPointUseId;
}
