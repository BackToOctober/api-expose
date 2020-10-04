package vn.com.vtcc.apiExpose.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String parseToString(Object value) throws JsonProcessingException {
        return objectMapper.writeValueAsString(value);
    }
}
