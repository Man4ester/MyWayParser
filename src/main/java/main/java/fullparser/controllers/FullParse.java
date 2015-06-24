package main.java.fullparser.controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import main.java.fullparser.blogic.interfaces.IFullLocationModelService;
import main.java.fullparser.blogic.services.FullLocationModelService;
import main.java.fullparser.models.FullLocationModel;
import main.java.fullparser.models.MenuModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FullParse {

	private static final Logger logger = LogManager.getLogger(FullParse.class);

	private static final String INDEX = "http://turystam.in.ua/";

	private static final String IMG = "http://turystam.in.ua/";

	private static final String FOLDER = "/home/bismark/tmp/turystam/";

	private static final String JPG = "jpg";

	private static final List<String> itemsForParse = new ArrayList<String>();
	static {
//		itemsForParse.add("ВОДНИЙ БАСЕЙН"); 
		//itemsForParse.add("СЕЛА ТА СМТ");
		// itemsForParse.add("ПЕЧЕРИ УКРАЇНИ");
		 //itemsForParse.add("МІСТА");
		//itemsForParse.add("ПАРКИ");
		itemsForParse.add("ЗАМКИ");
		//itemsForParse.add("МУЗЕЇ");
		//itemsForParse.add("ЦЕРКВИ");
		
		
	}

	private static IFullLocationModelService service;

	public static void main(String[] args) {
		logger.info("Satrt: "+(new Date()));
		service = new FullLocationModelService();
		String url = "http://turystam.in.ua/index.php";
		String selector = ".menu-nav li";
		String selector_main_menu = ".megamenu li";
		List<MenuModel> menu = new ArrayList<MenuModel>();
		try {
			Document doc;
			doc = Jsoup.connect(url).timeout(0).get();
			Elements options = doc.select(selector);
			Iterator<Element> it = options.iterator();
			while (it.hasNext()) {
				Element node = it.next();
				Elements el = node.getElementsByAttribute("href");
				Element elementLink = el.get(0);
				MenuModel mod = new MenuModel(INDEX, elementLink.attr("href"),
						elementLink.text(), null);
				menu.add(mod);
			}
			
			options = doc.select(selector_main_menu);
			it = options.iterator();
			while (it.hasNext()) {
				Element node = it.next();
				Elements el = node.getElementsByAttribute("href");
				Element elementLink = el.get(0);
				MenuModel mod = new MenuModel(INDEX, elementLink.attr("href"),
						elementLink.text(), null);
				menu.add(mod);
			}

			List<MenuModel> lstItems = new ArrayList<MenuModel>();
			for (MenuModel el : menu) {
				logger.info(el.getName() + " " + el.getUrl());
				for (String item : itemsForParse) {
					if (el.getName().equalsIgnoreCase(item)) {
						logger.info("parsing...");
						String url_2 = url + el.getUrl();
						doc = Jsoup.connect(url_2).timeout(0).get();
						options = doc.select(".cat-children li");
						it = options.iterator();
						while (it.hasNext()) {
							Element node = it.next();
							Elements el_2 = node.getElementsByTag("a");
							lstItems.add(new MenuModel(url_2, url
									+ el_2.attr("href"), el_2.text(), null));
						}
					}
				}

			}

			List<MenuModel> finalItemsForParse = new ArrayList<MenuModel>();
			for (MenuModel menuModel : lstItems) {
				logger.info(menuModel.getName() + " "
						+ menuModel.getUrl());
				doc = Jsoup.connect(menuModel.getUrl()).timeout(0).get();
				options = doc.select(".category tr");
				it = options.iterator();
				while (it.hasNext()) {
					Element node = it.next();
					Elements el_2 = node.getElementsByTag("a");
					if (el_2.size() == 1) {
						finalItemsForParse.add(new MenuModel(url, url
								+ el_2.attr("href"), el_2.text(), menuModel
								.getName()));
					}
				}

			}

			for (MenuModel menuModel : finalItemsForParse) {
				logger.info(menuModel.getName() + " "
						+ menuModel.getUrl());
				doc = Jsoup.connect(menuModel.getUrl()).timeout(0).get();
				options = doc.select(".item-page img");
				if (!options.isEmpty()) {
					options.get(0).attr("src");
				} else {
					logger.info("No image: " + menuModel.getUrl());
				}

				File outputfile = null;
				File folder = null;
				if (!options.isEmpty()) {
					try {
						URL url_net = new URL(IMG + options.get(0).attr("src"));
						BufferedImage img = ImageIO.read(url_net);
						folder = new File(FOLDER + menuModel.getCategory());
						folder.mkdirs();
						String[] arg = options.get(0).attr("src").split("/");
						outputfile = new File(folder.getPath() + File.separator
								+ arg[arg.length - 1]);
						ImageIO.write(img, JPG, outputfile);
					} catch (Exception e) {
						logger.error(menuModel.getUrl());
						logger.error(e.getMessage(), e);
					}
				}
				FullLocationModel model = new FullLocationModel();
				model.setDescription(doc.select(".item-page p").text());
				model.setCategory(menuModel.getCategory());
				if (outputfile != null) {
					model.setImg(outputfile.getName());
				}
				model.setName(menuModel.getName());
				model.setUrl(menuModel.getUrl());
				try{
					service.saveUpdate(model);
				}catch(Exception e){
					logger.error(menuModel.getUrl(),e);
				}
				

			}
			logger.info("Done: "+(new Date()));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
