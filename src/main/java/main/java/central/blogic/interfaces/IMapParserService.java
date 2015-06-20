package main.java.central.blogic.interfaces;

import java.io.IOException;
import java.util.List;

import main.java.central.model.Location;

import org.jsoup.select.Elements;


public interface IMapParserService {
	
	public Elements loadElementsByUrlAndSelector(String url, String selector) throws IOException;
	
	public List<Location> loadListLocationByElements(Elements elements);
	
	public List<String> parseValueForCoordinates(String source, String splitSource);

}
