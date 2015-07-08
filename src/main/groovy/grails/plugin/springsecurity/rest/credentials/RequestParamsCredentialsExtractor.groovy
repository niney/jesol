/*
 * Copyright 2013-2015 Alvaro Sanchez-Mariscal <alvaro.sanchezmariscal@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package grails.plugin.springsecurity.rest.credentials

import groovy.util.logging.Slf4j
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

import javax.servlet.http.HttpServletRequest

/**
 * Extracts credentials from request parameters
 */
@Slf4j
class RequestParamsCredentialsExtractor implements CredentialsExtractor {

    String usernamePropertyName = "username"
    String passwordPropertyName = "password"

    UsernamePasswordAuthenticationToken extractCredentials(HttpServletRequest httpServletRequest) {
        String username = httpServletRequest.getParameter(usernamePropertyName)
        String password = httpServletRequest.getParameter(passwordPropertyName)

        log.debug "Extracted credentials from request params. Username: ${username}, password: ${password?.size()?'[PROTECTED]':'[MISSING]'}"

        new UsernamePasswordAuthenticationToken(username, password)
    }

}