package com.javaguides.clothesbabies.config;

import com.javaguides.clothesbabies.security.LoginSuccessHandler;
import com.javaguides.clothesbabies.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.session.*;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserServiceImpl customUserDetailsService;

    @Autowired
    private LoginSuccessHandler successHandler;

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(this.customUserDetailsService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity.ignoring().antMatchers("/images/**", "/css/**", "/js/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/registration**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(this.successHandler)
                .failureUrl("/login?error=true")
                .permitAll()
                .and()
                .sessionManagement(session -> session
                        .sessionAuthenticationStrategy(concurrentSession())
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .maximumSessions(1) //set maximum session (only 1 user login in app)
                        .expiredSessionStrategy(sessionInformationExpiredStrategy())
                        .maxSessionsPreventsLogin(false)//second login will cause the first to be invalidated
                )
                //Delete cookies and session invalid when logout
                .logout(logout -> logout
                        .deleteCookies("SESSION")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout").permitAll())
                .exceptionHandling().accessDeniedPage("/403")
                .and()
                .rememberMe().tokenRepository(persistentTokenRepository()).tokenValiditySeconds(7 * 24 * 60 * 60);
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Bean
    public CompositeSessionAuthenticationStrategy concurrentSession() {
        ConcurrentSessionControlAuthenticationStrategy concurrentAuthenticationStrategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
        List<SessionAuthenticationStrategy> delegateStrategies = new ArrayList<SessionAuthenticationStrategy>();
        delegateStrategies.add(concurrentAuthenticationStrategy);
        delegateStrategies.add(new SessionFixationProtectionStrategy());
        delegateStrategies.add(new RegisterSessionAuthenticationStrategy(sessionRegistry()));
        return new CompositeSessionAuthenticationStrategy(delegateStrategies);
    }

    @Bean
    SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
        return new CustomSessionInformationExpiredStrategy("/login?expired=true");
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
