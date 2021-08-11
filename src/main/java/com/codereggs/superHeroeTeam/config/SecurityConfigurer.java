package com.codereggs.superHeroeTeam.config;

import com.codereggs.superHeroeTeam.services.impl.UserDetailsServiceImpl;
import com.codereggs.superHeroeTeam.util.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    private static final String[] PUBLIC_PATHLIST = {
            // -- Swagger UI v3 (OpenAPI)
            "/api/docs/**",
            "/api/swagger-ui/**",
            "/v3/api-docs/**",
            "**/swagger-ui/**",
            // Para agregar otras rutas al whitelist, agregarlas aca.
            "/register",
            "/login",
    };

    private static final String[] USER_PATHLIST = {
            "/users/*",
            "/auth/me",
            "/members/**",
            "/comments/*"
    };

    private static final String[] ADMIN_PATHLIST = {
            "/**"
    };

    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable().authorizeRequests()
                //Acá, RUTAS PUBLICAS. (Cualquier usuario puede acceder a ellas.)
                .antMatchers(PUBLIC_PATHLIST).permitAll()
                .antMatchers(USER_PATHLIST).hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")//Acá, RUTAS SOLO DE ADMINS.
                .antMatchers(ADMIN_PATHLIST).hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().exceptionHandling();
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

}
