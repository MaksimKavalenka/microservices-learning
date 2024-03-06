package org.learning.microservices.processor.configuration;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import java.util.Collection;
import java.util.Collections;

import static java.util.Objects.isNull;

@Configuration
public class OAuthFeignConfiguration {

    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    private final ClientRegistrationRepository clientRegistrationRepository;

    public OAuthFeignConfiguration(
            OAuth2AuthorizedClientService oAuth2AuthorizedClientService,
            ClientRegistrationRepository clientRegistrationRepository) {

        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("resource-processor-client");
        OAuthClientCredentialsFeignManager clientCredentialsFeignManager =
                new OAuthClientCredentialsFeignManager(authorizedClientManager(), clientRegistration);
        return requestTemplate -> {
            requestTemplate.header("Authorization",
                    String.format("Bearer %s", clientCredentialsFeignManager.getAccessToken()));
        };
    }

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager() {
        OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, oAuth2AuthorizedClientService);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        return authorizedClientManager;
    }

    @Slf4j
    private static class OAuthClientCredentialsFeignManager {

        private final OAuth2AuthorizedClientManager manager;

        private final Authentication principal;

        private final ClientRegistration clientRegistration;

        public OAuthClientCredentialsFeignManager(
                OAuth2AuthorizedClientManager manager, ClientRegistration clientRegistration) {

            this.manager = manager;
            this.clientRegistration = clientRegistration;
            this.principal = createPrincipal();
        }

        private Authentication createPrincipal() {
            return new Authentication() {
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return Collections.emptySet();
                }

                @Override
                public Object getCredentials() {
                    return null;
                }

                @Override
                public Object getDetails() {
                    return null;
                }

                @Override
                public Object getPrincipal() {
                    return this;
                }

                @Override
                public boolean isAuthenticated() {
                    return false;
                }

                @Override
                public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
                }

                @Override
                public String getName() {
                    return clientRegistration.getClientId();
                }
            };
        }

        public String getAccessToken() {
            try {
                OAuth2AuthorizeRequest oAuth2AuthorizeRequest = OAuth2AuthorizeRequest
                        .withClientRegistrationId(clientRegistration.getRegistrationId())
                        .principal(principal)
                        .build();

                OAuth2AuthorizedClient client = manager.authorize(oAuth2AuthorizeRequest);
                if (isNull(client)) {
                    throw new IllegalStateException(
                            String.format("Client credentials flow on %s failed, client is null",
                                    clientRegistration.getRegistrationId()));
                }

                return client.getAccessToken().getTokenValue();
            } catch (Exception e) {
                log.error("Client credentials error " + e.getMessage());
            }

            return null;
        }

    }

}
