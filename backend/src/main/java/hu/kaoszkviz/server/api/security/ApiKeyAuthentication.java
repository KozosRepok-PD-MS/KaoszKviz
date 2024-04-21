
package hu.kaoszkviz.server.api.security;

import hu.kaoszkviz.server.api.exception.UnauthorizedException;
import hu.kaoszkviz.server.api.model.User;
import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;


public class ApiKeyAuthentication extends AbstractAuthenticationToken {
    private String apiKey;
    private User user;
    
    public ApiKeyAuthentication(User user, String apiKey, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.user = user;
        this.apiKey = apiKey;
        this.setAuthenticated(true);
    }

    /**
     * Visszaadja a kliens által használt API kulcsot.
     * @return {@code String} - api kulcs.
     */
    @Override
    public String getCredentials() {
        return this.apiKey;
    }
    
    /**
     * Visszaadja a kliens felhasználót.
     * @return {@code User} - Felhasználó.
     */
    @Override
    public User getPrincipal() {
        return this.user;
    }
    
    public static ApiKeyAuthentication getAuth() {
        try {
            return (ApiKeyAuthentication) SecurityContextHolder.getContext().getAuthentication();
        } catch(Exception ex) {
            throw new UnauthorizedException();
        }
    }

}
