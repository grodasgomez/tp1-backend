package py.com.progweb.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class IgnoreIdMixin {
  @JsonIgnore abstract Integer getId();
}
