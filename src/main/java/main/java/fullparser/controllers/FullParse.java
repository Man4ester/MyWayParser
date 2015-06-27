package main.java.fullparser.controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import main.java.fullparser.blogic.interfaces.IFullLocationModelService;
import main.java.fullparser.blogic.services.FullLocationModelService;
import main.java.fullparser.models.FullLocationModel;
import main.java.fullparser.models.MenuModel;

public class FullParse {

	private static final Logger logger = LogManager.getLogger(FullParse.class);

	private static final String INDEX = "http://turystam.in.ua/";

	private static final String IMG = "http://turystam.in.ua/";
	
	private static final String MENU_SELECTOR=".menu-nav li";
	
	private static final String MENU_SELECTOR_2=".megamenu li";

	private static final String FOLDER = "/home/bismark/tmp/turystam/";

	private static final String JPG = "jpg";
	
	private static final String HREF= "href";
	
	private static final String A="a";
	
	private static final String SRC="src";
	
	private static final String IMAGE_RULE=".item-page img";
	
	private static final String DESCRIPTION_RULE=".item-page p";
	
	private static final String CATEGORY_RULE=".category tr";
	
	private static final String SUB_CATEGORY= ".cat-children li";
	
	private static final String SUB_CATEGORY_2= ".categories-list";
	
	private static final String SUB_CATEGORY_CITY_DAY=".jcat-children li";
	
	private static final String LIMIT_HUC="?limit=100";

	private static IFullLocationModelService service;

	private static final List<String> itemsForParse = new ArrayList<String>();
	static {
		// itemsForParse.add("ВОДНИЙ БАСЕЙН");
		// itemsForParse.add("СЕЛА ТА СМТ");
		// itemsForParse.add("ПЕЧЕРИ УКРАЇНИ");
		// itemsForParse.add("МІСТА");
		// itemsForParse.add("ПАРКИ");
		// itemsForParse.add("ЗАМКИ");
		// itemsForParse.add("МУЗЕЇ");
		//itemsForParse.add("ЦЕРКВИ");
		//itemsForParse.add("ПАЛАЦИ");
		itemsForParse.add("ДЕНЬ МІСТА");
		
	}
	
	private static final List<String> specialItemsForParse = new ArrayList<String>();
	static{
		specialItemsForParse.add("ПАЛАЦИ");
		specialItemsForParse.add("ДЕНЬ МІСТА");
	}

	public static void main(String[] args) {
		logger.info("Satrt: " + (new Date()));
		service = new FullLocationModelService();
		String url = INDEX;
		String selector = MENU_SELECTOR;
		String selector_main_menu = MENU_SELECTOR_2;

		List<MenuModel> menu = loadMenuItems(url, selector, selector_main_menu);
		List<MenuModel> lstItems = loadSubItems(menu, url);
		List<MenuModel> finalItemsForParse = loadFinalItemsForParsing(url, lstItems);
		parseContentByItems(finalItemsForParse);
		logger.info("Done: " + (new Date()));
	}

