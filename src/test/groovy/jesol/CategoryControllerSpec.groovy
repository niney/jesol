package jesol

import grails.converters.JSON
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.hibernate.SessionFactory
import spock.lang.*

@TestFor(CategoryController)
@Mock(Category)
class CategoryControllerSpec extends Specification {

    def populateValidParams() {
        [title: 'grails']
    }

    void "test 빈값으로 잘못된 요청"() {
        when:"잘못된 엔티티 저장"
            request.contentType = JSON_CONTENT_TYPE
            request.method = 'POST'
            controller.request.json = []
            controller.save()

        then:"처리할 수 없는 엔티티 응답"
            response.status == 422
    }

    void "test 저장"() {

        when:"유효한 엔티티 저장"
            request.contentType = JSON_CONTENT_TYPE
            request.method = 'POST'
            controller.request.json = populateValidParams()
            controller.save()

        then:"유효한 응답"
            response.status == 201
            Category.count == 1
    }



}