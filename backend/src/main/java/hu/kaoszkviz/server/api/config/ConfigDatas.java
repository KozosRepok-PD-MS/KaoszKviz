
package hu.kaoszkviz.server.api.config;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import org.springframework.http.HttpMethod;


public class ConfigDatas {
    
    public static final HashMap<HttpMethod, String[]> NON_AUTHENTICATED_ENDPOINTS = new HashMap<>() {{
        put(HttpMethod.GET, new String[] {
            "/",
        });
        put(HttpMethod.POST, new String[] {
            "/user",
            "/user/login",
        });
    }};
    
    public static final TemporalUnit API_KEY_VALIDITY_TYPE = ChronoUnit.WEEKS;
    
    public static final long API_KEY_VALIDITY_DURATION = 1;
    
    
    public static final TemporalUnit PASSWORD_RESET_TOKEN_VALIDITY_TYPE = ChronoUnit.MINUTES;

    public static final long PASSWORD_RESET_TOKEN_VALIDITY_DURATION = 15;
    
    
    public static final String API_KEY_HEADER = "API-KEY";
    
}
