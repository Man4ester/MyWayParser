package main.java.central.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DESTINATION")
public class Destination implements Serializable {

	private static final long serialVersionUID = 1L;

	public Destination(Location location) {
		this.label = location.getName();
		this.locationX=location.getCoordinateX();
		this.locationY=location.getCoordinateY();
		this.coordinateX = new  BigDecimal(this.locationX);
		this.coordinateY = new  BigDecimal(this.locationY);
	}
	public Destination(){
		
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String label;

	@Column(name="description", length=9000)
	private String description;

	private String pictureUrl;

	private String locationX;

	private String locationY;
	
	@Column(name="coordinateX", precision=15, scale=15)
	private BigDecimal coordinateX;
	
	@Column(name="coordinateY", precision=15, scale=15)
	private BigDecimal coordinateY;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getLocationX() {
		return locationX;
	}

	public void setLocationX(String locationX) {
		this.locationX = locationX;
	}

	public String getLocationY() {
		return locationY;
	}

	public void setLocationY(String locationY) {
		this.locationY = locationY;
	}
	public BigDecimal getCoordinateX() {
		return coordinateX;
	}
	public void setCoordinateX(BigDecimal coordinateX) {
		this.coordinateX = coordinateX;
	}
	public BigDecimal getCoordinateY() {
		return coordinateY;
	}
	public void setCoordinateY(BigDecimal coordinateY) {
		this.coordinateY = coordinateY;
	}

}
