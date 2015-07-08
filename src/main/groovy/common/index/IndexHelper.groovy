package common.index

import common.PropertiesHelper
import grails.converters.JSON
import jesol.IndexField

/**
 * Created by Niney on 2015-06-18.
 */
class IndexHelper {

    private static fieldNameMap = [:]

    def static makeJSONStringIndexData(String tableName, srcData) {
        return (makeIndexData(tableName, srcData) as JSON).toString()
    }
    def static synchronized getFieldNameList(tableName) {
        if(fieldNameMap[tableName] == null) {
            fieldNameMap[tableName] = IndexField.createCriteria().list() {
                eq('tableName', tableName)
                projections {
                    distinct('fieldName')
                }
            }
        }
        return fieldNameMap[tableName]
    }
    def static makeIndexData(String tableName, srcData) {
        def fieldNameList = getFieldNameList(tableName)

        def newObj = new Object()
        newObj.metaClass.id = srcData.id
        fieldNameList.each {f ->
            String fieldName = f
            def val = PropertiesHelper.getProperty(srcData, fieldName)
            if(fieldName.contains('.')) {
                def dot = fieldName.indexOf('.')
                def b = fieldName.substring(0, dot)
                def m = fieldName.substring(dot + 1, dot + 2).toUpperCase()
                def e = fieldName.substring(dot + 2)
                fieldName = b + m + e
            }
            newObj.metaClass[fieldName] = val
        }

        return newObj.properties
    }
}
