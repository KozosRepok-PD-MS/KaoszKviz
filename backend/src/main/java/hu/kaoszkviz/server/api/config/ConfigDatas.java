
package hu.kaoszkviz.server.api.config;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import org.springframework.http.HttpMethod;


public class ConfigDatas {
    
    public static final HashMap<HttpMethod, String[]> NON_AUTHENTICATED_ENDPOINTS = new HashMap<>() {{
        put(HttpMethod.GET, new String[] {
            "/",
            "/media/**",
        });
        put(HttpMethod.POST, new String[] {
            "/user",
            "/user/login",
            "/user/resetpassword",
            "/user/changepassword",
        });
    }};
    
    public static final TemporalUnit API_KEY_VALIDITY_TYPE = ChronoUnit.WEEKS;
    
    public static final long API_KEY_VALIDITY_DURATION = 1;
    
    
    public static final TemporalUnit PASSWORD_RESET_TOKEN_VALIDITY_TYPE = ChronoUnit.MINUTES;

    public static final long PASSWORD_RESET_TOKEN_VALIDITY_DURATION = 15;
    
    public static final String PASSWORD_RESET_TOKEN_HEADER = "RESET-TOKEN";
    
    
    public static final String API_KEY_HEADER = "API-KEY";
    
    public static final String PAIRING_QUESTION_TYPE = "pairing";
    
    public static final int MAX_ANSWER_FOR_QUESTION = 6;
    
    public static final byte MAX_TIME_TO_ANSWER_IN_QUESTION = 90;
    public static final byte DEFAULT_TIME_TO_ANSWER_IN_QUESTION = 10;
    
}
