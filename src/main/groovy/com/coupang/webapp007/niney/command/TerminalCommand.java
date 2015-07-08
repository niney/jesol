package com.coupang.webapp007.niney.command;

import java.util.List;
import java.util.Scanner;

import com.coupang.common.exception.PrepareException;
import com.coupang.webapp007.niney.facade.SearchFacade;
import com.coupang.webapp007.niney.model.Item;
import com.coupang.webapp007.niney.parse.SearchRequest;
import com.coupang.webapp007.niney.util.Resourcer;

public class TerminalCommand {

	private SearchFacade searchFacade;

	public void printMenu() {
		System.out.print(Resourcer.getString("MENU_STR"));
	}

	public void search(Scanner scanner) throws PrepareException {
		String keyword = scanner.nextLine();

		SearchRequest searchRequest = new SearchRequest.Builder(keyword)
				.currentPage(1)
				.pageSize(10)
				.build();

		this.searchFacade = new SearchFacade(searchRequest);
		searchFacade.searchAll();

		this.print();
	}

	public void searchW(String keyword) {
		SearchRequest searchRequest = new SearchRequest.Builder(keyword)
				.currentPage(1)
				.pageSize(10)
				.build();

		this.searchFacade = new SearchFacade(searchRequest);
		searchFacade.searchAll();
	}

	public void sort(Scanner scanner) throws PrepareException {
		if(!this.verify()) return;
		System.out.println("(1) ???, (2) ??, (3) ??");
		String order = scanner.nextLine();
		System.out.println("(1) ????, (2) ????");
		String bAscStr = scanner.nextLine();
		boolean bAsc = true;
		if("2".equals(bAscStr))
			bAsc = false;

		switch (Integer.parseInt(order)) {
			case 1:
				this.searchFacade.sort("title", bAsc);
				break;
			case 2:
				this.searchFacade.sort("price", bAsc);
				break;
			case 3:
				this.searchFacade.sort("amount", bAsc);
				break;
		}
		this.print();
	}

	public void clear() {
		if(!this.verify()) return;
		this.searchFacade.itemListClear();
	}

	public void print() throws PrepareException {
		if(!this.verify()) return;
		this.searchFacade.print();
	}

	public boolean verify() {
		if(this.searchFacade == null || this.searchFacade.getItemList().size() == 0) {
			return false;
		}
		return true;
	}


	public List<Item> getResult() {
		return searchFacade.getItemList();
	}
}
