package common.controller

import grails.rest.RestfulController
import grails.transaction.Transactional
import grails.web.http.HttpHeaders
import jesol.FieldInfo
import org.grails.core.DefaultGrailsDomainClass
import org.hibernate.criterion.CriteriaSpecification

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK

class JSONRestfulController<T> extends RestfulController<T> {

    static responseFormats = ['json', 'html', 'xml']
    static ROWS_MAX_SIZE = 10

    def sessionFactory

//    static responseFormats = ['json']
    JSONRestfulController(Class<T> resource) {
        super(resource)
    }

    /*
     * paging json totalCount
     */
    def index(Integer max) {
        response.addHeader('x-resource-data', "{ totalCount : " + countResources() + "}")
        super.index(max)
    }

    protected List<String> getAllField(String tableName) {
        if(tableName == null || tableName == '')
            tableName = sessionFactory.getClassMetadata(resource).tableName

        String fields = FieldInfo.getSearchFieldListMap(tableName, 'ALL').fieldName[0]
        if(fields == null)
            return []
        def fieldList = []
        Collections.addAll(fieldList, fields.split(","))
        fieldList
    }

    def searchIndex(Integer max) {
        def allField = getAllField()
        def props = getListRestrictProperties()
        def result = resource.createCriteria().list(
                max: Math.min(max ?: 10, ROWS_MAX_SIZE),
                offset: params.offset ?: 0) {
            if(params.q && params.qt) {
                if(params.qt != 'ALL')
                    eq(params.qt, params.q)
                else if(params.qt == 'ALL') {
                    or {
                        allField.each {
                            like(it, "%${params.q}%")
                        }
                    }
                }
            }
            resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
            projections {
                props.each{
                    property(it,it)
                }
            }
            order(params.sort ?: 'id', params.order ?: "desc")
        }
        response.addHeader('x-resource-data', "{ totalCount : " + result.totalCount + "}")
        respond result, model: [("${resourceName}Count".toString()): result.totalCount]
    }

    /**
     * 다중 행에서 제한할 컬럼을 list 돌려준다 전체는 ''
     * @return List
     */
    protected getListRestrictProperties() {
        String tableName = sessionFactory.getClassMetadata(resource).tableName
        def list = FieldInfo.getSearchFieldListMap(tableName, 'LIST').fieldName
        if(list.size() == 0)
            list = queryForProperties()
        return list
    }

    /**
     * 다중 행에서 컬럼을 제한하여 list 돌려준다
     * @param params
     * @return Map
     */
    @Override
    protected List<T> listAllResources(Map params) {
        def props = getListRestrictProperties()
        if(props == '')
            return super.listAllResources(params)

        resource.createCriteria().list(params) {
            resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
            projections {
                props.each{
                    property(it,it)
                }
            }
        }
    }

    /**
     * 1행에서 제한 할 컬럼을 list 로 돌려준다
     * - oneToMany 제외
     * - version 제외
     * @return List
     */
    protected queryForProperties() {
        def domainCls = new DefaultGrailsDomainClass(resource)
        def properties = domainCls.properties.name
        properties.remove('version')
        domainCls.associations.each {
            if(it.oneToMany)
                properties.remove(it.name)
        }
        properties
    }

    /**
     * 1행에서 컬럼을 제한하여 Map 으로 돌려준다
     * @param id
     * @return Map
     */
    protected queryForRestrictResource(Serializable id) {
        def props = queryForProperties()
        if(props == '')
            return super.queryForResource(id)

        resource.createCriteria().list(params) {
            resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
            eq('id', id.toLong())
            projections {
                props.each{
                    property(it,it)
                }
            }
        }.first()
    }

    /**
     * 1행에서 제한된 컬럼(queryForProperties 함수)에서 따라 결과가 달라진다
     * @return T or Map
     */
    @Override
    def show() {
        if(params.id == 'index') {
            this.index()
            return
        }
        respond queryForRestrictResource(params.id)
    }

    /**
     * resultList 로 DB 조회 하여 결과 값을 돌려준다
     * @param idList id property 가 있는 리스트
     * @return List<Map>
     */
    protected listRestrictResources(idList) {
        def props = getListRestrictProperties()
        resource.createCriteria().list() {
            resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
            'in'('id', idList.id)
            projections {
                props.each{
                    property(it, it)
                }
            }
        }
    }

    /**
     * Saves a resource
     */
    @Override
    @Transactional
    def save() {
        if(handleReadOnly()) {
            return
        }
        def instance = createResource()

        instance.validate()
        if (instance.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond instance.errors, view:'create' // STATUS CODE 422
            return
        }

        def domainCls = new DefaultGrailsDomainClass(resource)
        for(int i = 0; i < domainCls.associations.size(); i++) {
            def association = instance[domainCls.associations[i].name]
            if(association != null && association.id == null) {
                association.validate()
                if (association.hasErrors()) {
                    transactionStatus.setRollbackOnly()
                    respond association.errors, view:'create' // STATUS CODE 422
                    return
                }
                association.save()
            }
        }
        instance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: "${resourceName}.label".toString(), default: resourceClassName), instance.id])
                redirect instance
            }
            '*' {
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link( resource: this.controllerName, action: 'show',id: instance.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
                respond instance, [status: CREATED]
            }
        }

        instance
    }

/**
 * Updates a resource for the given id
 * @param id
 */
    @Override
    @Transactional
    def update() {
        if(handleReadOnly()) {
            return
        }

        T instance = queryForResource(params.id)
        if (instance == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        instance.properties = getObjectToBind()

        if (instance.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond instance.errors, view:'edit' // STATUS CODE 422
            return
        }

        instance.save flush:true
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: "${resourceClassName}.label".toString(), default: resourceClassName), instance.id])
                redirect instance
            }
            '*'{
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link( resource: this.controllerName, action: 'show',id: instance.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
                respond instance, [status: OK]
            }
        }
        instance
    }

    /**
     * Deletes a resource for the given id
     * @param id The id
     */
    @Override
    @Transactional
    def delete() {
        if(handleReadOnly()) {
            return
        }

        def instance = queryForResource(params.id)
        if (instance == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        instance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: "${resourceClassName}.label".toString(), default: resourceClassName), instance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT } // NO CONTENT STATUS CODE
        }

        instance
    }

    protected setHeaderTotalCount(long count) {
        response.addHeader('x-resource-data', "{ totalCount : " + count + "}")
    }
}