	/*
	 * load main items from menu
	 */
	public static List<MenuModel> loadMenuItems(String url, String selector, String selector_main_menu) {
		logger.info("load menu items");
		List<MenuModel> menu = new ArrayList<MenuModel>();
		try {
			Document doc;
			doc = Jsoup.connect(url).timeout(0).get();
			Elements options = doc.select(selector);
			Iterator<Element> it = options.iterator();
			while (it.hasNext()) {
				Element node = it.next();
				Elements el = node.getElementsByAttribute(HREF);
				Element elementLink = el.get(0);
				MenuModel mod = new MenuModel(INDEX, elementLink.attr(HREF), elementLink.text(), null);
				menu.add(mod);
			}

			options = doc.select(selector_main_menu);
			it = options.iterator();
			while (it.hasNext()) {
				Element node = it.next();
				Elements el = node.getElementsByAttribute(HREF);
				Element elementLink = el.get(0);
				MenuModel mod = new MenuModel(INDEX, elementLink.attr(HREF), elementLink.text(), null);
				menu.add(mod);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return menu;

	}

	/*
	 * load sub items for parsing
	 */
	public static List<MenuModel> loadSubItems(List<MenuModel> menu, String url) {
		logger.info("loadSubItems");
		Document doc;
		Elements options;
		Iterator<Element> it;
		List<MenuModel> lstItems = new ArrayList<MenuModel>();
		try {
			for (MenuModel el : menu) {
				logger.info(el.getName() + " " + el.getUrl());
				for (String item : itemsForParse) {
					if (el.getName().equalsIgnoreCase(item)) {
						logger.info("parsing...");
						String url_2 = url + el.getUrl();
						doc = Jsoup.connect(url_2).timeout(0).get();
						options = doc.select(SUB_CATEGORY);
						if(options.isEmpty()){
							options=doc.select(SUB_CATEGORY_2);
						}
						if(options.isEmpty()){
							options=doc.select(SUB_CATEGORY_CITY_DAY);
						}
						it = options.iterator();
						while (it.hasNext()) {
							Element node = it.next();
							Elements el_2 = node.getElementsByTag(A);
							if(el_2.size()==1){
								lstItems.add(new MenuModel(url_2, url + el_2.attr(HREF), el_2.text(), item));
							}else{
								for (Element element : el_2) {
									lstItems.add(new MenuModel(url_2, url + element.attr(HREF), element.text(), item));
								}
							}
						}
					}
				}

			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return lstItems;
	}

	/*
	 * load final list for parsing
	 */
	public static List<MenuModel> loadFinalItemsForParsing(String url, List<MenuModel> lstItems) {
		logger.info("Load final lisr of items for parsing");
		Document doc;
		Elements options;
		Iterator<Element> it;
		List<MenuModel> finalItemsForParse = new ArrayList<>();
		try {
			for (MenuModel menuModel : lstItems) {
				logger.info(menuModel.getName() + " " + menuModel.getUrl());
				doc = Jsoup.connect(menuModel.getUrl()).timeout(0).get();
				options = doc.select(CATEGORY_RULE);
				it = options.iterator();
				String specUrl = specialItemsForParse.contains(menuModel.getCategory())==true?LIMIT_HUC:"";
				while (it.hasNext()) {
					Element node = it.next();
					Elements el_2 = node.getElementsByTag(A);
					if (el_2.size() == 1) {
						finalItemsForParse
								.add(new MenuModel(url, url + el_2.attr(HREF)+specUrl, el_2.text(), menuModel.getName()));
					}
				}
			}
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
		}
		return finalItemsForParse;
	}

	public static void parseContentByItems(List<MenuModel> finalItemsForParse) {
		logger.info("parse content by Itemas");
		Document doc;
		Elements options;
		try {
			for (MenuModel menuModel : finalItemsForParse) {
				logger.info(menuModel.getName() + " " + menuModel.getUrl());
				doc = Jsoup.connect(menuModel.getUrl()).timeout(0).get();
				options = doc.select(IMAGE_RULE);
				if (!options.isEmpty()) {
					options.get(0).attr(SRC);
				} else {
					logger.error("No image: " + menuModel.getUrl());
				}

				File outputfile = null;
				File folder = null;
				if (!options.isEmpty()) {
					try {
						URL url_net = new URL(IMG + options.get(0).attr(SRC));
						BufferedImage img = ImageIO.read(url_net);
						folder = new File(FOLDER + menuModel.getCategory());
						folder.mkdirs();
						String[] arg = options.get(0).attr(SRC).split("/");
						outputfile = new File(folder.getPath() + File.separator + arg[arg.length - 1]);
						ImageIO.write(img, JPG, outputfile);
					} catch (Exception e) {
						logger.error(menuModel.getUrl());
						logger.error(e.getMessage(), e);
					}
				}
				FullLocationModel model = new FullLocationModel();
				model.setDescription(doc.select(DESCRIPTION_RULE).text());
				model.setCategory(menuModel.getCategory());
				if (outputfile != null) {
					model.setImg(outputfile.getName());
				}
				model.setName(menuModel.getName());
				model.setUrl(menuModel.getUrl());
				try {
					service.saveUpdate(model);
				} catch (Exception e) {
					logger.error(menuModel.getUrl(), e);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.info("Done prasing....");
	}

}
