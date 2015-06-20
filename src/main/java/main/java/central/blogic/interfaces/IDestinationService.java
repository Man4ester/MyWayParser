package main.java.central.blogic.interfaces;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import main.java.central.model.Destination;

public interface IDestinationService {
	
	public Destination saveUpdate(Destination model);
	
	public Destination findById(int id);
	
	public List<Destination> finaAll();
	
	public List<Destination> finaByCriteria(DetachedCriteria cr, int from, int size);
	
	public void delete(int id);

}
