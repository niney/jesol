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
 * 위메프 site parsing
 * @author Administrator
 *
 */
public class Wemarket extends ParseBase {

	@Override
	public String getURL(SearchRequest searchRequest) {
		String param = "";
		try {
			param = String.format(Resourcer.getString("WEMARKET_QUERY"), URLEncoder.encode(searchRequest.getKeyword(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return Resourcer.getString("WEMARKET_URL") + param;
	}

	@Override
	public void parse() {
		Document doc = Jsoup.parseBodyFragment(this.getHtmlString());
		Elements listElms = doc.getElementsByClass("list_combine");
		
		Elements detailElms = listElms.first().select("li");
		for(Element detailElm : detailElms) {
			Item item = new Item();
			item.setCompany(Resourcer.getString("WEMARKET_COMPANY"));
			
			Elements titleElms = detailElm.getElementsByClass("standardinfo");
			String title = titleElms.html();
			item.setTitle(title);
			
			Elements priceElms = detailElm.getElementsByClass("sale");
			String price = priceElms.text().replace("원", "").replace("~", "");
			item.setPrice(price);
			
			Elements peopleElms = detailElm.getElementsByClass("point");
			String amount = peopleElms.html();
			item.setAmount(amount);
			
			this.getItemList().add(item);
		}
		
		
	}

}
