
package hu.kaoszkviz.server.api.tools;

import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;


public class HeaderBuilder {
    private static final String DEFAULT_HEADER_KEY = "Access-Control-Expose-Headers";
    private static final String DEFAULT_HEADER_VALUE = "*";
    
    public static HttpHeaders build() {
        return build(HeaderBuilder.DEFAULT_HEADER_KEY, HeaderBuilder.DEFAULT_HEADER_VALUE);
    }
    
    public static HttpHeaders build(String key, String value) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(key, value);
        
        if (!headers.containsKey(HeaderBuilder.DEFAULT_HEADER_KEY)) {
            List<String> settedHeaders = headers.get(HeaderBuilder.DEFAULT_HEADER_KEY);
            if (settedHeaders == null || !settedHeaders.contains(HeaderBuilder.DEFAULT_HEADER_VALUE)) {
                headers.add(HeaderBuilder.DEFAULT_HEADER_KEY, HeaderBuilder.DEFAULT_HEADER_VALUE);
            }
        }
        
        return headers;
    }
    
    public static HttpHeaders build(MultiValueMap<String, String> headers) {
        HttpHeaders headersToSet = new HttpHeaders();
        headersToSet.add(HeaderBuilder.DEFAULT_HEADER_KEY, HeaderBuilder.DEFAULT_HEADER_VALUE);
        headersToSet.addAll(headers);
        
        return headersToSet;
    }
    
}
