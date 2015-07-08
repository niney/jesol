package grails.plugin.springsecurity.rest

import org.springframework.security.core.GrantedAuthority

class RestUserDetails extends GrailsUser  {
	RestUserDetails(String username, String password, Collection<GrantedAuthority> authorities) {
		super(username, password, true, true, true, true, authorities, null)
	}
}
