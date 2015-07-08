package jesol

import grails.converters.JSON
import groovyx.net.http.RESTClient
import spock.lang.Specification

/**
 * Created by Niney on 2015-07-06.
 */
abstract class IntegrationBase extends Specification {

    protected static final String API_URL = "http://localhost:8080/"
    protected authResult

    protected authPrepare(username, password) {
        RESTClient restClient = new RESTClient(getApiUrl())
        def res = restClient.post(
                path: "api/login",
                body: [username: username, password: password],
                contentType: groovyx.net.http.ContentType.URLENC
        )
        for(String key : res.responseData.keySet()) {
            authResult = JSON.parse(key)
        }
    }

    def getApiUrl() {
        return API_URL
    }
}
