package springsecurity.rest

import grails.plugin.springsecurity.rest.token.AccessToken
import grails.plugin.springsecurity.rest.token.generation.SecureRandomTokenGenerator
import grails.plugin.springsecurity.rest.token.generation.TokenGenerator
import grails.plugin.springsecurity.rest.token.rendering.AccessTokenJsonRenderer
import grails.plugin.springsecurity.rest.token.rendering.DefaultAccessTokenJsonRenderer
import grails.plugin.springsecurity.rest.token.storage.TokenStorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import org.springframework.security.web.savedrequest.RequestCache
import org.springframework.security.web.savedrequest.SavedRequest
import org.springframework.stereotype.Component
import org.springframework.util.Assert
import org.springframework.util.StringUtils

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by Niney on 2015-05-20.
 */
@Component
@ComponentScan("grails.plugin.springsecurity.rest.token.storage")
class RestLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    TokenGenerator tokenGenerator = new SecureRandomTokenGenerator()
    AccessTokenJsonRenderer renderer = new DefaultAccessTokenJsonRenderer()

    @Autowired
    TokenStorageService tokenStorageService

    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
        AccessToken accessToken = tokenCreateAndSave(authentication)

        response.contentType = 'application/json'
        response.characterEncoding = 'UTF-8'
        response.addHeader 'Cache-Control', 'no-store'
        response.addHeader 'Pragma', 'no-cache'
        response << renderer.generateJson(accessToken)
    }

    private AccessToken tokenCreateAndSave(Authentication authentication) {
        AccessToken accessToken = tokenGenerator.generateAccessToken(authentication.principal as UserDetails)
        logger.debug "Generated token: ${accessToken}"
        tokenStorageService.storeToken(accessToken.accessToken, authentication.principal as UserDetails)
        SecurityContextHolder.context.setAuthentication(accessToken)

        return accessToken
    }

    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest == null) {
            clearAuthenticationAttributes(request);
            return;
        }
        String targetUrlParam = getTargetUrlParameter();
        if (isAlwaysUseDefaultTargetUrl() ||
                (targetUrlParam != null &&
                        StringUtils.hasText(request.getParameter(targetUrlParam)))) {
            requestCache.removeRequest(request, response);
            clearAuthenticationAttributes(request);
            return;
        }
        clearAuthenticationAttributes(request);
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
