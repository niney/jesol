package jesol

import grails.plugin.springsecurity.rest.PushAuthenticationProvider
import grails.plugin.springsecurity.rest.RestAuthenticationFilter
import grails.plugin.springsecurity.rest.token.reader.TokenReader
import grails.plugin.springsecurity.rest.token.storage.TokenStorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.LogoutFilter
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.web.filter.GenericFilterBean
import springsecurity.rest.RestAuthenticationEntryPoint
import springsecurity.rest.RestLoginFailureHandler
import springsecurity.rest.RestLoginSuccessHandler
import springsecurity.rest.RestLogoutFilter
import springsecurity.rest.RestTokenValidationFilter

import javax.servlet.Filter

/**
 * Created by Niney on 2015-05-12.
 */
@Configuration
@EnableWebSecurity
@ComponentScan("springsecurity.rest,grails.plugin.springsecurity.rest.token.reader")
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthenticationProvider authenticationProvider;
    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    TokenStorageService tokenStorageService
    @Autowired
    TokenReader tokenReader

    // could we put this in resources.groovy?...
    @Bean(name = "securityConfig")
    ConfigObject securityConfig() {
        return new ConfigSlurper().parse(new ClassPathResource('spring-security-config.groovy').URL)
    }
    @Autowired
    ConfigObject securityConfig

    public RestTokenValidationFilter CreateRestTokenValidationFilter() {
        RestTokenValidationFilter filter = new RestTokenValidationFilter()
        filter.tokenStorageService = this.tokenStorageService
        filter.authenticationSuccessHandler = authenticationSuccessHandler
        filter.authenticationFailureHandler = authenticationFailureHandler
        filter.tokenReader = tokenReader

        filter
    }

    public RestLogoutFilter CreateRestLogoutFilter() {
        RestLogoutFilter filter = new RestLogoutFilter()
        filter.tokenStorageService = this.tokenStorageService
        filter.tokenReader = this.tokenReader
        filter
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .csrf().disable()
                .exceptionHandling()
                    .authenticationEntryPoint(new RestAuthenticationEntryPoint())
            .and()
                .formLogin()
                    .loginPage("/api/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
            .and()
                .logout()
                    .logoutUrl("/api/logout")
                    .logoutSuccessHandler(logoutSuccessHandler)
            .and()
                .authenticationProvider(authenticationProvider)
//                .authorizeRequests().anyRequest().authenticated()
//            .and()
                .addFilterBefore(CreateRestTokenValidationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(CreateRestLogoutFilter(), LogoutFilter.class);

        Map<String, ArrayList<String>> interceptMap = securityConfig.grails.plugin.springsecurity.interceptUrlMap
        interceptMap.each { entry ->
            if (entry.value.any { it == 'permitAll' }) {
                http.authorizeRequests().antMatchers(entry.key).permitAll()
            } else if (entry.value.any { it == 'IS_AUTHENTICATED_FULLY' || it == 'isFullyAuthenticated()' }) {
                http.authorizeRequests().antMatchers(entry.key).fullyAuthenticated().and().httpBasic()
            } else if (entry.value.any { it == 'IS_AUTHENTICATED_REMEMBERED' }) {
                http.authorizeRequests().antMatchers(entry.key).rememberMe().and().httpBasic()
            } else if (entry.value.any { it == 'IS_AUTHENTICATED_ANONYMOUSLY' }) {
                http.authorizeRequests().antMatchers(entry.key).anonymous().and().httpBasic()
            } else if (entry.value.any { it.startsWith('ROLE_') }) {
                List<String> roles = entry.value.findAll { it.startsWith('ROLE_') }
                //Spring Security doesn't need the 'ROLE_'
                roles = roles.collect {it - 'ROLE_'}
                http.authorizeRequests().antMatchers(entry.key).hasAnyRole(roles as String[]).and().httpBasic()
            }
        }

    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("user").password("user").roles("USER")
//    }
}