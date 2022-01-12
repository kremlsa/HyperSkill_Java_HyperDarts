package dartsgame.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Value("${jwt.verifying-key}")
    String publicKey;

    private TokenStore tokenStore;

    @Bean
    public TokenStore tokenStore() {
        if (tokenStore == null) {
            tokenStore = new JwtTokenStore(jwtAccessTokenConverter());
        }
        return tokenStore;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId("api");
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/history/**")
                .access("(#oauth2.hasScope('read') and hasAnyRole('ROLE_REFEREE', 'ROLE_GAMER'))")
                .antMatchers(HttpMethod.PUT, "/**")
                .access("(#oauth2.hasScope('update') and hasRole('ROLE_REFEREE'))")
                .antMatchers(HttpMethod.GET, "/**")
                .access("(#oauth2.hasScope('read') and hasRole('ROLE_GAMER'))")
                .antMatchers(HttpMethod.POST, "/**")
                .access("(#oauth2.hasScope('write') and hasRole('ROLE_GAMER'))")
//                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable();
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setVerifierKey(publicKey);
        return converter;
    }

}

