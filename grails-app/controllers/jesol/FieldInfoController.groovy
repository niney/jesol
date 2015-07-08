package jesol

import common.controller.JSONRestfulController

class FieldInfoController extends JSONRestfulController<FieldInfo> {
    FieldInfoController() {
        super(FieldInfo)
    }

    def index(Integer max) {
//        def result = resource.createCriteria().list(
//                max: Math.min(max ?: 10, 255),
//                offset: params.offset ?: 0) {
//            if(params.tableName && params.fieldType) {
//                and {
//                    eq('tableName', params.tableName)
//                    eq('fieldType', params.fieldType)
//                }
//            }
//            order(params.sort ?: 'id', params.order ?: "desc")
//        }
//        response.addHeader('x-resource-data', "{ totalCount : " + result.totalCount + "}")
//        respond result, model: [("${resourceName}Count".toString()): result.totalCount]

//        def fieldType = params.fieldType.split('[|]')
//        def result = []
//        if(fieldType.size() > 1) {
//            for(int i = 0; i < fieldType.size(); i++) {
//                result.addAll(FieldInfo.getSearchFieldListMap(params.tableName, fieldType[i]))
//            }
//        } else
//            result = FieldInfo.getSearchFieldListMap(params.tableName, params.fieldType)

        def result = FieldInfo.getSearchFieldListMap(params.tableName, params.fieldType)

        respond result, model: [("${resourceName}Count".toString()): result.size()]
    }
}
