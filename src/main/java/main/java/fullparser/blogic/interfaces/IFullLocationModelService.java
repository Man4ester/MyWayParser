package main.java.fullparser.blogic.interfaces;

import java.util.List;

import main.java.fullparser.models.FullLocationModel;

import org.hibernate.criterion.DetachedCriteria;

public interface IFullLocationModelService {

	public FullLocationModel saveUpdate(FullLocationModel model)
			throws IllegalArgumentException;

	public FullLocationModel findById(long id) throws IllegalArgumentException;

	public List<FullLocationModel> finaAll();

	public List<FullLocationModel> finaByCriteria(DetachedCriteria cr,
			int from, int size) throws IllegalArgumentException;

	public void delete(long id) throws IllegalArgumentException,
			NullPointerException;

	public void truncate();

}
