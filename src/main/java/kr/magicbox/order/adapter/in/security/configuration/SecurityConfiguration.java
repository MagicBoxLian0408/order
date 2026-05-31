package kr.magicbox.order.adapter.in.security.configuration;

import kr.magicbox.order.adapter.in.security.filter.UserInfoExtractFilter;
import kr.magicbox.order.adapter.in.security.properties.TrustedIpProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(TrustedIpProperties.class)
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final TrustedIpProperties trustedIpProperties;


    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new UserInfoExtractFilter(trustedIpProperties), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .build();
    }
}
