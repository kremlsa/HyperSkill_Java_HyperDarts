package dartsgame.cfg;


import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private String ivanHoe = "ivanhoe@acme.com";
    private String passwordIH = "oMoa3VvqnLxW";
    private String robinHood = "robinhood@acme.com";
    private String passwordRH = "ai0y9bMvyF6G";
    private String wilhelmTell = "wilhelmtell@acme.com";
    private String passwordWT = "bv0y9bMvyF7E";
    private final String admin = "admin@acme.com";
    private String passwordAdmin = "zy0y3bMvyA6T";

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(ivanHoe)
                .password("{noop}" + passwordIH)
                .roles("GAMER")
                .and()
                .withUser(robinHood)
                .password("{noop}" + passwordRH)
                .roles("GAMER")
                .and()
                .withUser(wilhelmTell)
                .password("{noop}" + passwordWT)
                .roles("GAMER")
                .and()
                .withUser(admin)
                .password("{noop}" + passwordAdmin)
                .roles("ADMIN")
                .and()
                .withUser("judgedredd@acme.com")
                .password("{noop}" + "iAmALaw100500")
                .roles("REFEREE");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}


