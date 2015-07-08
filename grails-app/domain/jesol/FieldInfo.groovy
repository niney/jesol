package jesol

import org.hibernate.criterion.CriteriaSpecification

class FieldInfo {

    String tableName
    String fieldType
    String fieldDisplay
    String fieldName
    int ord

    private static searchFieldListMap = [:]

    private static getInitSearchFieldListMap(String tableName, String fieldType) {
        def props = ['tableName', 'fieldType', 'fieldDisplay', 'fieldName', 'ord']
        searchFieldListMap[tableName][fieldType] = FieldInfo.createCriteria().list() {
            resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
            projections {
                props.each {
                    property(it,it)
                }
            }
            and {
                eq('tableName', tableName)
                eq('fieldType', fieldType)
            }
        }
    }
    public static synchronized getSearchFieldListMap(String tableName, String fieldType) {
        // 1. 이미 있는지 검사
        if(searchFieldListMap[tableName] != null && searchFieldListMap[tableName][fieldType] != null)
            return searchFieldListMap[tableName][fieldType]

        // 2. 첫 테이블이면 초기화
        if(searchFieldListMap[tableName] == null)
            searchFieldListMap[tableName] = [:]
        // 3. 정보 가져오기
        getInitSearchFieldListMap(tableName, fieldType)

        return searchFieldListMap[tableName][fieldType]

    }

    public static reload(String tableName, String fieldType) {
        getInitSearchFieldListMap(tableName, fieldType)
        return searchFieldListMap[tableName][fieldType]
    }

    public static void reload() {
        searchFieldListMap = null
    }

    static constraints = {
    }
}
