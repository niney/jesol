package jesol

import org.hibernate.criterion.CriteriaSpecification

class IndexField {

    String tableName
    String fieldId
    String fieldName
    String fieldType

    private static indexFieldListMap = [:]
    private static getInitIndexFieldListMap(String tableName) {
        def props = ['tableName', 'fieldId', 'fieldName', 'fieldType']
        indexFieldListMap[tableName] = IndexField.createCriteria().list() {
            resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
            projections {
                props.each {
                    property(it,it)
                }
            }
            eq('tableName', tableName)
        }
    }
    public static synchronized getIndexFieldListMap(String tableName) {
        if(indexFieldListMap[tableName] == null) {
            getInitIndexFieldListMap(tableName);
        }
        indexFieldListMap[tableName]
    }

    public static reload(String tableName) {
        getInitIndexFieldListMap(tableName);
    }

    static constraints = {

    }
}
