package main.java.central.controller;

import java.io.IOException;
import java.util.List;

import main.java.central.blogic.interfaces.IMapParserService;
import main.java.central.blogic.services.MapParserService;
import main.java.central.model.Location;

import org.jsoup.select.Elements;


public class Main {

	public static void main(String[] args) {
		startAnalyze(
				"http://maps.turystam.in.ua/index.php/component/phocamaps/map/10-zahalna-karta?tmpl=component",
				"#toPMAddress option");

	}

	private static void startAnalyze(String url, String selector) {
		IMapParserService service = new MapParserService();

		try {
			Elements options = service.loadElementsByUrlAndSelector(url,
					selector);
			service.loadListLocationByElements(options);
			int count = 0;
			List<Location> lst = service.loadListLocationByElements(options);
			for (Location l : lst) {
				System.out.println(count + " " + l.getName() + " X:"
						+ l.getCoordinateX() + " Y:" + l.getCoordinateY());
				count++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}