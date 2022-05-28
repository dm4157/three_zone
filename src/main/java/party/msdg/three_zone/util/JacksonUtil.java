package party.msdg.three_zone.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
public class JacksonUtil {

    private static ObjectMapper objectMapper = JacksonMapperFactory.create();

    public static <T> T readValue(String value, Class<T> clazz) {
        try {
            return objectMapper.readValue(value, clazz);
        } catch (IOException e) {
            log.error(e.getClass().getName(), e);
            throw new JacksonException(e);
        }
    }

    public static String writeValueAsString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error(e.getClass().getName(), e);
            throw new JacksonException(e);
        }
    }

    public static <T> List<T> readArray(String value) {
        try {
            return objectMapper.readValue(value, new TypeReference<List<T>>(){});
        } catch (IOException e) {
            log.error(e.getClass().getName(), e);
            throw new JacksonException(e);
        }
    }
}
