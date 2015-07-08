package springsecurity.rest

import grails.plugin.springsecurity.rest.token.AccessToken
import grails.plugin.springsecurity.rest.token.reader.TokenReader
import grails.plugin.springsecurity.rest.token.storage.TokenStorageService
import groovy.util.logging.Slf4j
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.util.Assert
import org.springframework.web.filter.GenericFilterBean

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by Niney on 2015-05-20.
 */
@Slf4j
class RestTokenValidationFilter extends GenericFilterBean {

    TokenReader tokenReader
    AuthenticationSuccessHandler authenticationSuccessHandler;
    AuthenticationFailureHandler authenticationFailureHandler;
    TokenStorageService tokenStorageService
    String loginUrl = "/api/login"

    @Override
    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = request as HttpServletRequest
        HttpServletResponse httpResponse = response as HttpServletResponse
        AccessToken accessToken
        def actualUri =  httpRequest.requestURI - httpRequest.contextPath

        try {
            accessToken = tokenReader.findToken(httpRequest)
            if(loginUrl == actualUri) {
                log.debug "Trying Login"
                processFilterChain(request, response, chain, null)
            } else if (accessToken) {
                log.debug "Token found: ${accessToken}"
                log.debug "Trying to authenticate the token"

                accessToken = this.authentication(accessToken) as AccessToken

                if (accessToken.authenticated) {
                    log.debug "Token authenticated. Storing the authentication result in the security context"
                    log.debug "Authentication result: ${accessToken}"

                    if(SecurityContextHolder.context == null ||
                            SecurityContextHolder.context.authentication == null)
                        SecurityContextHolder.context.setAuthentication(accessToken)

                    processFilterChain(request, response, chain, accessToken)

                }

            } else {
                log.debug "Token not found"
                processFilterChain(request, response, chain, null)
            }
        } catch (AuthenticationException ae) {
            log.debug "Authentication failed: ${ae.message}"
            authenticationFailureHandler.onAuthenticationFailure(httpRequest, httpResponse, ae)
        }
    }

    private processFilterChain(ServletRequest request, ServletResponse response, FilterChain chain, AccessToken authenticationResult) {
        if (authenticationResult && authenticationResult.accessToken)
            log.debug "Continuing the filter chain"
        else
            log.debug "Request does not contain any token. Letting it continue through the filter chain"

        chain.doFilter(request, response)
    }

    private Authentication authentication(Authentication authentication) throws AuthenticationException {

        Assert.isInstanceOf(AccessToken, authentication, "Only AccessToken is supported")
        AccessToken authenticationRequest = authentication as AccessToken
        AccessToken authenticationResult = new AccessToken(authenticationRequest.accessToken)

        if (authenticationRequest.accessToken) {
            log.debug "Trying to validate token ${authenticationRequest.accessToken}"
            UserDetails userDetails = tokenStorageService.loadUserByToken(authenticationRequest.accessToken) as UserDetails

            authenticationResult = new AccessToken(userDetails, userDetails.authorities, authenticationRequest.accessToken, null, null)
            log.debug "Authentication result: ${authenticationResult}"
        }

        return authenticationResult
    }

}
