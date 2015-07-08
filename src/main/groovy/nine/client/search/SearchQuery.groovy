package nine.client;

import grails.web.servlet.mvc.GrailsParameterMap;

/**
 * Created by Niney on 2015-06-12.
 */
public class SearchQuery {

    private GrailsParameterMap parameterMap;

    public SearchQuery(GrailsParameterMap parameterMap) {
        this.parameterMap = parameterMap;
    }

    public int getPage() {
        return (parameterMap.currentPage as Integer) ?: 1;
    }

    public void setPage(int page) {
        parameterMap.currentPage = page;
    }

    public int getMax() {
        return (parameterMap.max as Integer) ?: 10;
    }

    public void setMax(int max) {
        parameterMap.max = max;
    }

    public int getOffset() {
        return (parameterMap.offset as Integer) ?: 0;
    }

    public void setOffset(int offset) {
        parameterMap.offset = offset;
    }

    public String getSort() {
        return parameterMap.sort ?: '';
    }

    public void setSort(String sort) {
        parameterMap.sort = sort;
    }

    public String getOrder() {
        return parameterMap.order ?: '';
    }

    public void setOrder(String order) {
        parameterMap.order = order;
    }

    // todo 현재는 대문자로 처리
    public String getQt() {
        parameterMap.qt.toUpperCase() ?: '';
    }

    public void setQt(String qt) {
        parameterMap.qt = qt;
    }

    public String getQ() {
        return parameterMap.q ?: '';
    }

    public void setQ(String q) {
        parameterMap.q = q;
    }

    public String getQo() {
        parameterMap.qo ?: 'OR';
    }

    public void setQo(String qo) {
        parameterMap.qo = qo;
    }

    public float getBs() {
        return parameterMap.bs ?: 1.0f;
    }

    public void setBs(float bs) {
        parameterMap.bs = bs;
    }

    public String getQf() {
        return parameterMap.qf ?: '';
    }

    public void setQf(String qf) {
        parameterMap.qf = qf;
    }
}