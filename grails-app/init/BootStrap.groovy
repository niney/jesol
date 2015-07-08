import jesol.IndexField
import jesol.FieldInfo

class BootStrap {

    def grailsApplication

    def init = { servletContext ->

        if(!IndexField.count()) {
            new IndexField(fieldId: "TITLE", fieldName: "title", fieldType: "K", tableName: "product").save();
            new IndexField(fieldId: "TITLE", fieldName: "title", fieldType: "S", tableName: "product").save();
            new IndexField(fieldId: "COMPANY", fieldName: "company", fieldType: "K", tableName: "product").save();
            new IndexField(fieldId: "COMPANY", fieldName: "company", fieldType: "S", tableName: "product").save();
            new IndexField(fieldId: "CONTENT", fieldName: "content", fieldType: "K", tableName: "product").save();
            new IndexField(fieldId: "CATEGORY.ID", fieldName: "category.id", fieldType: "K", tableName: "product").save();
        }

        if(!FieldInfo.count()) {
//            FieldInfo.deleteAll(FieldInfo.list())
            new FieldInfo(tableName: 'product', fieldType: 'SEARCH', fieldDisplay: '전체', fieldName: 'ALL', ord: 100).save();
            new FieldInfo(tableName: 'product', fieldType: 'SEARCH', fieldDisplay: '제품명', fieldName: 'title', ord: 200).save();
            new FieldInfo(tableName: 'product', fieldType: 'SEARCH', fieldDisplay: '본문', fieldName: 'content', ord: 300).save();
            new FieldInfo(tableName: 'product', fieldType: 'SEARCH', fieldDisplay: '회사', fieldName: 'company', ord: 400).save();

            new FieldInfo(tableName: 'category', fieldType: 'SEARCH', fieldDisplay: '이름', fieldName: 'title', ord: 100).save();

            new FieldInfo(tableName: 'product', fieldType: 'ALL', fieldDisplay: '전체', fieldName: 'title,content', ord: 100).save();
            new FieldInfo(tableName: 'product', fieldType: 'LIST', fieldDisplay: 'ID', fieldName: 'id', ord: 100).save();
            new FieldInfo(tableName: 'product', fieldType: 'LIST', fieldDisplay: '이름', fieldName: 'title', ord: 200).save();
            new FieldInfo(tableName: 'product', fieldType: 'LIST', fieldDisplay: '수량', fieldName: 'amount', ord: 300).save();
            new FieldInfo(tableName: 'product', fieldType: 'LIST', fieldDisplay: '가격', fieldName: 'price', ord: 400).save();

        }

        // ALL title, optionInfo
        // SEARCH title, optionInfo, company
        // LIST ...
        // DETAIL ...

//        JSON.createNamedConfig('jsonDeep') {
//            it.registerObjectMarshaller( new DeepDomainClassMarshaller(false, grailsApplication) )
//        }
    }
    def destroy = {
    }
}
