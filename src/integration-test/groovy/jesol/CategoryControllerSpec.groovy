package jesol

import grails.converters.JSON
import grails.test.mixin.TestFor
import grails.test.mixin.integration.Integration
import grails.transaction.*
import groovyx.net.http.RESTClient

@Integration
@Rollback
@TestFor(CategoryController)
class CategoryIntegrationControllerSpec extends IntegrationBase {

    public static final String USERNAME = 'admin'
    public static final String PASSWORD = '1'

    def setup() {
        authPrepare(USERNAME, PASSWORD)
    }

    def cleanup() {
    }

    void "test 항목 조회"() {
        given:
            RESTClient restClient = new RESTClient(getApiUrl())
        when: "올바른 조회"
            def res = restClient.get(
                    headers: ["x-auth-token": authResult.access_token],
                    path: "categories"
            )
        then: "유효한 응답"
            res.status == 200
            def xResData = JSON.parse(res.headers['x-resource-data'].value)
            // Con.1 totalCount
            xResData.totalCount != null
    }
}
