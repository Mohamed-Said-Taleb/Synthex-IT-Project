package com.devsling.fr.security;


import com.devsling.fr.security.filters.AuthorizationFilter;
import com.devsling.fr.security.oauth2.CustomOAuth2UserService;
import com.devsling.fr.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.devsling.fr.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.devsling.fr.security.oauth2.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;

    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final AuthorizationFilter authorizationFilter;
    private final MyUserDetailsService userDetailsService;
    private final AuthEntryPoint authEntryPoint;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
            return httpSecurity
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(HttpMethod.POST,"/auth/register").permitAll()
                            .requestMatchers(HttpMethod.POST,"/auth/login").permitAll()
                            .requestMatchers(HttpMethod.POST,"/auth/validate-token").permitAll()
                            .requestMatchers(HttpMethod.POST,"/auth/password-request").permitAll()
                            .requestMatchers(HttpMethod.POST,"/auth/reset-password").permitAll()
                            .requestMatchers(HttpMethod.GET,"/auth/activate-account").permitAll()
                            .requestMatchers(
                                    "/",
                                    "/error",
                                    "/favicon.ico"
                            ).permitAll()
                            .anyRequest().authenticated()
                    )
                    .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .formLogin(AbstractHttpConfigurer::disable)
                    .logout(AbstractHttpConfigurer::disable)
                    .httpBasic(AbstractHttpConfigurer::disable)
                  /**  .oauth2Login(oauth2 -> oauth2
                            .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint
                                    .baseUri("/oauth2/authorize")
                                    .authorizationRequestRepository(cookieAuthorizationRequestRepository()))
                            .redirectionEndpoint(redirectionEndpoint -> redirectionEndpoint
                                    .baseUri("/oauth2/callback/*"))
                            .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                    .userService(customOAuth2UserService))
                            .successHandler(oAuth2AuthenticationSuccessHandler)
                            .failureHandler(oAuth2AuthenticationFailureHandler))**/
                    .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
        }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    @Primary
    public AuthenticationManagerBuilder configureAuthenticationManagerBuilder(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(getBCE());
        return authenticationManagerBuilder;
    }
    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }
    @Bean
    BCryptPasswordEncoder getBCE() {
        return new BCryptPasswordEncoder();
    }
}
