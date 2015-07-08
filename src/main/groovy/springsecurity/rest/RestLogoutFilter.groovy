package springsecurity.rest

import grails.plugin.springsecurity.rest.token.AccessToken
import grails.plugin.springsecurity.rest.token.reader.TokenReader
import grails.plugin.springsecurity.rest.token.storage.TokenNotFoundException
import grails.plugin.springsecurity.rest.token.storage.TokenStorageService
import groovy.util.logging.Slf4j
import org.springframework.web.filter.GenericFilterBean

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Filter exposing an endpoint for deleting tokens. It will read the token from an HTTP header. If found, will delete it
 * from the storage, sending a 200 response. Otherwise, it will send a 404 response.
 */
@Slf4j
class RestLogoutFilter extends GenericFilterBean {

    String logoutUrl = "/api/logout"
    TokenReader tokenReader

    TokenStorageService tokenStorageService

    @Override
    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = request as HttpServletRequest
        HttpServletResponse servletResponse = response as HttpServletResponse

        def actualUri =  servletRequest.requestURI - servletRequest.contextPath

        //Only apply filter to the configured URL
        if (actualUri == logoutUrl) {

            //Only POST is supported
            if (servletRequest.method != 'POST') {
                log.debug "${servletRequest.method} HTTP method is not supported. Setting status to ${HttpServletResponse.SC_METHOD_NOT_ALLOWED}"
                servletResponse.setStatus HttpServletResponse.SC_METHOD_NOT_ALLOWED
                return
            }

            AccessToken accessToken = tokenReader.findToken(servletRequest)

            if (accessToken) {
                log.debug "Token found: ${accessToken.accessToken.mask()}"

                try {
                    log.debug "Trying to remove the token"
                    tokenStorageService.removeToken accessToken.accessToken
                } catch (TokenNotFoundException tnfe) {
                    servletResponse.sendError HttpServletResponse.SC_NOT_FOUND, "Token not found"
                }
            } else {
                log.debug "Token is missing. Sending a 400 Bad Request response"
                servletResponse.sendError HttpServletResponse.SC_BAD_REQUEST, "Token header is missing"
            }

        } else {
            chain.doFilter(request, response)
        }
    }
}
