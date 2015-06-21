package main.java.central.blogic.interfaces;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import main.java.central.model.Destination;

public interface IDestinationService {
	
	public Destination saveUpdate(Destination model) throws IllegalArgumentException;
	
	public Destination findById(long id) throws IllegalArgumentException;
	
	public List<Destination> finaAll();
	
	public List<Destination> finaByCriteria(DetachedCriteria cr, int from, int size) throws IllegalArgumentException;
	
	public void delete(long id) throws IllegalArgumentException, NullPointerException;
	
	public void truncate();

}
