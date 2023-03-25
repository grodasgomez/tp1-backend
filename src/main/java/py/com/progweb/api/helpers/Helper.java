package py.com.progweb.api.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Helper {

  public static <T> T convert(Object object, Class<T> clazz) {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.convertValue(object, clazz);
  }
}
