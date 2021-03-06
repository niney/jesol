package com.coupang.webapp007.niney.parse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.coupang.webapp007.niney.model.Item;

/**
 * @author niney
 *
 */
public abstract class ParseBase {
	
	private String htmlString;
	private List<Item> itemList = new ArrayList<Item>();
	
	public String getHtmlString() {
		return htmlString;
	}
	
	public void setHtmlString(String htmlString) {
		this.htmlString = htmlString;
	}
	
	public List<Item> getItemList() {
		return itemList;
	}
	
	public Item getFirstItem() {
		return itemList.get(0);
	}

	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
	}

	/**
	 * SearchRequest param
	 * @param searchRequest
	 * @return
	 */
	public abstract String getURL(SearchRequest searchRequest);
	
	/**
	 */
	public abstract void parse();

	/**
	 * @param searchRequest
	 */
	public void ready(SearchRequest searchRequest) {
		
        BufferedReader br = null;
        try {
        	URL u = new URL(this.getURL(searchRequest));
        	
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setRequestProperty("Accept-Charset", "UTF-8"); 
            huc.setRequestMethod("GET");
            huc.connect();
            String inputLine;
            StringBuilder sb = new StringBuilder();
            br = new BufferedReader(new InputStreamReader(huc.getInputStream(), "UTF-8"));
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            br.close();
            this.htmlString = sb.toString();
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}


}
