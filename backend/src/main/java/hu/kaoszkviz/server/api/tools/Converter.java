
package hu.kaoszkviz.server.api.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hu.kaoszkviz.server.api.exception.InternalServerErrorException;
import hu.kaoszkviz.server.api.jsonview.JsonViewEnum;
import java.util.List;


public class Converter {
    private static final ObjectMapper MAP = new ObjectMapper()
                                                .registerModule(new JavaTimeModule())
                                                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    private static final JsonViewEnum DEFAULT_VIEW = JsonViewEnum.PUBLIC_VIEW;
    
    public static <T> String ModelTableToJsonString(List<T> list) {
        return Converter.ModelTableToJsonString(list.toArray());
    }
    
    public static <T> String ModelTableToJsonString(T[] array) {
        return Converter.ModelTableToJsonString(array, Converter.DEFAULT_VIEW);
    }
    
    
    public static <T> String ModelTableToJsonString(List<T> list, JsonViewEnum view) {
        return Converter.ModelTableToJsonString(list.toArray(), view);
    }
   
    public static <T> String ModelTableToJsonString(T[] array, JsonViewEnum view) {
        try {
            return MAP.writerWithView(view.viewClass.getClass()).writeValueAsString(array);
        } catch (JsonProcessingException ex) {
            throw new InternalServerErrorException();
        }
    }
    
    
    public static <T> String ModelToJsonString(T model) {
        return Converter.ModelToJsonString(model, Converter.DEFAULT_VIEW);
    }
    
    public static <T> String ModelToJsonString(T model, JsonViewEnum view) {
        try {
            return MAP.writerWithView(view.viewClass.getClass()).writeValueAsString(model);
        } catch (JsonProcessingException ex) {
            throw new InternalServerErrorException();
        }
    }
}
