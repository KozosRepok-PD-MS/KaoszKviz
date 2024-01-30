
package hu.kaoszkviz.server.api.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.kaoszkviz.server.api.jsonview.JsonViewEnum;
import java.util.List;


public class Converter {
    private static final ObjectMapper MAP = new ObjectMapper();
    private static final JsonViewEnum DEFAULT_VIEW = JsonViewEnum.PUBLIC_VIEW;
    
    public static <T> String ModelTableToJsonString(List<T> list) throws JsonProcessingException {
        return Converter.ModelTableToJsonString(list.toArray());
    }
    
    public static <T> String ModelTableToJsonString(T[] array) throws JsonProcessingException {
        return Converter.ModelTableToJsonString(array, Converter.DEFAULT_VIEW);
    }
    
    
    public static <T> String ModelTableToJsonString(List<T> list, JsonViewEnum view) throws JsonProcessingException {
        return Converter.ModelTableToJsonString(list.toArray(), view);
    }
   
    public static <T> String ModelTableToJsonString(T[] array, JsonViewEnum view) throws JsonProcessingException {
        return MAP.writerWithView(view.viewClass.getClass()).writeValueAsString(array);
    }
    
    
    public static <T> String ModelToJsonString(T model) throws JsonProcessingException {
        return Converter.ModelToJsonString(model, Converter.DEFAULT_VIEW);
    }
    
    public static <T> String ModelToJsonString(T model, JsonViewEnum view) throws JsonProcessingException {
        return MAP.writerWithView(view.viewClass.getClass()).writeValueAsString(model);
    }
}
