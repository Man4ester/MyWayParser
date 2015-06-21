package main.java.central.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import main.java.central.blogic.interfaces.IDestinationService;
import main.java.central.blogic.interfaces.IMapParserService;
import main.java.central.blogic.services.DestinationService;
import main.java.central.blogic.services.MapParserService;
import main.java.central.model.Destination;
import main.java.central.model.Location;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.select.Elements;

public class Main {

	private static final Logger logger = LogManager.getLogger(Main.class);

	static IDestinationService destServcie;

	static final String PARSE = "parse";
	static final String SHOW_ALL = "showAll";
	static final String FIND_ONE = "find_";
	static final String EXIT = "exit";

	public static void main(String[] args) {
		logger.info("Hello quest.");
		destServcie = new DestinationService();
		String s = "";
		while (!s.equalsIgnoreCase(EXIT)) {
			System.out.println("Enter command: parse, showAll, find_<ID>");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			try {
				s = br.readLine();
				dispatcher(s);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		System.exit(0);
	}

	private static void dispatcher(String command) {
		logger.info("Analyze command....");
		if (StringUtils.isBlank(command)) {
			return;
		}
		if (command.equalsIgnoreCase(PARSE)) {
			startParse();
		} else if (command.equalsIgnoreCase(SHOW_ALL)) {
			showAllLocation();
		} else if (command.toLowerCase().contains(FIND_ONE.toLowerCase())) {
			findOne(command);
		} else {
			logger.info("Enter correct commands");
			return;
		}
	}

	private static void showAllLocation() {
		logger.info("Show all destinations");
		List<Destination> lst = destServcie.finaAll();
		for (Destination destination : lst) {
			System.out.println(destination.getId() + " == "
					+ destination.getLabel() + " X:"
					+ destination.getLocationX() + " Y: "
					+ destination.getLocationY());
		}
	}

	private static void findOne(String command) {
		logger.info("findOne");
		String[] ar = command.split("_");
		if (ar.length != 2) {
			return;
		}
		try {
			long id = Long.parseLong(ar[1]);
			Destination model = destServcie.findById(id);
			if (model != null) {
				System.out.println(model.getLabel() + " X: "
						+ model.getLocationX() + " Y: " + model.getLocationY());
			} else {
				logger.info("Not exist with ID: " + id);
			}
		} catch (NumberFormatException e) {
			logger.info("Bad format");
		}
	}

	private static void startParse() {
		startAnalyze(
				"http://maps.turystam.in.ua/index.php/component/phocamaps/map/10-zahalna-karta?tmpl=component",
				"#toPMAddress option");
	}

	private static void startAnalyze(String url, String selector) {
		logger.info("Start parse....");
		IMapParserService service = new MapParserService();

		try {
			destServcie.truncate();
			Elements options = service.loadElementsByUrlAndSelector(url,
					selector);
			service.loadListLocationByElements(options);
			int count = 0;
			List<Location> lst = service.loadListLocationByElements(options);
			for (Location l : lst) {
				Destination model = new Destination(l);
				destServcie.saveUpdate(model);
				System.out.println(count + " " + l.getName() + " X:"
						+ l.getCoordinateX() + " Y:" + l.getCoordinateY());
				count++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}