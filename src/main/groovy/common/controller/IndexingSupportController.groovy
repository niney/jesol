package common.controller

import org.apache.solr.client.solrj.SolrServerException

/**
 * Created by Niney on 2015-06-12.
 */
interface IndexingSupportController {
    def searchEngineIndex() throws SolrServerException
}
