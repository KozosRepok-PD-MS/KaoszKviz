
package hu.kaoszkviz.server.api.service;

import hu.kaoszkviz.server.api.config.ConfigDatas;
import hu.kaoszkviz.server.api.exception.NotFoundException;
import hu.kaoszkviz.server.api.model.APIKey;
import hu.kaoszkviz.server.api.repository.APIKeyRepository;
import hu.kaoszkviz.server.api.repository.UserRepository;
import hu.kaoszkviz.server.api.security.ApiKeyAuthentication;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private APIKeyRepository aPIKeyRepository;
    
    public Optional<Authentication> getAuthenticationFromRequest(HttpServletRequest request) {
        String providedKey = request.getHeader(ConfigDatas.API_KEY_HEADER);
        if (providedKey == null || providedKey.isBlank()) {
            return Optional.empty();
        }
        //User user = userRepository.getUserByAPIKey(providedKey).orElseThrow(() -> new UnauthorizedException());
        
        APIKey apiKey = this.aPIKeyRepository.findById(UUID.fromString(providedKey)).orElseThrow(() -> new NotFoundException(APIKey.class, ""));
        
        if (apiKey.getExpiresAt().isBefore(LocalDateTime.now()));
        
        return Optional.of(new ApiKeyAuthentication(apiKey.getUser(), providedKey, AuthorityUtils.NO_AUTHORITIES));
    }
    
}

