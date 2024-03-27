
package hu.kaoszkviz.server.api.service;

import hu.kaoszkviz.server.api.config.ConfigDatas;
import hu.kaoszkviz.server.api.model.User;
import hu.kaoszkviz.server.api.repository.UserRepository;
import hu.kaoszkviz.server.api.security.ApiKeyAuthentication;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    
    @Autowired
    private UserRepository userRepository;
    
    public Optional<Authentication> getAuthenticationFromRequest(HttpServletRequest request) {
        String providedKey = request.getHeader(ConfigDatas.API_KEY_HEADER);
        Optional<User> user = userRepository.getUserByAPIKey(providedKey);
        if (providedKey == null || user.isEmpty()) {
            return Optional.empty();
        }
        
        return Optional.of(new ApiKeyAuthentication(user.get(), providedKey, AuthorityUtils.NO_AUTHORITIES));
    }
    
}

