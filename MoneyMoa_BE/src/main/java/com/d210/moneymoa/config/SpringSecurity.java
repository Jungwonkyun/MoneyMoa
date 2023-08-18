package com.d210.moneymoa.config;

import com.d210.moneymoa.domain.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import javax.servlet.http.HttpServletRequestWrapper;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;


import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SpringSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

//.frameOptions().deny().and()

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .headers()
                .frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/swagger-ui/**", "/v2/api-docs/**", "/webjars/**", "/swagger-resources/**", "/api/feed/search").permitAll()
                .antMatchers( "/api/auth/**", "/api/member/login", "/api/member/signup", "/api/member/findpassword",
                        "/api/member/emailauth", "/api/file/**", "/api/deposit/**", "/api/saving/**","/api/cma/**", "/api/interestdetail/**","/api/ws-stomp","/pub/**", "/sub/**",
                        "/api/chat/**", "/api/ws-stomp/**", "/api/wiki/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().hasAnyRole("MEMBER", "ADMIN")
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class).requestMatchers()
                .and()
                .logout()
                .permitAll();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();

        // Set the allowed origins
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://127.0.0.1:5173", "i9d210.p.ssafy.io","https://i9d210.p.ssafy.io"));

        // Set the allowed methods
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        
        // Allow credentials
        configuration.setAllowCredentials(true);

        // Set the allowed headers
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Expose the Authorization and RefreshToken headers
        configuration.setExposedHeaders(Arrays.asList("Authorization", "RefreshToken"));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
