package common

/**
 * Created by Niney on 2015-06-18.
 */
class PropertiesHelper {

    @Deprecated
    def static getEvalProperty(object, String property) {
        Eval.x(object, 'x.' + property)
    }

    @Deprecated
    def static setMetaEvalProperty(object, String property, val) {
        Eval.xy(object, val, 'x.' + 'metaClass.' + property + '=y')
    }

    def static getProperty(obj, String property) {
        def propertyArr = property.split("[.]")
        propertyArr.each {
            obj = obj[it]
        }
        obj
    }

    def static setProperty(obj, String property, val) {
        obj.metaClass[property] = val
    }
}
