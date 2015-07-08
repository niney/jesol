package com.coupang.common.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 검색 결과 공통
 * @author niney
 *
 * @param <T>
 */
public class Result<T> {
	
	private List<T> list = new ArrayList<T>();

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
	
	public void addAll(List<? extends T> coll) {
		this.list.addAll(coll);
	}
	
	public void clear() {
		this.list.clear();
	}
	
}
