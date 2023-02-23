package com.springbootangular.config;

import com.springbootangular.service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final JWTAuthenticationEntryPoint unauthorizedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public UserDetailsService getUserDetailService () { // this interface will load the user's specific data
        return new UserDetailsServiceImpl ( ); // returning the object
    }

    @Bean
    public PasswordEncoder passwordEncoder () {
        return NoOpPasswordEncoder.getInstance ( );
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean ();
    }

    @Override
    protected void configure ( AuthenticationManagerBuilder auth ) throws Exception { // this class will create an authentication manager
        auth.userDetailsService ( this.userDetailsServiceImpl ).passwordEncoder ( passwordEncoder ( ) ); // class used for the authentication implementation
    }

    @Override
    protected void configure ( HttpSecurity http ) throws Exception {
        http.csrf ( ).disable ( ).cors ( ).disable ( ).authorizeRequests ( ).
                antMatchers ( "/generate-token" , "/user/" ).permitAll ( )
                .antMatchers ( HttpMethod.OPTIONS ).permitAll ( )
                .anyRequest ( ).authenticated ( ).and ( ).exceptionHandling ( )
                .authenticationEntryPoint ( unauthorizedHandler )
                .and ( )
                .sessionManagement ( )
                .sessionCreationPolicy ( SessionCreationPolicy.STATELESS );
        http.addFilterBefore ( jwtAuthenticationFilter , UsernamePasswordAuthenticationFilter.class );

    }
}
