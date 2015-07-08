package nine.client.search

import grails.core.GrailsApplication
import groovy.util.logging.Slf4j
import jesol.Product
import kr.nine.client.product.ProductQuery
import kr.nine.client.product.ProductSearchResult
import kr.nine.client.product.ProductSearcher
import kr.nine.common.Field
import kr.nine.common.SortField
import nine.client.SearchQuery
import grails.util.Holders
/**
 * Created by Niney on 2015-06-12.
 */
@Slf4j
class ProductSearchBuilder extends SearchBuilder {

    GrailsApplication grailsApplication
    SearchQuery searchQuery

    public ProductSearchBuilder(SearchQuery searchQuery) {
        this.searchQuery = searchQuery;
    }

    public ProductSearchResult build() {
        ProductSearcher productSearcher = new ProductSearcher(Holders.config.getProperty('grails.nine.serverUrl'))
        ProductQuery query = new ProductQuery()
        query.setPage(searchQuery.page, searchQuery.max)
        queryFieldSetting(query)
        sortFieldSetting(query)
        log.info(query.getSortQuery())
        ProductSearchResult results = productSearcher.search(query)
        return results
    }

    private queryFieldSetting(ProductQuery query) {
        if((searchQuery.qt == '' || searchQuery.qt == 'ALL') && searchQuery.q)
            query.addTextSearchItem('ALL', searchQuery.q)
        else if(searchQuery.qt != '' && searchQuery.q)
            query.addTextSearchItem(searchQuery.qt, searchQuery.q)
    }

    private sortFieldSetting(ProductQuery query) {
        if(searchQuery.sort != '' && searchQuery.order) {
            // S_ 는 지정된 sort field
            String sort = ''
            if (!searchQuery.sort.startsWith("S_"))
                sort = searchQuery.sort.toUpperCase()

            if (searchQuery.order == 'asc')
                query.addSortField(sort, SortField.SortType.ASC)
            else
                query.addSortField(sort, SortField.SortType.DESC)
        }
    }

}
