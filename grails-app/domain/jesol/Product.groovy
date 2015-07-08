package jesol

import common.index.IndexHelper
import nine.client.IndexingSupportDomain
import grails.converters.JSON

class Product implements IndexingSupportDomain {

//    SocketService socketService

    Category category
    String title
    int amount
    int price
    String company
    int deliveryCharge
    String content
    ProductInfo productInfo

    Date dateCreated
    Date lastUpdated

//    Member createdBy
//    static hasOne = [productInfo:ProductInfo]
//    static hasMany = [
//            reviews : Review
//    ]


    static mapping = {
        content type:'text'
    }

    static constraints = {
    }

    def afterInsert() {
        afterInsertIndexing()
    }

    def afterUpdate() {
        afterUpdateIndexing()
    }

    def afterDelete() {
        afterDeleteIndexing()
    }

    @Override
    void afterInsertIndexing() {
        ProductIndex productIndex = new ProductIndex(product: this.id, idxData: IndexHelper.makeJSONStringIndexData('product', this), idxStatus: IdxStatus.INSERT)
//        ProductIndex productIndex = new ProductIndex(product: this.id, idxData: (this as JSON).toString(), idxStatus: IdxStatus.INSERT)
        productIndex.save()
    }

    @Override
    void afterUpdateIndexing() {

        ProductIndex productIndex = ProductIndex.findByProduct(this.id)
        productIndex.idxData = IndexHelper.makeJSONStringIndexData('product', this).toString()
        productIndex.idxStatus = IdxStatus.UPDATE
        productIndex.save()
    }

    @Override
    void afterDeleteIndexing() {
        ProductIndex productIndex = ProductIndex.findByProduct(this.id)
        productIndex.idxStatus = IdxStatus.DEL
        productIndex.save()
    }
}
