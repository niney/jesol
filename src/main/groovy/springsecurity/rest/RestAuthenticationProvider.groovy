package springsecurity.rest

import grails.plugin.springsecurity.rest.RestUserDetails
import grails.plugin.springsecurity.rest.token.AccessToken
import org.apache.commons.logging.LogFactory
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.util.Assert

/**
 * Created by SangIL on 2015-01-26.
 */
@Component
public class RestAuthenticationProvider implements AuthenticationProvider {

    def dataSource

    private static final logger = LogFactory.getLog(this)

    @Override
    Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName()
        String password = authentication.getCredentials().toString()

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        RestUserDetails restUserDetails = new RestUserDetails(name, password, grantedAuthorities)
        Authentication auth = new UsernamePasswordAuthenticationToken(restUserDetails, null, grantedAuthorities)
        return auth
    }
    @Override
    public boolean supports(Class<? extends Object> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}