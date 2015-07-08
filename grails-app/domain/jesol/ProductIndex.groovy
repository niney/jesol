package jesol

import grails.core.GrailsApplication
import kr.nine.client.product.ProductBuilder
import kr.nine.client.product.ProductDocument
import kr.nine.client.product.ProductIndexer
import org.apache.solr.common.SolrInputDocument

import static grails.async.Promises.*

class ProductIndex {

    long product
    String idxData
    String extIdxData
    IdxStatus idxStatus

    GrailsApplication grailsApplication

    static transients = ['grailsApplication']
    static constraints = {
        extIdxData nullable: true
        idxStatus blank: false, inList: IdxStatus.values() as List
    }

    static mapping = {
        idxData type:'text'
        extIdxData type:'text'
    }

    def afterInsert() {
        indexing()
    }

    def afterUpdate() {
        indexing()
    }

    def indexing() {
        if (idxStatus == IdxStatus.DEL)
            indexingDel()
        else if (idxStatus == IdxStatus.INSERT || idxStatus == idxStatus.UPDATE)
            indexingInsertUpdate()
    }

    /**
     * insert, update 색인
     * @return
     */
    private indexingInsertUpdate() {

        // 1. Document Make
        ProductBuilder productBuilder = new ProductBuilder(IndexField.getIndexFieldListMap('product'))
        SolrInputDocument doc = productBuilder.build(idxData)

        // 2. indexer
        ProductIndexer indexer = new ProductIndexer(grailsApplication.config.getProperty('grails.nine.serverUrl'))
        indexer.update(doc as SolrInputDocument)
        indexer.commit()

        // 3. status update
        idxStatus = IdxStatus.SET // index
    }

    /**
     * 색인 삭제
     * @return
     */
    private indexingDel() {
        // 1. indexer
        ProductIndexer indexer = new ProductIndexer(grailsApplication.config.getProperty('grails.nine.serverUrl'))
        indexer.delete(id.toString()) // indexingInsertUpdate
        indexer.commit()

        // 2. status update
        idxStatus = IdxStatus.RESET
    }
}
