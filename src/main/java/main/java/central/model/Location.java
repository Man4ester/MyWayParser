package main.java.central.model;

public class Location {
	
	private String name;
	
	private String coordinateX;
	
	private String coordinateY;
	
	private String note;
	
	public Location(String name, String coordinateX, String coordinateY) {
		this.name = name;
		this.coordinateX = coordinateX;
		this.coordinateY = coordinateY;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCoordinateX() {
		return coordinateX;
	}

	public void setCoordinateX(String coordinateX) {
		this.coordinateX = coordinateX;
	}

	public String getCoordinateY() {
		return coordinateY;
	}

	public void setCoordinateY(String coordinateY) {
		this.coordinateY = coordinateY;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}