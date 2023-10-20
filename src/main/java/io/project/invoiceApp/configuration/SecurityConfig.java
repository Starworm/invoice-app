package io.project.invoiceApp.configuration;

import io.project.invoiceApp.handler.CustomAccessDeniedHandler;
import io.project.invoiceApp.handler.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final BCryptPasswordEncoder encoder;
    private final CustomAccessDeniedHandler handler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final UserDetailsService userDetailsService;

    private static String[] PUBLIC_URLS = {"/user/login"};

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//
//        // disabling cross-site request forgery
//        http.csrf().disable().cors().disable();
//        // define session management
//        // we don't use cookies
//        http.sessionManagement().sessionCreationPolicy(STATELESS);
//        // free access for all to these urls
//        http.authorizeHttpRequests().requestMatchers(PUBLIC_URLS).permitAll();
//        // should have permissions for these requests
//        http.authorizeHttpRequests().requestMatchers(DELETE, "/user/delete/**").hasAuthority("DELETE:USER");
//        http.authorizeHttpRequests().requestMatchers(DELETE, "/customer/delete/**").hasAuthority("DELETE:CUSTOMER");
//        // failed authentication
//        http.exceptionHandling().accessDeniedHandler(handler).authenticationEntryPoint(customAuthenticationEntryPoint);
//        // any other requests should be authenticated
//        http.authorizeHttpRequests().anyRequest().authenticated();
//
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        // disabling cross-site request forgery
        http

                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                        .authorizeHttpRequests(auth -> {
                            auth
                                    .requestMatchers(PUBLIC_URLS).permitAll()
                                    .anyRequest().authenticated();
//                                    .requestMatchers(DELETE, "/user/delete/**").hasAuthority("DELETE:USER")
//                                    .requestMatchers(DELETE, "/customer/delete/**").hasAuthority("DELETE:CUSTOMER");
                        })
//                .sessionManagement(sess -> sess.sessionCreationPolicy(STATELESS))
                .exceptionHandling(handler -> handler.authenticationEntryPoint(customAuthenticationEntryPoint));


//        http.csrf().disable().cors().disable();
//         define session management
//         we don't use cookies
//        http.sessionManagement().sessionCreationPolicy(STATELESS);
//         free access for all to these urls
//        http.authorizeHttpRequests().requestMatchers(PUBLIC_URLS).permitAll();
//         should have permissions for these requests
//        http.authorizeHttpRequests().requestMatchers(DELETE, "/user/delete/**").hasAuthority("DELETE:USER");
//        http.authorizeHttpRequests().requestMatchers(DELETE, "/customer/delete/**").hasAuthority("DELETE:CUSTOMER");
//         failed authentication
//        http.exceptionHandling().accessDeniedHandler(handler).authenticationEntryPoint(customAuthenticationEntryPoint);
//         any other requests should be authenticated
//        http.authorizeHttpRequests().anyRequest().authenticated();

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        // create an Auth provider
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(encoder);

        return new ProviderManager(daoAuthenticationProvider);
    }
}
