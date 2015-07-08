package com.coupang.webapp007.niney.model;

import java.util.Comparator;

public class Item {

	String company;
	String title;
	String amount;
	String price;
	
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAmount() {
		return amount;
	}
	public int getIntAmount() {
		String am = this.amount.replace("+", "").replace(",", "");
		am = am.equals("") ? "0" : am;
		return Integer.parseInt(am);
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getPrice() {
		return price;
	}
	public int getIntPrice() {
		return Integer.parseInt(this.price.replace(",", ""));
	}
	public void setPrice(String price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return String.format("%s | %s | %s | %s", company, title, amount, price);
	}
	
	public static class ItemCompare implements Comparator<Item> {
		
		private String order;
		private boolean bAsc;
		
		public ItemCompare(String order, boolean asc) {
			this.order = order;
			this.bAsc = asc;
		}

		@Override
		public int compare(Item o1, Item o2) {
			
			int result = 0;
			if("price".equals(order)) {
				result = o1.getIntPrice() > o2.getIntPrice() ? 1 : -1;
			} else if("title".equals(order)) {
				result = o1.getTitle().compareTo(o2.getTitle());
			} else if("amount".equals(order)) {
				result = o1.getIntAmount() > o2.getIntAmount() ? 1 : -1;
			}
			
			if(bAsc)
				return result;
			else
				return -result;
			
		}
		
	}
	
}
