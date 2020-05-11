package pl.tomzwi.tokenauth.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableGlobalMethodSecurity(prePostEnabled = true)
@PropertySource("classpath:security.properties")
public class AppConfig extends WebSecurityConfigurerAdapter {

    @Value( "/${security.endpoint.token.prefix}" )
    private String tokenPrefix;

    @Value( "/${security.endpoint.users.prefix}" )
    private String usersPrefix;

    @Value( "/${security.endpoint.api.prefix}" )
    private String apiPrefix;

    @Value( "${security.default.role}" )
    private String defaultRole;

    @Value( "${security.inactive.role}" )
    private String inactiveRole;

    @Value( "${security.admin.role}" )
    private String adminRole;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers( usersPrefix + "/register" )
                .antMatchers( tokenPrefix + "/token");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(apiPrefix + "/**").hasRole( defaultRole )
                .antMatchers( tokenPrefix + "/current" ).hasAnyRole( inactiveRole, defaultRole )
                .antMatchers(usersPrefix + "/activate").hasRole( inactiveRole )
                .antMatchers( usersPrefix + "/roles" ).hasRole( adminRole )
                .anyRequest().authenticated()
                .and()
                .anonymous().disable()
                .exceptionHandling().authenticationEntryPoint(unathorizedEntryPoint())
                .accessDeniedHandler(accessDeniedHandler())
                .and()
                .formLogin().disable();

        http
                .addFilterBefore(new AuthenticationFilter( authenticationManager() ), BasicAuthenticationFilter.class);

    }

    @Bean
    public AuthenticationEntryPoint unathorizedEntryPoint() {
        return ((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED));
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return ((request, response, accessDeniedException) -> response.sendError(HttpServletResponse.SC_FORBIDDEN));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(tokenAuthenticationProvider());
    }

    @Bean
    public TokenAuthenticationProvider tokenAuthenticationProvider() {
        return new TokenAuthenticationProvider();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

