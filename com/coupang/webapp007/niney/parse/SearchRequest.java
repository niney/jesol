package com.coupang.webapp007.niney.parse;

/**
 * 검색 정보
 * @author niney
 *
 */
public class SearchRequest {
	
	private String keyword;
	private int pageSize;
	private int currentPage;			

	public static class Builder {
		private String keyword;
		private int pageSize = 10;
		private int currentPage = 1;
		
		public Builder(String keyword) {
			this.keyword = keyword;
		}
		
		public Builder pageSize(int pageSize) {
			this.pageSize = pageSize;
			return this;
		}
		public Builder currentPage(int currentSize) {
			this.currentPage = currentSize;
			return this;
		}
		
		public SearchRequest build() {
			return new SearchRequest(this);
		}
		
	}
	
	private SearchRequest(Builder builder) {
		this.keyword = builder.keyword;
		this.pageSize = builder.pageSize;
		this.currentPage = builder.currentPage;
	}
	
	public String getKeyword() {
		return this.keyword;
	}
	
	public int getPageSize() {
		return this.pageSize;
	}
	
	public int getCurrentPage() {
		return this.currentPage;
	}

}
