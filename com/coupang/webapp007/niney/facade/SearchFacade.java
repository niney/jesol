package com.coupang.webapp007.niney.facade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.coupang.common.exception.PrepareException;
import com.coupang.common.model.Result;
import com.coupang.webapp007.niney.model.Item;
import com.coupang.webapp007.niney.parse.*;
import com.coupang.webapp007.niney.util.Resourcer;

/**
 * 검색 관련 Facade
 * @author niney
 *
 */
public class SearchFacade {

	private SearchRequest searchRequest; // 검색 요청 정보
	private Result<Item> resultList = new Result<Item>(); // 검색결과  
	
	public List<Item> getItemList() {
		return resultList.getList();
	}

	public void setItemList(List<Item> itemList) {
		this.resultList.setList(itemList);
	}

	/**
	 * 검색 param setting
	 * @param searchRequest
	 */
	public SearchFacade(SearchRequest searchRequest) {
		this.searchRequest = searchRequest;
	}
	
	/**
	 * 모두 검색
	 */
	public void searchAll() {
		
		List<String> parseList = new ArrayList<String>();
		Collections.addAll(parseList, Resourcer.getString("SEARCH_ALL_CLASS").split(","));
		
		for(String clsName : parseList) {
			try {
				Class<? extends ParseBase> parseCls = Class.forName(clsName).asSubclass(ParseBase.class);
				ParseBase parse = parseCls.newInstance();
				parse.ready(searchRequest);
				parse.parse();
				resultList.addAll(parse.getItemList());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 정렬
	 * @param order 정렬이름
	 * @param bAsc asc, desc
	 * @throws PrepareException
	 */
	public void sort(String order, boolean bAsc) throws PrepareException {
		this.verify();
		Collections.sort(this.getItemList(), new Item.ItemCompare(order, bAsc));
	}

	public void print() throws PrepareException {
		this.verify();
		for(Item item : this.getItemList()) {
			System.out.println(item);
		}
	}
	
	/**
	 * 초기화
	 */
	public void itemListClear() {
		this.resultList.clear();
	}
	
	/**
	 * 검증
	 * @throws PrepareException
	 */
	public void verify() throws PrepareException {
		if(this.getItemList().size() == 0) {
			throw new PrepareException(); 
		}
	}

}
