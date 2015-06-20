package main.java.central.blogic.services;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import main.java.central.blogic.interfaces.IMapParserService;
import main.java.central.model.Location;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MapParserService implements IMapParserService {
	
	private final Charset UTF8_CHARSET = Charset.forName("UTF-8");

	private static final String SPLIT_SEPARATOR = ",";

	@Override
	public Elements loadElementsByUrlAndSelector(String url, String selector)
			throws IOException {
		if (StringUtil.isBlank(url)) {
			throw new IllegalArgumentException("URL is wrong");
		}
		if (StringUtil.isBlank(selector)) {
			throw new IllegalArgumentException("selector is wrong");
		}
		Document doc;
		doc = Jsoup.connect(url).timeout(0).get();
		Elements options = doc.select(selector);
		return options;
	}

	@Override
	public List<Location> loadListLocationByElements(Elements elements) {
		if (elements == null) {
			throw new IllegalArgumentException("Elements isnull");
		}
		Iterator<Element> it = elements.iterator();
		List<Location> result = new ArrayList<>();
		while (it.hasNext()) {
			Element node = it.next();
			List<String> coordinates = parseValueForCoordinates(
					node.attr("value"), SPLIT_SEPARATOR);
			Location loc = new Location(convertName(node.childNode(0).attr("text")),
					coordinates.get(0), coordinates.get(1));
			result.add(loc);
		}
		return result;
	}

	@Override
	public List<String> parseValueForCoordinates(String source,
			String splitSource) {
		List<String> res = new ArrayList<>();
		if (StringUtil.isBlank(splitSource)) {
			throw new IllegalArgumentException("splitSource is null");
		}
		if (!StringUtil.isBlank(source)) {
			String[] args = source.split(",");
			if (args != null && args.length == 2) {
				res.add(args[0].trim());
				res.add(args[1].trim());
			} else {
				throw new IllegalArgumentException("Bad split");
			}
		}
		return res;
	}
	
	
	private String convertName(String source){
		String convert =null;
		if(StringUtils.isNotBlank(source)){
			byte[] b = encodeUTF8(source);
			convert=decodeUTF8(b);
		}
		return convert;
				
	}
	String decodeUTF8(byte[] bytes) {
	    return new String(bytes, UTF8_CHARSET);
	}

	byte[] encodeUTF8(String string) {
	    return string.getBytes(UTF8_CHARSET);
	}

}
