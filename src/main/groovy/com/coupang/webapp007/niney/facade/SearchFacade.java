package com.coupang.webapp007.niney.facade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.coupang.common.exception.PrepareException;
import com.coupang.common.model.Result;
import com.coupang.webapp007.niney.model.Item;
import com.coupang.webapp007.niney.parse.*;
import com.coupang.webapp007.niney.util.Resourcer;

public class SearchFacade {

	private SearchRequest searchRequest;
	private Result<Item> resultList = new Result<Item>();
	
	public List<Item> getItemList() {
		return resultList.getList();
	}

	public void setItemList(List<Item> itemList) {
		this.resultList.setList(itemList);
	}

	public SearchFacade(SearchRequest searchRequest) {
		this.searchRequest = searchRequest;
	}

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
	
	public void itemListClear() {
		this.resultList.clear();
	}

	public void verify() throws PrepareException {
		if(this.getItemList().size() == 0) {
			throw new PrepareException(); 
		}
	}

}
