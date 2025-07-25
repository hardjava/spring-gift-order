package gift.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KakaoOauthConfig {
    @Value("${spring.client.id}")
    private String clientId;

    @Value("${spring.redirect.uri}")
    private String redirectURI;

    public String getClientId() {
        return clientId;
    }

    public String getRedirectURI() {
        return redirectURI;
    }
}
