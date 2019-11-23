package com.hali.config;

import com.hali.config.firebase.FirebaseAuthenticationProvider;
import com.hali.config.firebase.FirebaseBasicAuthenticationFilter;
import com.hali.config.firebase.FirebaseFilter;
import com.hali.config.firebase.FirebaseResources;
import com.hali.security.*;
import com.hali.security.jwt.*;

import com.hali.service.FirebaseService;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import javax.annotation.PostConstruct;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;
    private final CorsFilter corsFilter;
    private final SecurityProblemSupport problemSupport;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private FirebaseAuthenticationProvider authenticationProvider;
    public SecurityConfiguration(TokenProvider tokenProvider, UserDetailsService userDetailsService, CorsFilter corsFilter, SecurityProblemSupport problemSupport,
                                 AuthenticationManagerBuilder authenticationManagerBuilder, FirebaseAuthenticationProvider provider) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
        this.corsFilter = corsFilter;
        this.problemSupport = problemSupport;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.authenticationProvider=provider;
    }

    @PostConstruct
    public void init() {
        try {
            authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
            if(FirebaseResources.IS_FIREBASE_ENABLED){
                authenticationManagerBuilder.authenticationProvider(authenticationProvider);
            }
        } catch (Exception e) {
            throw new BeanInitializationException("Security configuration failed", e);
        }

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
            .antMatchers(HttpMethod.OPTIONS, "/**")
            .antMatchers("/app/**/*.{js,html}")
            .antMatchers("/i18n/**")
            .antMatchers("/content/**")
            .antMatchers("/swagger-ui/index.html")
            .antMatchers("/test/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
//        http
//            .csrf()
//            .disable()
//            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
//            .exceptionHandling()
//            .authenticationEntryPoint(problemSupport)
//            .accessDeniedHandler(problemSupport)
//        .and()
//            .headers()
//            .contentSecurityPolicy("default-src 'self'; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com; style-src 'self' https://fonts.googleapis.com 'unsafe-inline'; img-src 'self' data:; font-src 'self' https://fonts.gstatic.com data:")
//        .and()
//            .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
//        .and()
//            .featurePolicy("geolocation 'none'; midi 'none'; sync-xhr 'none'; microphone 'none'; camera 'none'; magnetometer 'none'; gyroscope 'none'; speaker 'none'; fullscreen 'self'; payment 'none'")
//        .and()
//            .frameOptions()
//            .deny()
//        .and()
//            .sessionManagement()
//            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//        .and()
//            .authorizeRequests()
//            .antMatchers("/api/authenticate").permitAll()
//            .antMatchers("/api/register").permitAll()
//            .antMatchers("/api/activate").permitAll()
//            .antMatchers("/api/account/reset-password/init").permitAll()
//            .antMatchers("/api/account/reset-password/finish").permitAll()
//            .antMatchers("/api/**").authenticated()
//            .antMatchers("/management/health").permitAll()
//            .antMatchers("/management/info").permitAll()
//            .antMatchers("/management/prometheus").permitAll()
//            .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
//        .and()
//            .httpBasic()
//        .and()
//            .apply(securityConfigurerAdapter());

        if(FirebaseResources.IS_FIREBASE_ENABLED){
            final FirebaseBasicAuthenticationFilter firebaseBasicAuthenticationFilter=new FirebaseBasicAuthenticationFilter(this.authenticationManager(),tokenProvider);
            http
                .addFilterBefore(tokenAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilter(firebaseBasicAuthenticationFilter)
                .exceptionHandling()
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport)
                .and()
                .csrf().disable()
                .headers()
                .frameOptions().disable()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/register").permitAll()
                .antMatchers("/api/activate").permitAll()
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/account/reset-password/init").permitAll()
                .antMatchers("/api/account/reset-password/finish").permitAll()
                .antMatchers("/api/profile-info").permitAll()
                .antMatchers("/api/**").authenticated()
                .antMatchers("/management/health").permitAll()
                .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/v2/api-docs/**").permitAll()
                .antMatchers("/swagger-resources/configuration/ui").permitAll()
                .antMatchers("/swagger-ui/index.html").hasAuthority(AuthoritiesConstants.ADMIN)
                .and()
                .apply(securityConfigurerAdapter());
        }else{
            http
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport)
                .and()
                .csrf()
                .disable()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/register").permitAll()
                .antMatchers("/api/activate").permitAll()
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/account/reset-password/init").permitAll()
                .antMatchers("/api/account/reset-password/finish").permitAll()
                .antMatchers("/api/profile-info").permitAll()
                .antMatchers("/api/**").authenticated()
                .antMatchers("/management/health").permitAll()
                .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/v2/api-docs/**").permitAll()
                .antMatchers("/swagger-resources/configuration/ui").permitAll()
                .antMatchers("/swagger-ui/index.html").hasAuthority(AuthoritiesConstants.ADMIN)
                .and()
                .apply(securityConfigurerAdapter());
        }
        // @formatter:on
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }

    @Autowired(required = false)
    private FirebaseService firebaseService;

    private FirebaseFilter tokenAuthorizationFilter() {
        return new FirebaseFilter(firebaseService);
    }

    private FirebaseBasicAuthenticationFilter firebaseBasicAuthenticationFilter(){
        try {
            return new FirebaseBasicAuthenticationFilter(this.authenticationManager(),tokenProvider);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
