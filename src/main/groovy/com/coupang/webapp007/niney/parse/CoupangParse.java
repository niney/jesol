package com.coupang.webapp007.niney.parse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.coupang.webapp007.niney.model.Item;
import com.coupang.webapp007.niney.util.Resourcer;

/**
 * @author niney
 *
 */
public class CoupangParse extends ParseBase {

	@Override
	public String getURL(SearchRequest searchRequest) {
		String param = "";
		try {
			param = String.format(Resourcer.getString("COUPANG_QUERY"), URLEncoder.encode(searchRequest.getKeyword(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return Resourcer.getString("COUPANG_URL") + param;
	}

	@Override
	public void parse() {
	
		Document doc = Jsoup.parseBodyFragment(this.getHtmlString());
		Element listElm = doc.getElementById("productList");
		
		
		Elements detailElms = listElm.select("li");
		for(Element detailElm : detailElms) {
			Item item = new Item();
			item.setCompany(Resourcer.getString("COUPANG_COMPANY"));
			
			Elements titleElms = detailElm.getElementsByTag("a");
			String title = titleElms.first().attr("title");
			item.setTitle(title);
			
			Elements priceElms = detailElm.getElementsByClass("price");
			String price = priceElms.select("em").html();
			item.setPrice(price);
			
			Elements peopleElms = detailElm.getElementsByClass("condition");
			String amount = peopleElms.select("em").html();
			item.setAmount(amount);
			
			this.getItemList().add(item);
		}
		
		
	}
	
	

}
