package main.java.fullparser.models;

public class MenuModel {

	public MenuModel(String index, String url, String name, String category) {
		super();
		this.index = index;
		this.url = url;
		this.name = name;
		this.category=category;
	}

	public MenuModel() {

	}

	private String index;
	
	public String category;

	private String url;

	private String name;

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
