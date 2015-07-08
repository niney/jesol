package grails.plugin.springsecurity.rest

import org.apache.commons.logging.LogFactory
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

/**
 * Created by SangIL on 2015-01-26.
 */
public class PushAuthenticationProvider implements AuthenticationProvider {

    def dataSource

    private static final logger = LogFactory.getLog(this)

    @Override
    Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName()
        String password = authentication.getCredentials().toString()

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
        RestUserDetails pushUserDetails = new RestUserDetails(name, password, grantedAuthorities)
        Authentication auth = new UsernamePasswordAuthenticationToken(pushUserDetails, null, grantedAuthorities)
        return auth
    }
    @Override
    public boolean supports(Class<? extends Object> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}